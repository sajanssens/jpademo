package com.example.service;

import com.example.domain.Contact;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class ContactService {

    @PersistenceContext
    private EntityManager em;

    public void save(Contact c) {
        em.persist(c);
    }

    public Contact update(Contact c) {
        return em.merge(c);
    }

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    public Contact updateFirstname(long id, String fn) {
        Contact contact = find(id);
        if (contact != null) {
            contact.setName(fn);
        }
        return contact;
    }

    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            em.remove(contact);
        }
    }

}
