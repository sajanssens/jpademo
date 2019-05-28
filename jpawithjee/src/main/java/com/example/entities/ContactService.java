package com.example.entities;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

public class ContactService {

    private EntityManager em;

    public ContactService(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        em.persist(c);
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
            contact.setFirstname(fn);
        }
        return contact;
    }

    public void updateFirstname(Contact c) {
        em.persist(c);
    }

    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            em.remove(contact);
        }
    }

}
