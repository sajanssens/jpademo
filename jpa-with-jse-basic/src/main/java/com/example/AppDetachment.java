package com.example;

import com.example.domain.Contact;
import com.example.domain.ContactDaoDetachAndRollback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.Date;

public class AppDetachment {

    // App creates EntityManager
    private static final EntityManager em = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    private static void test() {
        ContactDaoDetachAndRollback dao = new ContactDaoDetachAndRollback(em);

        Contact bram = new Contact("Bram", new Date());
        dao.save(bram);

        System.out.println("updating 1...");
        System.out.println(bram);
        Contact arie = dao.updateFirstname(1, "arie"); // works because we do a find first
        System.out.println(arie);

        System.out.println("updating 2...");
        System.out.println(arie);
        arie.setFirstname("harry");
        // persist won't work now anymore, because entity is detached
        dao.save(arie);
        Contact harryOrArie = dao.find(1);
        System.out.println(harryOrArie); // will stay arie
        // merge will update arie to harry
        Contact harry = dao.update(arie);
        System.out.println(harry);
    }

    public static void main(String[] args) { test(); }
}
