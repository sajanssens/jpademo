package com.example.domain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ContactDao {

    // @PersistenceContext // works only when ran in a container
    private EntityManager em;

    public ContactDao(EntityManager em) {
        this.em = em;
    }

    public void save(Contact c) {
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
    }

    public Contact update(Contact c) {
        em.getTransaction().begin();
        Contact merged = em.merge(c);
        em.getTransaction().commit();
        return merged;
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
