package com.example;

import com.example.domain.Contact;
import com.example.domain.ContactService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class App {

    // App creates EntityManager
    private static EntityManager em = Persistence.createEntityManagerFactory("ContactServiceH2").createEntityManager();

    private static void test() {
        ContactService service = new ContactService(em);

        Contact bram = new Contact("Bram", new Date());
        service.save(bram);

        System.out.println("findAll...");
        List<Contact> all = service.findAll();
        for (Contact contact : all) {
            System.out.println(contact);
        }

        System.out.println("find...");
        Contact contact1 = service.find(1);
        System.out.println("contact1=" + contact1);
        Contact contact2 = service.find(2);
        System.out.println("contact2=" + contact2);

        System.out.println("updating by id...");
        System.out.println("before update: " + bram);
        Contact arie = service.updateFirstname(contact1.getId(), "arie");
        System.out.println("after update: " + arie);

        System.out.println("updating complete object...");
        System.out.println("before update: " + arie);
        arie.setFirstname("harry");

        // service.save(arie);
        // Contact harry = service.find(1);
        //   or:
        Contact harry = service.update(arie);

        System.out.println("after update: " + harry);

        System.out.println("removing...");
        service.remove(1);
        bram = service.find(1);
        System.out.println(bram);
    }

    public static void main(String[] args) { test(); }
}
