package com.example;

import com.example.domain.Contact;
import com.example.domain.ContactServiceTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class AppDetachment implements CommandLineRunner {

    @Autowired
    private ContactServiceTransactional service;

    private void test() {
        Contact bram = new Contact("Bram", new Date());
        service.save(bram);

        System.out.println("updating 1...");
        System.out.println(bram);
        Contact arie = service.updateFirstname(1, "arie"); // works because we do a find first
        System.out.println(arie);

        System.out.println("updating 2...");
        System.out.println(arie);
        arie.setFirstname("harry");
        // service.save(arie); // won't work now anymore, because entity is detached already, since transaction is finished
        Contact harryOrArie = service.find(1); // will stay arie, not harry, in contrast to jpawithjee/jse examples
        System.out.println(harryOrArie);
        Contact harry = service.update(arie); // will update arie to harry
        System.out.println(harry);
    }

    public static void main(String[] args) {
        SpringApplication.run(AppDetachment.class, args);
    }

    public void run(String... args) throws Exception { test(); }
}
