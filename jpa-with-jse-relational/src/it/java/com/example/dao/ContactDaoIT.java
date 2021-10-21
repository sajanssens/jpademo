package com.example.dao;

import com.example.App;
import com.example.EntityManagerProducerAlt;
import com.example.domain.*;
import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

import static com.example.util.EntityManagerFactory.em;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(App.class)
@AddBeanClasses(EntityManagerProducerAlt.class)
@EnableAlternatives(EntityManagerProducerAlt.class)
class ContactDaoIT {

    @Inject
    private Logger log;

    @Inject
    private ContactDao dao;

    @AfterEach
    public void teardown() {
        // If some tests have open transactions because of exceptions (like in testSaveDetachedEntityWithoutCatchAndRollback)
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Test
    void whenContactIsInsertedItGetsAnId() {
        dao.save(new Contact("A"));
        Assertions.assertThat(dao.findAll()).allMatch(e -> e.getId() != 0);
    }

    @Test
    void whenContactIsBossItIsSavedAndQueried() {
        Contact bram = new Contact("Bram", new Date());
        Department kc = new Department("Kenniscentrum");
        bram.setBossOfDepartment(kc);

        dao.save(bram);

        Contact boss = dao.findBoss("Kenniscentrum");
        assertThat(boss.getName(), is("Bram"));
    }

    @Test
    void whenContactsHaveParkingSpaceTheyCanBeFound() {
        ParkingSpace ps = ParkingSpace.builder().number(1).build();

        Contact bram = new Contact("Bram", new Date());
        Contact mieke = new Contact("Mieke", new Date());

        bram.setParkingSpace(ps);
        mieke.setParkingSpace(ps);

        dao.save(bram);
        dao.save(mieke);

        List<Contact> contacts = dao.findByParkingSpace(ps);
        assertThat(contacts.size(), is(2));
    }

    @Test
    public void saveWithDetailsDemo() {
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
        dao.save(bram);
        assertThat(bram.getId(), is(not(0L))); // bram got an id

        Contact refreshedBram = dao.find(bram.getId());
        assertThat(refreshedBram.getId(), is(not(0)));
        assertThat(refreshedBram.getName(), is("Bram"));
        assertThat(refreshedBram.getBossOfDepartment().getName(), is("Kenniscentrum"));
        assertThat(refreshedBram.getParkingSpace().getId(), is(not(0)));

        Contact boss = dao.findBoss("Kenniscentrum");
        assertThat(boss.getName(), is("Bram"));
    }

    @Test
    public void testSaveDetachedEntityWithoutCatchAndRollback() {
        Contact bram = new Contact("Bram", new Date());
        dao.save(bram);

        assertTrue(isDetached(bram));

        bram.setName("Piet");
        assertThrows(PersistenceException.class, () -> dao.saveWithoutCatchAndRollback(bram)); // cannot save a detached entity; exception is thrown

    }

    @Test
    public void whenContactWithInvalidNameIsInsertedItIsRefused() {
        Contact bramTooLong = new Contact("Bram bram bram Bram bram bram Bram bram bram Bram bram bram Bram bram bram Bram bram bram Bram bram bram Bram bram bram Bram bram bram Bram bram bram ", new Date());
        assertThrows(RuntimeException.class, () -> dao.save(bramTooLong));
    }

    @Test
    public void whenContactWithInvalidEmailIsInsertedItIsRefused() {
        Contact bramInvalidEmail = new Contact("Bram", new Date());
        bramInvalidEmail.setEmailAddress("bram_at_test.com");
        assertThrows(RuntimeException.class, () -> dao.save(bramInvalidEmail));
    }

    @Test
    void whenContactsAreQueriedTheirPhonesAreLazilyLoaded() {
        Contact a = new Contact("A");
        a.addPhone(new Phone("1"));
        a.addPhone(new Phone("2"));

        Contact b = new Contact("B");
        b.addPhone(new Phone("3"));
        b.addPhone(new Phone("4"));

        dao.save(a);
        dao.save(b);

        for (Contact contact : dao.findWithPhones(false)) {
            log(contact);
            contact.getPhones().forEach(p -> log(p.getNumber()));
        }

        for (Contact contact : dao.findWithPhones(true)) {
            log(contact);
            contact.getPhones().forEach(p -> log(p.getNumber()));
        }
    }

    @Test
    public void findAllDemo() {
        Contact bram = new Contact("Bram", new Date());

        List<Contact> beforeInsert = dao.findAll();
        dao.save(bram);
        List<Contact> afterInsert = dao.findAll();

        assertTrue(afterInsert.size() > beforeInsert.size());
    }

    @Test
    public void testSaveFindAndUpdate() {
        Contact refreshedBram;
        Contact bram = new Contact("Bram", new Date());
        dao.save(bram);

        assertTrue(isDetached(bram));
        bram.setName("Piet");
        refreshedBram = dao.find(bram.getId());
        assertThat(refreshedBram.getName(), is(not("Piet"))); // change was not saved since bram is detached and not merged yet

        dao.update(bram);
        refreshedBram = dao.find(bram.getId());
        assertThat(refreshedBram.getName(), is("Piet")); // change was saved
    }

    @Test
    public void testUpdateFirstname() {
        Contact refreshedBram;
        Contact bram = new Contact("Bram", new Date());

        dao.save(bram);
        assertThat(bram.getName(), is("Bram")); // contact was saved

        refreshedBram = dao.updateFirstname(bram.getId(), "Piet");
        assertThat(refreshedBram.getName(), is("Piet")); // change was saved
    }

    @Test
    public void testRemove() {
        Contact bram = new Contact("Bram", new Date());
        dao.save(bram);

        List<Contact> beforeRemove = dao.findAll();
        dao.remove(bram.getId());
        List<Contact> afterRemove = dao.findAll();

        assertTrue(afterRemove.size() < beforeRemove.size());
    }

    @Test
    @Disabled
        // IMPORTANT!! Only works when project is built with maven!
        // Because hibernate-enhance-maven-plugin is needed to let basic fields load lazily!
    void whenContactIsSelectedResumeIsLazilyLoaded() {

        // given a new and saved Contact with resume
        Contact e = new Contact("emp");
        e.setResume("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent non tempus enim. Duis eget sapien enim. Morbi elementum dictum tempus. Sed posuere tortor mauris, quis vehicula tellus congue non.");
        dao.save(e);

        // when we get it from the db and it is detached
        Contact detachedEmp = dao.find(e.getId());
        // then resume is not loaded and cannot be loaded anymore
        assertTrue(isDetached(detachedEmp));
        assertThrows(LazyInitializationException.class, detachedEmp::getResume);

        // but
        // when we keep it managed
        Contact managedEmp = dao.find(e.getId());
        // then the resume can be loaded
        assertFalse(isDetached(managedEmp));
        String resume = managedEmp.getResume(); // get resume from managed Contact
        Assertions.assertThat(resume).isNotBlank(); // this should succeed
    }

    @Test
    @Disabled
    void whenContactIsSelectedItsPhonesAreNotLoaded() {
        Contact a = new Contact("A");
        a.addPhone(new Phone("1"));
        a.addPhone(new Phone("2"));
        dao.save(a);

        Contact e = dao.find(1);

        assertThrows(LazyInitializationException.class, () -> e.getPhones().get(0));
    }

    @Test
    void findByPhone() {
        Contact e = new Contact("B");
        Phone p1 = new Phone("1");
        Phone p2 = new Phone("2");
        e.addPhone(p1);
        e.addPhone(p2);

        dao.save(e);

        List<Contact> byPhone = dao.findByPhone(p1.getId());
        Assertions.assertThat(byPhone.get(0).getId()).isEqualTo(e.getId());
    }

    private void log(Object o) {log.info(o + "");}

    private boolean isDetached(Contact c) {return !em.contains(c);}

}
