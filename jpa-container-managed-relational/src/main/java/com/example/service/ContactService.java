package com.example.service;

import com.example.domain.*;
import com.example.repository.ContactRepositoryCRUD;
import com.example.repository.ContactRepositoryJPA;
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

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ContactService {

    @PersistenceContext
    private EntityManager em;

    @Autowired private DepartmentService departmentService;
    @Autowired private LaptopService laptopService;

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

    public Contact addLaptop(Contact c, String brand) {
        // BiDi-relationship laptop-contact is managed by the owning side: Laptop.
        // So, delegate this work to laptopservice.
        laptopService.addNewLaptopToOwner(c, brand);
        return em.merge(c);
    }

    public Laptop getLazyLaptop(Contact contact, int index) {
        // transaction starts
        Contact managedContact = em.merge(contact);

        // transaction runs, contact is "live"; lazy laptops can and will be fetched if requested
        List<Laptop> laptops = managedContact.getLaptops();

        // works since laptops are fetched
        return laptops.get(index);
    }

    // Queries ==========================

    // JPQL ---------

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

    public List<Contact> findByLaptopUsingJoin(String laptopBrand) {
        return em.createQuery(
                "SELECT c " +
                        "FROM Contact c " +
                        "JOIN c.laptops l " + // without fetch, lazyinitexception when laptops are requested.
                        "WHERE l.brand = :brand", Contact.class)
                .setParameter("brand", laptopBrand)
                .getResultList();
    }

    public List<Contact> findByLaptopAndFetchThemIteratively(String laptopBrand) {
        List<Contact> contactsWithoutLaptops = findByLaptopUsingJoin(laptopBrand);

        // to fetch each laptop, just query each laptop in the collection.
        contactsWithoutLaptops.stream()
                .flatMap(c -> c.getLaptops().stream().peek(l -> System.out.println("Fetching laptop " + l)).map(AbstractEntity::getId))
                .forEach(i -> {/*do nothing*/});

        return contactsWithoutLaptops;
    }

    public List<Contact> findByLaptopAndFetchThemUsingJoinFetch(String laptopBrand) {
        return em.createQuery(
                "SELECT c " +
                        "FROM Contact c " +
                        "JOIN fetch c.laptops l " + // with fetch: laptops are eagerly fetched by query.
                        "WHERE l.brand = :brand", Contact.class)
                .setParameter("brand", laptopBrand)
                .getResultList();
    }

    public List<Contact> findWithLaptopsUsingJoinFetch() {
        return em.createQuery(
                "SELECT c " +
                        "FROM Contact c " +
                        "JOIN FETCH c.laptops", Contact.class)
                .getResultList();
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

    // Using repositories --------

    @Autowired private ContactRepositoryCRUD repositoryCRUD;

    public List<Contact> findByNameWithRepo(String n) {
        return repositoryCRUD.findByName(n);
    }

    public List<Contact> findByNameOrEmailWithRepo(String n, String e) { return repositoryCRUD.findByNameOrEmail(n, e); }

    public List<Contact> findDriversUsingStream() {
        return repositoryCRUD.findContacts() // stream
                .filter(Contact::getHasDriversLicence) // filter in java instead of in db
                .collect(toList());
    }

    @Autowired private ContactRepositoryJPA repositoryJPA;

    public List<Contact> findByEmailWithJPARepo(String e) {
        return repositoryJPA.findByEmail(e);
    }

}
