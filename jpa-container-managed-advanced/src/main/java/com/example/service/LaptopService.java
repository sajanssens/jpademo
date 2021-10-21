package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Laptop;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LaptopService {

    @PersistenceContext
    private EntityManager em;

    public Laptop save(Laptop laptop) { return em.merge(laptop); }

    public Laptop find(long id) { return em.find(Laptop.class, id); }

    public List<Laptop> findAllLaptopsForContact(Contact c) {
        return em.createNamedQuery("findAllLaptopsForContact", Laptop.class)
                .setParameter("id", c.getId())
                .getResultList();
    }

    public Laptop addLaptopToOwner(Laptop lap, Contact owner) {
        Contact contact = owner.getId() == 0 ? em.merge(owner) : em.find(Contact.class, owner.getId());
        Laptop laptop = lap.getId() == 0 ? em.merge(lap) : find(lap.getId());
        contact.addLaptop(laptop);
        return em.merge(laptop);// update laptop, and since laptop.owner has cascade, contact will be inserted/updated too
    }

    public Laptop addNewLaptopToOwner(Contact owner, String brand) {
        Laptop newLaptop = em.merge(Laptop.builder().brand(brand).build());
        return addLaptopToOwner(newLaptop, owner);
    }

}
