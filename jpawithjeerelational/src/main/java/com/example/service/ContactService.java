package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Department;
import com.example.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public Contact find(long id) {
        return em.find(Contact.class, id);
    }

    public Contact removeLeaseCar(Contact c) {
        c.setLeaseCar(null);
        return em.merge(c);
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

    public List<Department> findDepartmentByName(String name) {
        TypedQuery<Department> query = em.createNamedQuery("findByName", Department.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }

    public List<Contact> findByNameNative(String name) {
        Query query = em.createNativeQuery("SELECT * FROM Contact WHERE name like ?", Contact.class);
        query.setParameter(1, name + "%");
        return query.getResultList();
    }

    public List<Contact> findUsingCriteriaAPI() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> q = cb.createQuery(Contact.class);

        Root<Contact> c = q.from(Contact.class);
        q.select(c)
                .where(
                        cb.and(
                                cb.equal(c.get("name"), "bram"),
                                cb.equal(c.get("email"), "s.a.janssens@gmail.com")));
        return em.createQuery(q).getResultList();
    }

    @Autowired
    private ContactRepository repo;

    public List<Contact> findByName(String n){
        return repo.findByName(n);
    }

    public List<Contact> findByNameOrEmail(String n, String e){
        return repo.findByNameOrEmail(n, e);
    }

}
