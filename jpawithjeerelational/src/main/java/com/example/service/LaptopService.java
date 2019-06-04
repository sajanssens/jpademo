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

    public void changeOwner(long laptopId, Contact newOwner) {
        Laptop laptop = find(laptopId);
        laptop.setOwner(newOwner);
        em.merge(laptop); // since laptop.owner has cascade on merge, also new contacts will be inserted
    }

    public void addNewLaptopToExistingOwner(Contact c, String laptopName) {
        Laptop laptop = Laptop.builder().name(laptopName).build();
        c.addLaptop(laptop);
        em.persist(laptop);
    }

}
