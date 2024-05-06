package com.example;

import com.example.domain.Contact;
import com.example.domain.ContactDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

// To start mysql, see README in parent.
// Next, create the database 'jpa-with-jse' on mysql, then run this example.

@Slf4j
public class App {

    // App creates EntityManager
    private static final EntityManager em = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    private static void test() {
        ContactDao dao = new ContactDao(em);

        log.debug("save...");
        Contact bram = new Contact("Bram", new Date());
        dao.save(bram);

        log.debug("find...");
        Contact contact1 = dao.find(1);
        log.debug("contact1=" + contact1);
        Contact contact2 = dao.find(2);
        log.debug("contact2=" + contact2);

        log.debug("findAll...");
        List<Contact> all = dao.findAll();
        for (Contact contact : all) {
            log.debug(contact + "");
        }

        log.debug("updating by id...");
        log.debug("before update: " + bram);
        Contact arie = dao.updateFirstname(contact1.getId(), "arie");
        log.debug("after update: " + arie);

        log.debug("updating full object...");
        log.debug("before update: " + arie);
        arie.setFirstname("harry");

        // dao.save(arie);
        // Contact harry = dao.find(1);
        //   or:
        Contact harry = dao.update(arie);

        log.debug("after update: " + harry);

        log.debug("removing...");
        dao.remove(1);
        bram = dao.find(1);
        log.debug(bram + "");
    }

    public static void main(String[] args) { test(); }
}
