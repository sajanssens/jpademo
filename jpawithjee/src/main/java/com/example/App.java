package com.example;

import com.example.entities.Contact;
import com.example.entities.ContactService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class App implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    private void test() {
        ContactService service = new ContactService(em);
        Contact bram = new Contact("Bram", new Date());
        service.save(bram);

        List<Contact> all = service.findAll();
        for (Contact contact : all) {
            System.out.println(contact);
        }

        bram = service.find(1);
        System.out.println(bram);
        Contact contact2 = service.find(2);
        System.out.println(contact2);

        Contact piet = service.updateFirstname(bram.getId(), "piet");
        System.out.println(piet);
        System.out.println(bram);

        System.out.println("updating...");
        System.out.println(piet);
        piet.setFirstname("harry"); // immediately committed
        System.out.println(piet);
        Contact harry = service.find(1);
        System.out.println(harry);

        service.updateFirstname(1, "arie");
        Contact arie = service.find(1);
        System.out.println(arie);

        System.out.println(piet);
        piet.setFirstname("piet");
        service.updateFirstname(piet);
        piet = service.find(1);
        System.out.println(piet);

        System.out.println("removing...");
        service.remove(1);
        bram = service.find(1);
        System.out.println(bram);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Transactional
    public void run(String... args) throws Exception { test(); }
}
