package com.example.domain;

import com.example.util.BooleanTFConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Table(name = "CONTACTPERSON")
@Builder
// @Setter
@Getter
@NoArgsConstructor
// @AllArgsConstructor
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE) // note
    private long id;

    @Column(name = "C_NAME", length = 50, nullable = false)
    private String name;

    @Temporal(value = TemporalType.DATE)
    private Date birthDate;

    @Column(unique = true)
    private String email;

    @Convert(converter = BooleanTFConverter.class)
    private Boolean hasDriversLicence;

    @Basic(fetch = FetchType.LAZY)
    private String resume;

    @Lob // to instruct jdbc to treat this field specifically
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Embedded
    private Address addressWork;

    @ElementCollection
    private Set<String> emailAddresses;

    // Single Valued relationships -------------------

    @OneToOne(cascade = PERSIST, orphanRemoval = true) // UniDi, TransientPropertyValueException when not cascading
    private Car leaseCar; // with UniDi, this is the owning side by definition

    @ManyToOne(cascade = PERSIST) // UniDi, dept has no reference back to this Contact's departmentBossOf-field
    @JoinColumn(name = "DEPT_BOSS_ID") // this is the owning side by definition (and why do you think?)
    private Department departmentBossOf;

    @ManyToOne(cascade = PERSIST) // BiDi, ParkingSpace has a OneToMany reference back to this Contact
    @JoinColumn // name is optional
    private ParkingSpace parkingSpace;

    // Collection Valued relationships -------------------

    @OneToMany(cascade = PERSIST) // UniDi
    @JoinTable(name = "contactworkphone", // UniDi OneToMany must use a JoinTable, since immutable Phone, the many side, doesn't have a reference to this Contact and therefore no FK to contact
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "phoneId"))
    private Collection<Phone> phoneWork;

    @OneToMany(cascade = {PERSIST, MERGE},// BiDi, passive side
            mappedBy = "owner"/*,
            fetch = FetchType.EAGER*/) // override the default
    private List<Laptop> laptops;

    @ManyToMany(cascade = PERSIST) // BiDi, owning side; Department has a ManyToMany reference back to this Contact's departmentWorking-field and has mappedBy
    private Collection<Department> departmentWorking; // @JoinColumn not possible (since the FK's reside in the generated join table)

    // ------------ Methods --------------

    public Contact(long id, String name, Date birthDate, String email, Boolean hasDriversLicence, String resume, byte[] picture, ContactType type, Address addressWork, Set<String> emailAddresses, Car leaseCar, Department departmentBossOf, ParkingSpace parkingSpace, Collection<Phone> phoneWork, List<Laptop> laptops, Collection<Department> departmentWorking) {
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.hasDriversLicence = hasDriversLicence;
        this.resume = resume;
        this.picture = picture;
        this.type = type;
        this.addressWork = addressWork;
        this.emailAddresses = emailAddresses;
        this.leaseCar = leaseCar;
        this.departmentBossOf = departmentBossOf;
        this.parkingSpace = parkingSpace;
        this.phoneWork = phoneWork;
        this.laptops = new ArrayList<>();
        this.departmentWorking = departmentWorking;
    }

    public void addLaptop(Laptop lap) {
        this.laptops.add(lap);
        lap.setOwner(this); //to fix the BiDi relationship
    }

    public void removeLaptop(Laptop lap) {
        this.laptops.remove(lap);
    }

    public void setLeaseCar(Car leaseCar) { this.leaseCar = leaseCar; }

    public void setName(String name) { this.name = name; }
}



