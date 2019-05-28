package com.example;


import com.example.entities.Contact;
import com.example.entities.ContactService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class App {

    private static EntityManager em() {
        return Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();
    }

    private static void test() {
        ContactService service = new ContactService(em());

        Contact bram = new Contact("Bram", new Date());
        service.save(bram);

        System.out.println("findAll...");
        List<Contact> all = service.findAll();
        for (Contact contact : all) {
            System.out.println(contact);
        }

        System.out.println("find one...");
        bram = service.find(1);
        System.out.println(bram);
        Contact contact2 = service.find(2);
        System.out.println(contact2);

        System.out.println("updating 1...");
        System.out.println(bram);
        Contact arie = service.updateFirstname(1, "arie");
        System.out.println(arie);

        System.out.println("updating 2...");
        System.out.println(arie);
        arie.setFirstname("harry");
        service.save(arie);
        Contact harry = service.find(1);
        System.out.println(harry);

        System.out.println("removing...");
        service.remove(1);
        bram = service.find(1);
        System.out.println(bram);
    }

    public static void main(String[] args) { test(); }
}
