package com.example.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;

public class ContactDaoDetachAndRollbackDRY {

    // @PersistenceContext // works only when ran in a container
    private final EntityManager em;

    private final Logger log = LoggerFactory.getLogger(ContactDaoDetachAndRollbackDRY.class);

    public ContactDaoDetachAndRollbackDRY(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        performTransaction(em::persist, c);
    }

    public Contact update(Contact c) {
        performTransaction(em::merge, c);
        return c;
    }

    public List<Contact> findAll() {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c", Contact.class);
        return query.getResultList();
    }

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    public Contact updateFirstname(long id, String fn) {
        Contact contact = find(id);
        performTransaction(contact::setFirstname, fn);
        return contact;
    }

    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            performTransaction(em::remove, contact);
        }
    }

    private <T> void performTransaction(Consumer<T> action, T subject) {
        if (subject == null) return;

        try {
            em.getTransaction().begin();
            action.accept(subject); // perform action with subject as argument
            em.flush();
            em.clear(); //detach
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Something bad happened. Rolling back... " + e.getMessage());
            em.getTransaction().rollback();
        }
    }

}
