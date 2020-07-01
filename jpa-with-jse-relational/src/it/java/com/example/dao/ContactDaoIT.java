package com.example.dao;

import com.example.App;
import com.example.EntityManagerProducerAlt;
import com.example.domain.Contact;
import com.example.domain.Phone;
import org.hibernate.LazyInitializationException;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void whenEmployeeIsInsertedItGetsAnId() {
        dao.insert(new Contact("A"));
        assertThat(dao.selectAll()).allMatch(e -> e.getId() != 0);
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
        assertThat(byPhone.get(0).getId()).isEqualTo(e.getId());
    }

    private void log(Object o) { log.info(o + ""); }
}
