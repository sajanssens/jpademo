package com.example;

import com.example.domain.Contact;
import com.example.domain.ContactDaoDetach;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;

public class AppDetachment {

    // App creates EntityManager
    private static final EntityManager em = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    private static void test() {
        ContactDaoDetach service = new ContactDaoDetach(em);

        Contact bram = new Contact("Bram", new Date());
        service.save(bram);

        System.out.println("updating 1...");
        System.out.println(bram);
        Contact arie = service.updateFirstname(1, "arie"); // works because we do a find first
        System.out.println(arie);

        System.out.println("updating 2...");
        System.out.println(arie);
        arie.setFirstname("harry");

        // save won't work now anymore, because entity is detached
        service.save(arie);
        Contact harryOrArie = service.find(1);
        System.out.println(harryOrArie); // will stay arie

        // merge will update arie to harry
        Contact harry = service.update(arie);
        System.out.println(harry);
    }

    public static void main(String[] args) { test(); }
}
