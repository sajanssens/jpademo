package com.example.service;

import com.example.domain.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.example.util.EntityManagerFactory.em;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ContactServiceTest {

    private ContactService contactService = new ContactService(em);

    @Test
    public void testSave() {
        Contact bram = new Contact("Bram", new Date());
        Department kc = new Department("Kenniscentrum");
        ParkingSpace ps = ParkingSpace.builder().number(1).build();
        Car c = Car.builder().brand("Skoda").build();
        Phone p = Phone.builder().number("06123456789").build();
        Laptop lap = Laptop.builder().brand("DELL").build();

        bram.setBossOfDepartment(kc);
        bram.setParkingSpace(ps);
        bram.setLeaseCar(c);
        bram.addPhone(p);
        bram.addLaptop(lap);
        bram.addWorksAt(kc);
        bram.addWorksAt(new Department("HR"));

        assertThat(bram.getId(), is(0L));
        contactService.save(bram);
        assertThat(bram.getId(), is(not(0L))); // bram got an id

        Contact refreshedBram = contactService.find(bram.getId());
        assertThat(refreshedBram.getId(), is(not(0)));
        assertThat(refreshedBram.getName(), is("Bram"));
        assertThat(refreshedBram.getBossOfDepartment().getName(), is("Kenniscentrum"));
        assertThat(refreshedBram.getParkingSpace().getId(), is(not(0)));
    }

    @Test
    public void testFindAll() {
        Contact bram = new Contact("Bram", new Date());

        List<Contact> beforeInsert = contactService.findAll();
        contactService.save(bram);
        List<Contact> afterInsert = contactService.findAll();

        assertTrue(afterInsert.size() > beforeInsert.size());
    }

    @Test
    public void testSaveFindAndUpdate() {
        Contact refreshedBram;
        Contact bram = new Contact("Bram", new Date());
        contactService.save(bram);

        assertTrue(isDetached(bram));
        bram.setName("Piet");
        refreshedBram = contactService.find(bram.getId());
        assertThat(refreshedBram.getName(), is(not("Piet"))); // change was not saved since bram is detached and not merged yet

        contactService.update(bram);
        refreshedBram = contactService.find(bram.getId());
        assertThat(refreshedBram.getName(), is("Piet")); // change was saved
    }

    @Test
    public void testUpdateFirstname() {
        Contact refreshedBram;
        Contact bram = new Contact("Bram", new Date());

        contactService.save(bram);
        refreshedBram = contactService.find(bram.getId());
        assertThat(refreshedBram.getName(), is("Bram")); // contact was saved

        contactService.updateFirstname(bram, "Piet");
        refreshedBram = contactService.find(bram.getId());
        assertThat(refreshedBram.getName(), is("Piet")); // change was saved
    }

    @Test
    public void testRemove() {
        Contact bram = new Contact("Bram", new Date());
        contactService.save(bram);

        List<Contact> beforeRemove = contactService.findAll();
        contactService.remove(bram);
        List<Contact> afterRemove = contactService.findAll();

        assertTrue(afterRemove.size() < beforeRemove.size());
    }

    @Test
    public void testSaveDetachedEntityWithRollback() {
        Contact bram = new Contact("Bram", new Date());
        contactService.save(bram);

        assertTrue(isDetached(bram));
        bram.setName("Piet");
        contactService.save(bram); // cannot save a detached entity; rollback is applied
        Contact contact = contactService.find(bram.getId());
        assertThat(contact.getName(), allOf(is(not("Piet")), is("Bram"))); // change was not saved
    }

    private boolean isDetached(Contact c) { return !em.contains(c); }
}