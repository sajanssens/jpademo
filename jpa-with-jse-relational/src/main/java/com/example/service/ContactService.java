package com.example.service;

import com.example.domain.Contact;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ContactService {

    private EntityManager em;

    public ContactService(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        try {
            begin();
            em.persist(c);
            commitAndClear();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void saveWithoutCatchAndRollback(Contact c) {
        begin();
        em.persist(c);
        commitAndClear();
    }

    public void update(Contact c) {
        try {
            begin();
            em.merge(c);
            commitAndClear();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public List<Contact> findAll() {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c", Contact.class);
        return query.getResultList();
    }

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    public Contact updateFirstname(Contact c, String fn) {
        Contact contact = find(c.getId());
        if (contact != null) {
            try {
                begin();
                contact.setName(fn);
                commitAndClear();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        }
        return contact;
    }

    public void remove(Contact c) {
        Contact contact = find(c.getId());
        if (contact != null) {
            try {
                begin();
                em.remove(contact);
                commitAndClear();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        }

    }

    private void begin() {
        em.getTransaction().begin();
    }

    private void commitAndClear() {
        em.getTransaction().commit();
        em.clear();
    }

}
