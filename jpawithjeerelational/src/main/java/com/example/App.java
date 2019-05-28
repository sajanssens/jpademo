package com.example;

import com.example.domain.Address;
import com.example.domain.Car;
import com.example.domain.Contact;
import com.example.domain.Department;
import com.example.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

import static com.example.domain.ContactType.Normal;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private ContactService service;

    private void test() {
        Contact bram = Contact.builder()
                .name("Bram")
                .birthDate(new Date())
                .email("s.a.janssens@gmail.com")
                .hasDriversLicence(true)
                .resume("...very large portion of text....")
                .type(Normal)
                .leaseCar(Car.builder().brand("Opel").build())
                .addressWork(Address.builder().street("Dorpsstraat 1").zip("1234AB").build())
                .departmentBossOf(Department.builder().name("Kenniscentrum").build())
                .build();

        service.save(bram);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    public void run(String... args) throws Exception { test(); }
}
