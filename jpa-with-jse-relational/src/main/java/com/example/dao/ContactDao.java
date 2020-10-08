package com.example.dao;

import com.example.domain.Contact;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

// Dao which uses DI through CDI. Corresponding ..IT has to use Weld therefore.
public class ContactDao {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public ContactDao() { }

    public void insert(Contact p) {
        try {
            em.getTransaction().begin();
            em.persist(p); // in persistence context
            em.getTransaction().commit();
            em.detach(p);
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void insertWithoutCatchAndRollback(Contact c) {
        em.getTransaction().begin();
        em.persist(c); // in persistence context
        em.getTransaction().commit();
        em.detach(c);
    }

    public Contact select(long id) {
        log.debug("Finding Contact with id " + id);
        Contact contact = em.find(Contact.class, id);
        if (contact != null) em.detach(contact);
        return contact; // 1
    }

    public List<Contact> selectAll() {
        TypedQuery<Contact> query = em.createQuery("select p from Contact p", Contact.class);
        return query.getResultList(); // 2
    }

    public List<Contact> selectAll(String name) {
        TypedQuery<Contact> query = em.createQuery("select p from Contact p where p.name = :firstarg", Contact.class);
        query.setParameter("firstarg", name);
        return query.getResultList(); // 3
    }

    public List<Contact> selectTempEmployees() {
        TypedQuery<Contact> query = em.createQuery("select p from Contact p where type(p) = TemporaryEmployee", Contact.class);
        return query.getResultList(); // 2
    }

    public List<Contact> findByPhone(long phoneId) {
        TypedQuery<Contact> query = em.createQuery(
                "SELECT p " +
                        "FROM Contact p " +
                        "JOIN p.phones ps " +
                        "WHERE ps.id = :phoneId",
                Contact.class);
        query.setParameter("phoneId", phoneId);
        return query.getResultList(); // findBy on OneToMany (with join)
    }

    public List<Contact> selectAllNamed() {
        TypedQuery<Contact> findAll = em.createNamedQuery("findAll", Contact.class);
        return findAll.getResultList();
    }

    public void delete(long id) {
        Contact select = em.find(Contact.class, id);
        if (select != null) {
            em.getTransaction().begin();
            em.remove(select);
            em.getTransaction().commit();
        }
    }

    public Contact updateFirstname(long id, String name) {
        Contact p = select(id);
        if (p != null) {
            em.getTransaction().begin();
            p.setName(name);
            em.getTransaction().commit();
        }
        return p;
    }

    public Contact update(Contact p) {
        em.getTransaction().begin();
        Contact merged = em.merge(p);
        em.getTransaction().commit();
        return merged;
    }

    public List<Contact> findEmployees(boolean eager) {
        String fetch = eager ? "FETCH" : "";

        return em.createQuery(
                "SELECT DISTINCT emp " +
                        "FROM Contact emp " +
                        "JOIN " + fetch + " emp.phones", Contact.class)
                .getResultList();
    }

    public List<Tuple> findEmployeeDepartments() {
        return em.createQuery(
                "SELECT new com.example.dao.Tuple(emp.name, dep.name) " +
                        "FROM Contact emp " +
                        "JOIN emp.worksAt dep", Tuple.class)
                .getResultList();
    }

    // Criteria API ---------

    public List<Contact> findUsingCriteriaAPI(String name, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> q = cb.createQuery(Contact.class);

        Root<Contact> emp = q.from(Contact.class);

        q.select(emp).distinct(true)
                .where(cb.or(
                        cb.equal(emp.get("naam"), name),
                        cb.equal(emp.get("emailAddress"), email)
                        )
                );

        return em.createQuery(q).getResultList();
    }

}
