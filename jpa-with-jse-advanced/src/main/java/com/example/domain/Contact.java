package com.example.domain;

import com.example.util.BooleanTFConverter;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.TemporalType.DATE;

@Entity
@NamedQuery(name = "Contact.findAll", query = "select c from Contact c")
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE) // this is the default
public class Contact extends AbstractEntity {

    // @Basic is present implicitly on each field
    @Column(name = "C_NAME")
    @Size(max = 100, message = "Naam is te lang!")
    private String name;

    @Column(unique = true)
    // Bean validation API:
    @Email
    // Alternatively, use: @Pattern(regexp = "....")
    private String emailAddress;

    // Special fields -----------------------------------------------

    @Temporal(value = DATE)
    private Date birthday = new Date();

    // Better: use LocalDate or LocalDateTime
    // Will be converted (automatically) to time by LocalDateTimeAttributeConverter
    // @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime timeOfBirth = LocalDateTime.of(1979, 8, 22, 6, 15, 0, 0); // 22-08-1979 06h:15m:00s:00ms

    @Convert(converter = BooleanTFConverter.class)
    private Boolean hasDriversLicence;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Embedded
    private Address addressWork;

    @Lob // CLOB Character large object
    @Basic(fetch = LAZY) // only loaded when explicitly called (with getResume()) on a managed object.
    private String resume;

    @Lob // BLOB Binary large object
    @Basic(fetch = LAZY)
    private byte[] image;

    // --- Single Valued relationships  (@...ToOne) -----------------

    // UniDi
    @ManyToOne(cascade = PERSIST)
    private Department bossOfDepartment;

    // BiDi, owning side
    @ManyToOne(cascade = PERSIST)
    private Team team;

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

    public Contact() { }

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

    public void setEmailAddress(String email) { this.emailAddress = email; }

    public String getResume() { return resume; }

    public Department getBossOfDepartment() { return bossOfDepartment; }

    public void setBossOfDepartment(Department bossOfDepartment) { this.bossOfDepartment = bossOfDepartment; }

    public Team getTeam() { return team; }

    public void setTeam(Team team) { this.team = team; }

    public void setLeaseCar(Car leaseCar) { this.leaseCar = leaseCar; }

    public void setResume(String resume) { this.resume = resume; }

    public void addPhone(Phone p) { this.phones.add(p); }

    public List<Phone> getPhones() {
        return phones;
    }

    public void addLaptop(Laptop lap) {
        this.laptops.add(lap);
        lap.setContact(this); // fix the other side of the BiDi-relationship; choose whether to do it on this side or on the other side
    }

    public void removeLaptop(Laptop lap) {
        this.laptops.remove(lap);
        lap.setContact(null); // fix the other side of the BiDi-relationship; choose whether to do it on this side or on the other side
    }

    public void clearLeaseCar() { this.leaseCar = null; }

    public void addWorksAt(Department d) {
        this.worksAt.add(d);
        d.addEmployee(this);
    }
}

