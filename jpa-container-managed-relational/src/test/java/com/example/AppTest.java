package com.example;

import com.example.domain.*;
import com.example.service.ContactService;
import com.example.service.DepartmentService;
import com.example.service.LaptopService;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.example.domain.ContactType.Normal;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Autowired private ContactService contactService;
    @Autowired private LaptopService laptopService;
    @Autowired private DepartmentService departmentService;

    @Test
    public void test() {
        Department kenniscentrum = Department.builder().name("Kenniscentrum").build();
        Department finance = Department.builder().name("Finance").build();
        ParkingSpace parkingSpace = ParkingSpace.builder().number(100).build();
        Contact piet = Contact.builder().name("Piet").birthDate(new Date()).build();

        Contact bram = Contact.builder()
                .name("Bram")
                .birthDate(new Date())
                .email("s.a.janssens@gmail.com")
                .hasDriversLicence(true)
                .resume("...very large portion of text....")
                .type(Normal)
                .resume("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .addressWork(Address.builder().street("Dorpsstraat 1").zip("1234 AB").build())
                .emailAddresses(Set.of("a@b.com", "b@c.com"))
                .bossOfDepartment(kenniscentrum)
                .parkingSpace(parkingSpace)
                .leaseCar(Car.builder().brand("Opel").build())
                .phones(singletonList(Phone.builder().number("0612345678").build()))
                .worksAtDepartment(kenniscentrum)
                .worksAtDepartment(finance)
                .build();

        // Basic CRUD -----------------------------------
        // create entity
        contactService.create(bram);

        // read entity
        Contact c1 = contactService.find(bram.getId());
        System.out.println("================================");
        System.out.println(c1);

        // updates
        c1.setEmail("bram.janssens@infosupport.com");
        c1.setBossOfDepartment(finance);
        c1.setParkingSpace(ParkingSpace.builder().number(99).build());
        bram = contactService.update(c1);

        // delete
        // contactService.delete(bram.getId());

        // Modify entity's relationships -----------
        bram = contactService.removeLeaseCar(bram); // merge one to one UniDi, remove orphan lease car
        Laptop dell = laptopService.addNewLaptopToOwner(bram, "DELL");// fix Bidi passive side
        dell = laptopService.addLaptopToOwner(dell, piet);// cascade merge on Laptop == update Laptop && insert Contact piet; piet is returned, so we can get its id
        piet = dell.getContact(); // don't forget to refresh piet, since it has an id now
        contactService.addDepartment(piet, kenniscentrum); // update manytomany
        contactService.addLaptop(piet, "HP");

        // Demonstrate FETCH types
        Contact contactWithId1 = contactService.find(1);
        try {
            Department departmentBossOf = contactWithId1.getBossOfDepartment(); // OK: department is eagerly loaded
            System.out.println("departmentBossOf loaded: " + departmentBossOf);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException for departmentBossOf");
        }

        laptopService.addNewLaptopToOwner(contactWithId1, "SONY");
        try {
            List<Laptop> laptops = contactWithId1.getLaptops();
            Laptop laptop = laptops.get(0);                 // NOK: laptops lazy and not managed here
            System.out.println("laptop(0) loaded: " + laptop);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException 1 for laptops");
        }

        try {
            Laptop laptop = contactService.getLazyLaptop(contactWithId1, 0); // OK: laptops are lazy, but were managed and accessed in transactional service, and therefore loaded just in time
            System.out.println("laptop(0) loaded: " + laptop);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException 2 for laptops");
        }

        laptopService.addLaptopToOwner(dell, contactWithId1);

        // Inheritance

        ContactVip vip = new ContactVip(50);
        vip.setName("Vlip");
        contactService.create(vip);

        ContactSpecial special = new ContactSpecial("Smart guy.");
        special.setName("John");
        contactService.create(special);

        // Queries -------------
        System.out.println("Queries:");

        System.out.println("findByParkingSpace");
        contactService.findByParkingSpace(parkingSpace).forEach(System.out::println);

        System.out.println("findByParkingSpaceUsingIN");
        contactService.findByParkingSpaceUsingIN(parkingSpace).forEach(System.out::println);

        System.out.println("findByLaptopAndFetchThemIteratively");
        contactService.findByLaptopAndFetchThemIteratively("Dell").forEach(c -> System.out.println(c + "has laptops: " + c.getLaptops().stream().map(Laptop::toString).collect(joining(", "))));

        System.out.println("findByLaptopAndFetchThemUsingJoinFetch");
        contactService.findByLaptopAndFetchThemUsingJoinFetch("Dell").forEach(c -> System.out.println(c + "has laptops: " + c.getLaptops().stream().map(Laptop::toString).collect(joining(", "))));

        System.out.println("findWithLaptopsUsingJoinFetch");
        contactService.findWithLaptopsUsingJoinFetch().forEach(System.out::println);

        System.out.println("findByNameNative");
        contactService.findByNameNative("br").forEach(System.out::println);

        System.out.println("findUsingCriteriaAPI");
        contactService.findUsingCriteriaAPI("bram", "s.a.janssens@gmail.com").forEach(System.out::println);

        System.out.println("findByNameWithRepo");
        contactService.findByNameWithRepo("br").forEach(System.out::println);

        System.out.println("findByEmailWithJPARepo");
        contactService.findByEmailWithJPARepo("gmail").forEach(System.out::println);

        System.out.println("findByNameOrEmailWithRepo");
        contactService.findByNameOrEmailWithRepo("br", "x@y.com").forEach(System.out::println);

        System.out.println("findDriversUsingStream");
        contactService.findDriversUsingStream().forEach(System.out::println);

        System.out.println("findDepartmentsByNameWithNamedQuery");
        departmentService.findDepartmentsByNameWithNamedQuery("Kenniscentrum").forEach(System.out::println);
    }

    private String getLaptopsString(Contact c) {
        try {
            return c.getLaptops().stream().map(Laptop::toString).collect(joining(", "));
        } catch (Throwable t) {
            t.printStackTrace();
            return "No laptops found: " + t.getMessage();
        }
    }

}