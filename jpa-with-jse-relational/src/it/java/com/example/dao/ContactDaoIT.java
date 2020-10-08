package com.example.dao;

import com.example.App;
import com.example.EntityManagerProducerAlt;
import com.example.domain.*;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hibernate.LazyInitializationException;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.After;
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
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableAutoWeld
@AddPackages(App.class)
@AddBeanClasses(EntityManagerProducerAlt.class)
@EnableAlternatives(EntityManagerProducerAlt.class)
class ContactDaoIT {

    @Inject
    private Logger log;

    @Inject
    private ContactDao dao;

    @After
    public void teardown() {
        // If some tests have open transactions because of exceptions (like in testSaveDetachedEntityWithoutCatchAndRollback)
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Test
    void whenEmployeeIsInsertedItGetsAnId() {
        dao.insert(new Contact("A"));
        Assertions.assertThat(dao.selectAll()).allMatch(e -> e.getId() != 0);
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
        dao.insert(bram);
        assertThat(bram.getId(), is(not(0L))); // bram got an id

        Contact refreshedBram = dao.select(bram.getId());
        assertThat(refreshedBram.getId(), is(not(0)));
        assertThat(refreshedBram.getName(), is("Bram"));
        assertThat(refreshedBram.getBossOfDepartment().getName(), is("Kenniscentrum"));
        assertThat(refreshedBram.getParkingSpace().getId(), is(not(0)));
    }

    @Test
    public void testSaveDetachedEntityWithoutCatchAndRollback() {
        Contact bram = new Contact("Bram", new Date());
        dao.insert(bram);

        assertTrue(isDetached(bram));

        bram.setName("Piet");
        assertThrows(PersistenceException.class, () -> dao.insertWithoutCatchAndRollback(bram)); // cannot save a detached entity; exception is thrown

    }

    @Test
    void whenEmployeesAreQueriedTheirPhonesAreLazilyLoaded() {
        Contact a = new Contact("A");
        a.addPhone(new Phone("1"));
        a.addPhone(new Phone("2"));

        Contact b = new Contact("B");
        b.addPhone(new Phone("3"));
        b.addPhone(new Phone("4"));

        dao.insert(a);
        dao.insert(b);

        for (Contact employee : dao.findEmployees(false)) {
            log(employee);
            employee.getPhones().forEach(p -> log(p.getNumber()));
        }

        for (Contact employee : dao.findEmployees(true)) {
            log(employee);
            employee.getPhones().forEach(p -> log(p.getNumber()));
        }
    }

    @Test
    public void findAllDemo() {
        Contact bram = new Contact("Bram", new Date());

        List<Contact> beforeInsert = dao.selectAll();
        dao.insert(bram);
        List<Contact> afterInsert = dao.selectAll();

        assertTrue(afterInsert.size() > beforeInsert.size());
    }

    @Test
    public void testSaveFindAndUpdate() {
        Contact refreshedBram;
        Contact bram = new Contact("Bram", new Date());
        dao.insert(bram);

        assertTrue(isDetached(bram));
        bram.setName("Piet");
        refreshedBram = dao.select(bram.getId());
        assertThat(refreshedBram.getName(), is(not("Piet"))); // change was not saved since bram is detached and not merged yet

        dao.update(bram);
        refreshedBram = dao.select(bram.getId());
        assertThat(refreshedBram.getName(), is("Piet")); // change was saved
    }

    @Test
    public void testUpdateFirstname() {
        Contact refreshedBram;
        Contact bram = new Contact("Bram", new Date());

        dao.insert(bram);
        assertThat(bram.getName(), is("Bram")); // contact was saved

        refreshedBram = dao.updateFirstname(bram.getId(), "Piet");
        assertThat(refreshedBram.getName(), is("Piet")); // change was saved
    }

    @Test
    public void testRemove() {
        Contact bram = new Contact("Bram", new Date());
        dao.insert(bram);

        List<Contact> beforeRemove = dao.selectAll();
        dao.delete(bram.getId());
        List<Contact> afterRemove = dao.selectAll();

        assertTrue(afterRemove.size() < beforeRemove.size());
    }

    @Test
    void whenEmployeeIsSelectedItsPhonesAreNotLoaded() {
        Contact a = new Contact("A");
        a.addPhone(new Phone("1"));
        a.addPhone(new Phone("2"));
        dao.insert(a);

        Contact e = dao.select(1);

        assertThrows(LazyInitializationException.class, () -> e.getPhones().get(0));
    }

    @Test
    void findByPhone() {
        Phone p1 = new Phone("1");
        Phone p2 = new Phone("2");
        Contact e = new Contact("B");
        e.addPhone(p1);
        e.addPhone(p2);

        dao.insert(e);

        List<Contact> byPhone = dao.findByPhone(p1.getId());
        Assertions.assertThat(byPhone.get(0).getId()).isEqualTo(e.getId());
    }

    private void log(Object o) { log.info(o + ""); }

    private boolean isDetached(Contact c) { return !em.contains(c); }

}
