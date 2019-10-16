package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Laptop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LaptopServiceTest {

    @Autowired private LaptopService laptopService;

    @Test
    public void whenAddingContactToLaptopsBidirectionalRelationshipIsCorrectlyUpdated() {
        Contact bram = Contact.builder().name("Bram").build();

        Laptop dell = Laptop.builder().brand("DELL").contact(bram).build();
        bram.addLaptop(dell); // do by hand, since laptop doesn't sync the bidi relation, but contact does
        Laptop savedDell = laptopService.save(dell); // bram is saved too
        bram = savedDell.getContact(); // refresh

        Laptop hp = Laptop.builder().brand("HP").contact(bram).build();
        bram.addLaptop(hp); // do by hand, since laptop doesn't sync the bidi relation, but contact does
        Laptop savedHp = laptopService.save(hp);

        assertThat(savedDell.getId(), not(nullValue()));
        assertThat(savedDell.getContact().getName(), is(bram.getName()));
        assertThat(savedHp.getId(), not(nullValue()));
        assertThat(savedHp.getContact().getName(), is(bram.getName()));

        assertThat(bram.getLaptops().size(), is(2));
    }

    @Test
    public void whenAddingContactToLaptopsViaServiceBidirectionalRelationshipIsCorrectlyUpdated() {
        Contact piet = Contact.builder().name("Piet").build();

        Laptop dell = Laptop.builder().brand("DELL").build();
        Laptop savedDell = laptopService.addLaptopToOwner(dell, piet); // piet is saved too
        piet = savedDell.getContact(); // refresh

        Laptop savedHp = laptopService.addNewLaptopToOwner(piet, "HP");
        piet = savedHp.getContact(); // refresh

        assertThat(savedDell.getId(), not(nullValue()));
        assertThat(savedDell.getContact().getId(), is(piet.getId()));
        assertThat(savedHp.getId(), not(nullValue()));
        assertThat(savedHp.getContact().getId(), is(piet.getId()));

        List<Laptop> allLaptopsForContact = laptopService.findAllLaptopsForContact(piet);
        assertThat(allLaptopsForContact.size(), is(2));
    }
}