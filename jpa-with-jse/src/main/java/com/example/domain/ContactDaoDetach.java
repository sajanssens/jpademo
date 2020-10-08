package com.example.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class ContactDaoDetach {

    // @PersistenceContext // works only when ran in a container
    private final EntityManager em;

    private final Logger log = LoggerFactory.getLogger(ContactDaoDetach.class);

    public ContactDaoDetach(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.flush();
            em.clear(); //detach
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Something bad happened. Rolling back... " + e.getMessage());
            em.getTransaction().rollback();
        }
    }

    public Contact update(Contact c) {
        em.getTransaction().begin();
        Contact merged = em.merge(c);
        em.flush();
        em.clear(); //detach
        em.getTransaction().commit();
        return merged;
    }

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    public Contact updateFirstname(long id, String fn) {
        Contact contact = find(id);
        if (contact != null) {
            em.getTransaction().begin();
            contact.setFirstname(fn);
            em.flush();
            em.clear(); //detach
            em.getTransaction().commit();
        }
        return contact;
    }

}
