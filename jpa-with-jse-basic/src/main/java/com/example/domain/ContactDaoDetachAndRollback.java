package com.example.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ContactDaoDetachAndRollback {

    // @PersistenceContext // works only when ran in a container
    private final EntityManager em;

    private final Logger log = LoggerFactory.getLogger(ContactDaoDetachAndRollback.class);

    public ContactDaoDetachAndRollback(EntityManager em) {
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
        try {
            em.getTransaction().begin();
            Contact merged = em.merge(c);
            em.flush();
            em.clear(); //detach
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            log.error("Something bad happened. Rolling back... " + e.getMessage());
            em.getTransaction().rollback();
        }
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
        if (contact != null) {
            em.getTransaction().begin();
            contact.setFirstname(fn);
            em.flush();
            em.clear(); //detach
            em.getTransaction().commit();
        }
        return contact;
    }

    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            try {
                em.getTransaction().begin();
                em.remove(contact);
                em.flush();
                em.clear(); //detach
                em.getTransaction().commit();
            } catch (Exception e) {
                log.error("Something bad happened. Rolling back... " + e.getMessage());
                em.getTransaction().rollback();
            }
        }
    }

}
