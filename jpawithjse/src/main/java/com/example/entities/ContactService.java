package com.example.entities;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ContactService {

    // @PersistenceContext // works only when ran in ee-container
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
        em.getTransaction().begin();
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c", Contact.class);
        List<Contact> resultList = query.getResultList();
        em.getTransaction().commit();
        return resultList;

    }

    public Contact find(long id) {
        em.getTransaction().begin();
        Contact contact = em.find(Contact.class, id);
        em.getTransaction().commit();
        return contact;
    }

    public Contact updateFirstname(long id, String fn) {
        Contact contact = find(id);
        em.getTransaction().begin();
        if (contact != null) {
            contact.setFirstname(fn);
        }
        em.getTransaction().commit();
        return contact;
    }

    public void updateFirstname(Contact c) {
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
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
