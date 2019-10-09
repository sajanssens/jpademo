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

    public Contact changeLaptopOwner(Laptop lap, Contact newOwner) {
        Laptop laptop = find(lap.getId());
        laptop.setOwner(newOwner);
        return em.merge(laptop).getOwner(); // update laptop, and since laptop.owner has cascade on merge, new contact will be inserted and returned here too
    }

    public Laptop addNewLaptopToExistingOwner(Contact c, String laptopName) {
        Contact merge = em.merge(c);
        Laptop laptop = Laptop.builder().name(laptopName).build();
        merge.addLaptop(laptop);
        em.merge(merge);
        em.persist(laptop); // create laptop, and since laptop has cascade, new laptop will be inserted and returned here too
        return laptop;
    }

}