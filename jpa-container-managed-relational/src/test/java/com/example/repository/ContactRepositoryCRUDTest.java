package com.example.repository;

import com.example.domain.Contact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactRepositoryCRUDTest {

    @Autowired
    private ContactRepositoryCRUD repositoryCRUD;

    @Before
    public void setUp() {
        Contact piet = Contact.builder().name("Piet").birthDate(new Date()).build();
        repositoryCRUD.save(piet);
    }

    @Test
    public void findByName() {
        Contact piet = repositoryCRUD.findByName("Piet").get(0);
        assertThat(piet.getName(), is("Piet"));
    }
}