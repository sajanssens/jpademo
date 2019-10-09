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
                .resume("jkd fhskdj fhsdkjh fsdjkfh sdkjhf sdkjfh kjsd fj")
                .addressWork(Address.builder().street("Dorpsstraat 1").zip("1234 AB").build())
                .emailAddresses(Set.of("a@b.com", "b@c.com"))
                .bossOfDepartment(kenniscentrum)
                .parkingSpace(parkingSpace)
                .leaseCar(Car.builder().brand("Opel").build())
                .phoneWork(singletonList(Phone.builder().number("0612345678").build()))
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
        Laptop dell = laptopService.addNewLaptopToExistingOwner(bram, "DELL");// fix Bidi passive side
        piet = laptopService.changeLaptopOwner(dell, piet);// cascade merge on Laptop == update Laptop && insert Contact piet; piet is returned, so we can get its id
        contactService.addDepartment(piet, kenniscentrum); // update manytomany

        // Demonstrate FETCH types
        Contact contact = contactService.find(1);
        try {
            Department departmentBossOf = contact.getBossOfDepartment(); // OK: department is eagerly loaded
            System.out.println("departmentBossOf loaded: " + departmentBossOf);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException for departmentBossOf");
        }

        laptopService.addNewLaptopToExistingOwner(contact, "SONY");
        try {
            List<Laptop> laptops = contact.getLaptops();
            Laptop laptop = laptops.get(0);                 // NOK: laptops lazy and not managed here
            System.out.println("laptop(0) loaded: " + laptop);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException 1 for laptops");
        }

        try {
            Laptop laptop = contactService.getLazyLaptop(contact, 0); // OK: laptops are lazy, but were managed and accessed in transactional service, and therefore loaded just in time
            System.out.println("laptop(0) loaded: " + laptop);
        } catch (LazyInitializationException e) {
            System.out.println("LazyInitializationException 2 for laptops");
        }
        // Inheritance

        VipContact vipContact = new VipContact(50);
        vipContact.setName("Vlip");
        contactService.create(vipContact);

        // Queries -------------

        System.out.println("Queries:");
        contactService.findByParkingSpace(parkingSpace).forEach(System.out::println);
        contactService.findByParkingSpaceUsingIN(parkingSpace).forEach(System.out::println);
        contactService.findByNameNative("br").forEach(System.out::println);
        contactService.findUsingCriteriaAPI("bram", "s.a.janssens@gmail.com").forEach(System.out::println);
        contactService.findByNameWithRepo("br").forEach(System.out::println);
        contactService.findByEmailWithJPARepo("gmail").forEach(System.out::println);
        contactService.findByNameOrEmailWithRepo("br", "x@y.com").forEach(System.out::println);
        contactService.findDrivers().forEach(System.out::println);
        departmentService.findDepartmentsByNameWithNamedQuery("Kenniscentrum").forEach(System.out::println);
    }

}