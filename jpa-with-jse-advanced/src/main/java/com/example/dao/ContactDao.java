package com.example.dao;

import com.example.domain.Contact;
import com.example.domain.ParkingSpace;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    public ContactDao() {}

    // Basic CRUD -----------

    public void save(Contact p) {
        try {
            em.getTransaction().begin();
            em.persist(p); // in persistence context
            em.getTransaction().commit();
            em.detach(p);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void saveWithoutCatchAndRollback(Contact c) {
        em.getTransaction().begin();
        em.persist(c); // in persistence context
        em.getTransaction().commit();
        em.detach(c);
    }

    public Contact find(long id) {
        log.debug("Finding Contact with id " + id);
        Contact contact = em.find(Contact.class, id);
        if (contact != null) em.detach(contact);
        return contact; // 1
    }

    public List<Contact> findAll() {
        TypedQuery<Contact> query = em.createQuery("select p from Contact p", Contact.class);
        return query.getResultList(); // 2
    }

    public List<Contact> findAllNamed() {
        TypedQuery<Contact> findAll = em.createNamedQuery("findAll", Contact.class);
        return findAll.getResultList();
    }

    public List<Contact> findByName(String name) {
        TypedQuery<Contact> query = em.createQuery("select p from Contact p where p.name = :firstarg", Contact.class);
        query.setParameter("firstarg", name);
        return query.getResultList(); // 3
    }

    public void remove(long id) {
        Contact select = em.find(Contact.class, id);
        if (select != null) {
            em.getTransaction().begin();
            em.remove(select);
            em.getTransaction().commit();
        }
    }

    public Contact update(Contact p) {
        em.getTransaction().begin();
        Contact merged = em.merge(p);
        em.getTransaction().commit();
        return merged;
    }

    public Contact updateFirstname(long id, String name) {
        Contact p = find(id);
        if (p != null) {
            em.getTransaction().begin();
            p.setName(name);
            em.getTransaction().commit();
        }
        return p;
    }

    // Managing relationships

    public Contact removeLeaseCar(Contact c) {
        Contact contact = find(c.getId());
        contact.clearLeaseCar();
        return update(contact);
    }

    // Queries ==========================

    // JPQL ---------

    public Contact findBoss(String depName) {
        TypedQuery<Contact> query = em.createQuery(
                "SELECT c " +
                        "FROM Contact c " +
                        "JOIN c.bossOfDepartment d " +
                        "WHERE d.name = :name",
                Contact.class);
        query.setParameter("name", depName);
        return query.getSingleResult();
    }

    public List<Contact> findByParkingSpace(ParkingSpace ps) {
        TypedQuery<Contact> query = em.createQuery(
                        "SELECT c " +
                                "FROM Contact c " +
                                "WHERE c.parkingSpace.id = :p_id", Contact.class) // no join needed
                .setParameter("p_id", ps.getId());

        return query.getResultList();
    }

    public List<Contact> findByParkingSpaceUsingIN(ParkingSpace ps) {
        TypedQuery<Contact> query = em.createQuery(
                        "SELECT c " +
                                "FROM ParkingSpace p, " +
                                "   IN (p.contacts) c " + // generates a join
                                "WHERE p.id = :p_id", Contact.class)
                .setParameter("p_id", ps.getId());

        return query.getResultList();
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

    public List<Contact> findWithPhones(boolean eager) {
        String fetch = eager ? "FETCH" : "";

        return em.createQuery(
                        "SELECT DISTINCT c " + // distinct is needed here!
                                "FROM Contact c " +
                                "JOIN " + fetch + " c.phones", Contact.class)
                .getResultList();
    }

    public List<Contact> findByLaptopUsingJoin(String laptopBrand) {
        return em.createQuery(
                        "SELECT DISTINCT c " +
                                "FROM Contact c " +
                                "JOIN c.laptops l " + // without fetch, lazyinitexception when laptops are requested.
                                "WHERE l.brand = :brand", Contact.class)
                .setParameter("brand", laptopBrand)
                .getResultList();
    }

    public List<Contact> findByLaptopUsingJoinFetch(String laptopBrand) {
        return em.createQuery(
                        "SELECT DISTINCT c " +
                                "FROM Contact c " +
                                "JOIN fetch c.laptops l " + // with fetch: laptops are eagerly fetched by query.
                                "WHERE l.brand = :brand", Contact.class)
                .setParameter("brand", laptopBrand)
                .getResultList();
    }

    public List<Tuple> findContactNameDepartmentName() {
        return em.createQuery(
                        "SELECT new com.example.dao.Tuple(c.name, d.name) " +
                                "FROM Contact c " +
                                "JOIN c.worksAt d", Tuple.class)
                .getResultList();
    }

    public List<Contact> findTempEmployees() {
        TypedQuery<Contact> query = em.createQuery("select p from Contact p where type(p) = TemporaryEmployee", Contact.class);
        return query.getResultList(); // 2
    }

    // Native query ---------

    public List<Contact> findByNameNative(String name) {
        Query query = em.createNativeQuery("SELECT * FROM Contact WHERE C_NAME LIKE ?", Contact.class);
        query.setParameter(1, name + "%");
        return query.getResultList();
    }

    // Criteria API ---------

    public List<Contact> findUsingCriteriaAPI(String name, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> q = cb.createQuery(Contact.class);

        Root<Contact> contact = q.from(Contact.class);

        q.select(contact).distinct(true)
                .where(cb.or(
                                cb.equal(contact.get("naam"), name),
                                cb.equal(contact.get("emailAddress"), email)
                        )
                );

        return em.createQuery(q).getResultList();
    }

}
