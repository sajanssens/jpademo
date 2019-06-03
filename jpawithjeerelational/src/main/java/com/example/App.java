package com.example;

import com.example.domain.*;
import com.example.service.CarService;
import com.example.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

import static com.example.domain.ContactType.Normal;
import static java.util.Collections.singletonList;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired private ContactService contactService;
    @Autowired private CarService carService;

    private void test() {
        Contact bram = Contact.builder()
                .name("Bram")
                .birthDate(new Date())
                .email("s.a.janssens@gmail.com")
                .hasDriversLicence(true)
                .resume("...very large portion of text....")
                .type(Normal)
                .addressWork(Address.builder().street("Dorpsstraat 1").zip("1234AB").build())
                .leaseCar(Car.builder().brand("Opel").build())
                .departmentBossOf(Department.builder().name("Kenniscentrum").build())
                .parkingSpace(ParkingSpace.builder().number(100).build())
                .phoneWork(singletonList(Phone.builder().number("0612345678").build()))
                .build();

        contactService.create(bram);
        contactService.removeLeaseCar(bram);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public void run(String... args) throws Exception { test(); }
}
