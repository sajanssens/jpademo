package com.example;

import com.example.domain.*;
import com.example.service.ContactService;
import com.example.service.DepartmentService;
import com.example.service.LaptopService;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

import static com.example.domain.ContactType.Normal;
import static java.util.Collections.singletonList;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired private ContactService contactService;
    @Autowired private LaptopService laptopService;
    @Autowired private DepartmentService departmentService;

    private void test() {
        Department kenniscentrum = Department.builder().name("Kenniscentrum").workers(new ArrayList<>()).build();
        Department finance = Department.builder().name("Finance").build();
        ParkingSpace parkingSpace = ParkingSpace.builder().number(100).build();
        Contact piet = Contact.builder().name("Piet").worksAtDepartments(Arrays.asList(kenniscentrum, finance)).birthDate(new Date()).build();

        Contact bram = Contact.builder()
                .name("Bram")
                .birthDate(new Date())
                .email("s.a.janssens@gmail.com")
                .hasDriversLicence(true)
                .resume("...very large portion of text....")
                .type(Normal)
                .addressWork(Address.builder().street("Dorpsstraat 1").zip("1234 AB").build())
                .emailAddresses(new HashSet<>(Arrays.asList("a@b.com", "b@c.com")))
                .leaseCar(Car.builder().brand("Opel").build())
                .bossOfDepartment(kenniscentrum)
                .worksAtDepartments(Arrays.asList(kenniscentrum, finance))
                .parkingSpace(parkingSpace)
                .phoneWork(singletonList(Phone.builder().number("0612345678").build()))
                .build();

        // Basic CRUD -----------------------------------
        // create entity
        contactService.create(bram);

        // read entity
        Contact c1 = contactService.find(1);

        // updates
        c1.setEmail("bram.janssens@infosupport.com");
        c1.setBossOfDepartment(finance);
        c1.setParkingSpace(ParkingSpace.builder().number(99).build());
        bram = contactService.update(c1);

        // delete
        // contactService.delete(bram.getId());

        // Modify entity's relationships -----------
        contactService.removeLeaseCar(bram); // merge one to one UniDi, remove orphan lease car
        laptopService.addNewLaptopToExistingOwner(bram, "DELL"); // fix Bidi passive side
        laptopService.changeLaptopOwner(1, piet); // cascade merge on Laptop == update Laptop && insert Contact

        contactService.addDepartment(piet, kenniscentrum);

        // Demonstrate FETCH types
        Contact contact = contactService.find(1);
        try {
            Department departmentBossOf = contact.getBossOfDepartment(); // OK: department is eagerly loaded
            System.out.println("departmentBossOf loaded: " + departmentBossOf);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException for departmentBossOf");
        }

        laptopService.addNewLaptopToExistingOwner(bram, "SONY");
        try {
            List<Laptop> laptops = contact.getLaptops();
            Laptop laptop = laptops.get(0);                 // NOK: laptops lazy and not managed here
            System.out.println("laptop(0) loaded: " + laptop);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException 1 for laptops");
        }

        try {
            Laptop laptop = contactService.getLaptop(contact, 0); // OK: laptops are lazy, but were managed and accessed in transactional service, and therefore loaded just in time
            System.out.println("laptop(0) loaded: " + laptop);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException 2 for laptops");
        }

        // Queries -------------

        List<Contact> byDepartmentBoss = contactService.findByParkingSpace(parkingSpace);
        List<Contact> byDepartmentBossUsingIN = contactService.findByParkingSpaceUsingIN(parkingSpace);
        List<Contact> byNameNative = contactService.findByNameNative("br");
        List<Contact> usingCriteriaAPI = contactService.findUsingCriteriaAPI("bram", "s.a.janssens@gmail.com");
        List<Contact> byName = contactService.findByNameWithRepo("br");
        List<Contact> byNameOrEmail = contactService.findByNameOrEmailWithRepo("br", "x@y.com");
        List<Department> departments = departmentService.findDepartmentsByNameWithNamedQuery("Kenniscentrum");
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public void run(String... args) throws Exception { test(); }
}
