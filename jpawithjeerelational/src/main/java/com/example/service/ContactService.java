package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Department;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContactService {

    @PersistenceContext
    private EntityManager em;

    public void create(Contact c) {
        em.persist(c);
    }

    public Contact update(Contact c) {
        return em.merge(c);
    }

    public Contact removeLeaseCar(Contact c) {
        c.setLeaseCar(null);
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

    public void delete(long id) {
        Contact contact = find(id);
        if (contact != null) {
            em.remove(contact);
        }
    }

    public List<Contact> findByDepartment(Department d) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.departmentBossOf.id = :id", Contact.class);
        query.setParameter("id", d.getId());
        return query.getResultList();
    }

    public List<Contact> findByDepartmentUsingIN(Department d) {
        TypedQuery<Contact> query = em.createQuery(
                "SELECT c " +
                        "FROM Department d, " +
                        "   IN (d.contacts) c " +
                        "   WHERE d.id = :id", Contact.class);
        query.setParameter("id", d.getId());
        return query.getResultList();
    }

}
