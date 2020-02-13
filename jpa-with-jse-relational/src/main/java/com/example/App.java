package com.example;

import com.example.domain.Car;
import com.example.domain.Contact;
import com.example.domain.Department;
import com.example.domain.ParkingSpace;
import com.example.service.ContactService;
import com.example.service.DepartmentService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;

public class App {

    private static EntityManager em = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    private ContactService contactService = new ContactService(em);
    private DepartmentService departmentService = new DepartmentService(em);

    private void test() {
        Contact bram = new Contact("Bram", new Date());
        Department kc = new Department("Kenniscentrum");
        ParkingSpace ps = ParkingSpace.builder().number(1).build();
        Car c = Car.builder().brand("Skoda").build();

        bram.setBossOfDepartment(kc);
        bram.setParkingSpace(ps);
        bram.setLeaseCar(c);

        contactService.save(bram);
    }

    public static void main(String[] args) { new App().test(); }
}
