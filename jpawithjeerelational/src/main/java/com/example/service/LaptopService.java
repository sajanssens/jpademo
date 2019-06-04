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

    public Contact changeLaptopOwner(long laptopId, Contact newOwner) {
        Laptop laptop = find(laptopId);
        laptop.setOwner(newOwner);
        return em.merge(laptop).getOwner(); // since laptop.owner has cascade on merge, also new contacts will be inserted and returned here
    }

    public void addNewLaptopToExistingOwner(Contact c, String laptopName) {
        Laptop laptop = Laptop.builder().name(laptopName).build();
        c.addLaptop(laptop);
        em.persist(laptop);
    }

}
