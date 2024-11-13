package com.example;

import com.example.dao.ContactDao;
import com.example.domain.Contact;
import com.example.domain.Department;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;

import java.util.Date;

@Singleton
public class Boot {

    @Inject Logger logger;
    @Inject ContactDao dao;

    public void runDemo() {
        Contact bram = new Contact("Bram", new Date());
        Department kc = new Department("Kenniscentrum");
        bram.setBossOfDepartment(kc);

        dao.save(bram);

        Contact boss = dao.findBoss("Kenniscentrum");
        logger.info("The boss of {} is {}.", kc.getName(), boss.getName());

        // see ContactDaoIT for more code
    }
}
