package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

import static javax.persistence.CascadeType.PERSIST;

@Entity
public class Contact extends AbstractEntity {

    // @Basic is present implicitly on each field
    private String name;
    private Date birthdate;

    @ManyToOne(cascade = PERSIST)
    private Department bossOfDepartment;

    @ManyToOne(cascade = PERSIST)
    private ParkingSpace parkingSpace;

    @OneToOne(cascade = PERSIST, orphanRemoval = true)
    private Car leaseCar;

    public Contact() {}

    public Contact(String firstname, Date birthdate) {
        this.name = firstname;
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }

    public void setName(String name) { this.name = name; }

    public void setBossOfDepartment(Department bossOfDepartment) { this.bossOfDepartment = bossOfDepartment; }

    public void setParkingSpace(ParkingSpace parkingSpaces) { this.parkingSpace = parkingSpaces; }

    public void setLeaseCar(Car leaseCar) { this.leaseCar = leaseCar; }
}
