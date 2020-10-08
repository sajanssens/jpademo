package com.example.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

public class ContactDaoDetachDRY {

    // @PersistenceContext // works only when ran in a container
    private final EntityManager em;

    private final Logger log = LoggerFactory.getLogger(ContactDaoDetachDRY.class);

    public ContactDaoDetachDRY(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        performTransaction(em::persist, c);
    }

    public Contact update(Contact c) {
        performTransaction(em::merge, c);
        return c;
    }

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    public Contact updateFirstname(long id, String fn) {
        Contact contact = find(id);
        performTransaction(contact::setFirstname, fn);
        return contact;
    }

    private <T> void performTransaction(Consumer<T> action, T t) {
        if (t == null) return;

        try {
            em.getTransaction().begin();
            action.accept(t);
            em.flush();
            em.clear(); //detach
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Something bad happened. Rolling back... " + e.getMessage());
            em.getTransaction().rollback();
        }
    }

}
