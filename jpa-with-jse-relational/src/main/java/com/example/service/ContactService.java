package com.example.service;

import com.example.domain.Contact;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ContactService {

    // @PersistenceContext // works only when ran in a container
    private EntityManager em;

    public ContactService(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
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
            contact.setName(fn);
            em.getTransaction().commit();
        }
        return contact;
    }

    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            em.getTransaction().begin();
            em.remove(contact);
            em.getTransaction().commit();
        }
    }


}
