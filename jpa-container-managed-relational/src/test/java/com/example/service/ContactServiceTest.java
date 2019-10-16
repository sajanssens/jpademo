package com.example.service;

import com.example.domain.Contact;
import com.example.domain.Laptop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ContactServiceTest {

    @Mock private DepartmentService departmentService;
    @Autowired private LaptopService laptopService;

    @InjectMocks
    @Autowired private ContactService contactService;

    @Test
    public void createAndFind() {
        Contact bram = Contact.builder().name("Bram").build();

        contactService.create(bram);

        Contact c1 = contactService.find(bram.getId());

        assertThat(c1.getId(), not(nullValue()));
        assertThat(c1.getName(), is(bram.getName()));
    }

    @Test
    public void updateCreatesNewContact() {
        Contact c1 = Contact.builder().name("Bram").build();

        Contact contact = contactService.update(c1);

        assertThat(contact.getId(), not(nullValue()));
    }

    @Test
    public void persistAndFindGenerateNewContact() {
        Contact c1 = Contact.builder().name("Bram").build();

        Long persist = contactService.persistAndFlush(c1);
        Contact contact1 = contactService.find(persist);

        assertThat(contact1.getId(), not(nullValue()));
    }

    @Test
    public void whenAddingLaptopsToContactBidirectionalRelationshipIsCorrectlyUpdated() {
        Contact newContact = Contact.builder().name("Bram").build();
        assertThat(newContact.getLaptops().size(), is(0));

        newContact.addLaptop(Laptop.builder().brand("DELL").build());
        newContact.addLaptop(Laptop.builder().brand("HP").build());
        assertThat(newContact.getLaptops().size(), is(2));

        Contact foundContactWithLaptops = contactService.update(newContact);

        assertThat(foundContactWithLaptops.getLaptops().size(), is(2));

        for (Laptop laptop : foundContactWithLaptops.getLaptops()) {
            assertThat(laptop.getContact(), is(foundContactWithLaptops));
        }
    }
}