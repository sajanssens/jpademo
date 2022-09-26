package com.example.dao;

import com.example.domain.Contact;
import com.example.domain.ContactDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.util.Date;

class ContactDaoIT {

    private final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();

    private final ContactDao dao = new ContactDao(em);

    @AfterEach
    public void teardown() {
        // If some tests have open transactions because of exceptions
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Test
    void whenEmployeeIsInsertedItGetsAnId() {
        dao.save(new Contact("A", new Date()));
        Assertions.assertThat(dao.findAll()).allMatch(e -> e.getId() != 0);
    }

}
