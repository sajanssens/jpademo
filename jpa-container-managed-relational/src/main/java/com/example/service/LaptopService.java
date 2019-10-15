package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Laptop;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class LaptopService {

    @PersistenceContext
    private EntityManager em;

    public Laptop find(long id) {
        return em.find(Laptop.class, id);
    }

    public Laptop addLaptopToOwner(Laptop lap, Contact owner) {
        Contact contact = owner.getId() == 0 ? em.merge(owner) : em.find(Contact.class, owner.getId());
        Laptop laptop = find(lap.getId());
        laptop.setUser(contact);
        em.merge(laptop);// update laptop, and since laptop.owner has cascade, contact will be inserted/updated and returned here
        return laptop;
    }

    public Laptop addNewLaptopToOwner(Contact owner, String brand) {
        Laptop newLaptop = em.merge(Laptop.builder().brand(brand).build());
        return addLaptopToOwner(newLaptop, owner);
    }

}
