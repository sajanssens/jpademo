package com.example.service;

import com.example.domain.Contact;
import org.junit.After;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.Date;

import static com.example.util.EntityManagerFactory.em;
import static org.junit.Assert.assertTrue;

public class ContactServiceDetachedTest {

    private ContactService contactService = new ContactService(em);

    @Test(expected = PersistenceException.class)
    public void testSaveDetachedEntityWithoutCatchAndRollback() {
        Contact bram = new Contact("Bram", new Date());
        contactService.save(bram);

        assertTrue(isDetached(bram));
        bram.setName("Piet");
        contactService.saveWithoutCatchAndRollback(bram); // cannot save a detached entity; exception is thrown
    }

    @After
    public void teardown() {
        // If some tests have open transactions because of exceptions (like in testSaveDetachedEntityWithoutCatchAndRollback)
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    private boolean isDetached(Contact c) { return !em.contains(c); }
}