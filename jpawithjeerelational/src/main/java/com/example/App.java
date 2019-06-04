package com.example;

import com.example.domain.*;
import com.example.service.ContactService;
import com.example.service.LaptopService;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.example.domain.ContactType.Normal;
import static java.util.Collections.singletonList;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired private ContactService contactService;
    @Autowired private LaptopService laptopService;

    private void test() {
        Contact bram = Contact.builder()
                .name("Bram")
                .birthDate(new Date())
                .email("s.a.janssens@gmail.com")
                .hasDriversLicence(true)
                .resume("...very large portion of text....")
                .type(Normal)
                .addressWork(Address.builder().street("Dorpsstraat 1").zip("1234AB").build())
                .emailAddresses(new HashSet<>(Arrays.asList("a@b.com", "b@c.com")))
                .leaseCar(Car.builder().brand("Opel").build())
                .departmentBossOf(Department.builder().name("Kenniscentrum").build())
                .parkingSpace(ParkingSpace.builder().number(100).build())
                .phoneWork(singletonList(Phone.builder().number("0612345678").build()))
                .build();

        contactService.create(bram); // persist new entity
        contactService.removeLeaseCar(bram); // merge one to one UniDi, remove orphan lease car

        laptopService.addNewLaptopToExistingOwner(bram, "DELL"); // fix Bidi passive side

        Contact piet = Contact.builder()
                .name("Piet")
                .birthDate(new Date())
                .build();

        laptopService.changeOwner(1, piet); // cascade merge => insert on Contact

        // demonstrate FETCH types:
        Contact contact = contactService.find(1);
        try {
            Department departmentBossOf = contact.getDepartmentBossOf();
            System.out.println("departmentBossOf loaded");
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException for departmentBossOf");
        }

        laptopService.addNewLaptopToExistingOwner(bram, "SONY");

        try {
            List<Laptop> laptops = contact.getLaptops();
            Laptop laptop = laptops.get(0);
            System.out.println("laptops loaded");
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException for laptops");
        }

        List<Department> departments = contactService.findDepartmentByName("X");
        List<Contact> byNameNative = contactService.findByNameNative("br");
        List<Contact> usingCriteriaAPI = contactService.findUsingCriteriaAPI();
        List<Contact> byName = contactService.findByName("br");
        List<Contact> byNameOrEmail = contactService.findByNameOrEmail("br", "x@y.com");

    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public void run(String... args) throws Exception { test(); }
}
