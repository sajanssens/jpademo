package com.example;

import com.example.domain.Contact;
import com.example.domain.ContactDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.Date;
import java.util.List;

// To start mysql, see README in parent.
// Next create the database 'jpa-with-jse' on mysql to run this example.

public class App {

    // App creates EntityManager
    private static final EntityManager em = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    private static void test() {
        ContactDao dao = new ContactDao(em);

        System.out.println("save...");
        Contact bram = new Contact("Bram", new Date());
        dao.save(bram);

        System.out.println("find...");
        Contact contact1 = dao.find(1);
        System.out.println("contact1=" + contact1);
        Contact contact2 = dao.find(2);
        System.out.println("contact2=" + contact2);

        System.out.println("findAll...");
        List<Contact> all = dao.findAll();
        for (Contact contact : all) {
            System.out.println(contact);
        }

        System.out.println("updating by id...");
        System.out.println("before update: " + bram);
        Contact arie = dao.updateFirstname(contact1.getId(), "arie");
        System.out.println("after update: " + arie);

        System.out.println("updating full object...");
        System.out.println("before update: " + arie);
        arie.setFirstname("harry");

        // dao.save(arie);
        // Contact harry = dao.find(1);
        //   or:
        Contact harry = dao.update(arie);

        System.out.println("after update: " + harry);

        System.out.println("removing...");
        dao.remove(1);
        bram = dao.find(1);
        System.out.println(bram);
    }

    public static void main(String[] args) { test(); }
}
