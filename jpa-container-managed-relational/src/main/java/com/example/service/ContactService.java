package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Department;
import com.example.domain.Laptop;
import com.example.domain.ParkingSpace;
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

    @Autowired private DepartmentService departmentService;

    // Basic CRUD -----------

    public void create(Contact c) { em.persist(c); }

    public Contact find(long id) { return em.find(Contact.class, id); }

    public Contact update(Contact c) { return em.merge(c); }

    public void delete(long id) {
        Contact contact = find(id);
        if (contact != null) { em.remove(contact); }
    }

    // Managing relationships

    public Contact removeLeaseCar(Contact c) {
        Contact contact = find(c.getId());
        contact.clearLeaseCar();
        return update(contact);
    }

    public Contact addDepartment(Contact c, Department d) {
        Contact contact = find(c.getId());
        Department department = departmentService.find(d.getId());
        contact.addDepartment(department);
        return update(contact);
    }

    // Queries ==========================

    // JPQL ---------

    public List<Contact> findByParkingSpace(ParkingSpace ps) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.parkingSpace.id = :id", Contact.class);
        query.setParameter("id", ps.getId());
        return query.getResultList();
    }

    public List<Contact> findByParkingSpaceUsingIN(ParkingSpace ps) {
        TypedQuery<Contact> query = em.createQuery(
                "SELECT c FROM ParkingSpace p, " +
                        "   IN (p.contacts) c " +
                        // "   Contact c " +
                        "   WHERE p.id = :id", Contact.class);
        query.setParameter("id", ps.getId());
        return query.getResultList();
    }

    // Native query ---------

   public List<Contact> findByNameNative(String name) {
        Query query = em.createNativeQuery("SELECT * FROM contactperson WHERE C_NAME LIKE ?", Contact.class);
        query.setParameter(1, name + "%");
        return query.getResultList();
    }

    // Criteria API ---------

    public List<Contact> findUsingCriteriaAPI(String name, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> q = cb.createQuery(Contact.class);

        Root<Contact> c = q.from(Contact.class);
        q.select(c)
                .distinct(true)
                .where(cb.and(
                        cb.equal(c.get("name"), name),
                        cb.equal(c.get("email"), email)));
        return em.createQuery(q).getResultList();
    }

    // Using repository --------

    @Autowired
    private ContactRepository repository;

    public List<Contact> findByNameWithRepo(String n) {
        return repository.findByName(n);
    }

    public List<Contact> findByNameOrEmailWithRepo(String n, String e) {
        return repository.findByNameOrEmail(n, e);
    }

    public Laptop getLaptop(Contact contact, int index) {
        Contact merge = em.merge(contact);
        return merge.getLaptops().get(index);
    }
}
