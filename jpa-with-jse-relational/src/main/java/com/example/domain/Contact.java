package com.example.domain;

import com.example.util.BooleanTFConverter;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.PERSIST;

@Entity
@NamedQuery(name = "findAll", query = "select c from Contact c")
public class Contact extends AbstractEntity {

    // @Basic is present implicitly on each field
    private String name;
    private Date birthday;

    // Special fields -----------------------------------------------

    @Convert(converter = BooleanTFConverter.class)
    private Boolean hasDriversLicence;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    // --- Single Valued relationships  (@...ToOne) -----------------

    // UniDi
    @ManyToOne(cascade = PERSIST)
    private Department bossOfDepartment;

    // BiDi, owning side
    @ManyToOne(cascade = PERSIST)
    private ParkingSpace parkingSpace;

    // UniDi
    @OneToOne(cascade = PERSIST, orphanRemoval = true)
    private Car leaseCar;

    // --- Collection Valued relationships (@...ToMany) -------------

    // UniDi
    @OneToMany(cascade = {PERSIST})
    private List<Phone> phones = new ArrayList<>();

    // BiDi, passive side
    @OneToMany(cascade = PERSIST, mappedBy = "contact")
    private List<Laptop> laptops = new ArrayList<>();

    // BiDi, passive config side (passive, since FK is in join table)
    @ManyToMany(cascade = PERSIST, mappedBy = "employees") // either use mappedBy or use JoinTable on either manytomany side (of your choice); it doesn't matter which side
    private Set<Department> worksAt = new HashSet<>();

    public Contact() {}

    public Contact(String firstname) {
        this.name = firstname;
        this.birthday = new Date();
    }

    public Contact(String firstname, Date birthday) {
        this.name = firstname;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", hasDriversLicence=" + hasDriversLicence +
                ", type=" + type +
                '}';
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Department getBossOfDepartment() { return bossOfDepartment; }

    public void setBossOfDepartment(Department bossOfDepartment) { this.bossOfDepartment = bossOfDepartment; }

    public ParkingSpace getParkingSpace() { return parkingSpace; }

    public void setParkingSpace(ParkingSpace parkingSpaces) { this.parkingSpace = parkingSpaces; }

    public void setLeaseCar(Car leaseCar) { this.leaseCar = leaseCar; }

    public void addPhone(Phone p) { this.phones.add(p);}

    public List<Phone> getPhones() {
        return phones;
    }

    public void addLaptop(Laptop lap) {
        this.laptops.add(lap);
        lap.setContact(this); // fix the other side of the BiDi-relationship; choose whether to do it on this side or on the other side
    }

    public void addWorksAt(Department d) {
        this.worksAt.add(d);
        d.addEmployee(this);
    }

}

