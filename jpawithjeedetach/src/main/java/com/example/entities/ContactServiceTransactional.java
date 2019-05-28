package com.example.entities;

import com.example.entities.Contact;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class ContactServiceTransactional {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Contact c) {
        em.persist(c);
    }

    @Transactional
    public Contact update(Contact c) {
        return em.merge(c);
    }

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    @Transactional
    public Contact updateFirstname(long id, String fn) {
        Contact contact = find(id);
        if (contact != null) {
            contact.setFirstname(fn);
        }
        return contact;
    }

    @Transactional
    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            em.remove(contact);
        }
    }

}
