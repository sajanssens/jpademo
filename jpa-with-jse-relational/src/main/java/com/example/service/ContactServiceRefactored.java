package com.example.service;

import com.example.domain.Contact;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;

public class ContactServiceRefactored {

    private EntityManager em;

    public ContactServiceRefactored(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        performTransaction(em::persist, c);
    }

    public void update(Contact c) { performTransaction(em::merge, c); }

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
            String name = contact.getName();
            performTransaction(contact::setName, name);
        }
        return contact;
    }

    public void remove(long id) {
        Contact contact = find(id);
        if (contact != null) {
            performTransaction(em::remove, contact);
        }
    }

    private <T> void performTransaction(Consumer<T> action, T t) {
        try {
            em.getTransaction().begin();
            action.accept(t);
            em.getTransaction().commit();
            em.clear();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

}
