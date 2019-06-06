package com.example.domain;

import com.example.util.BooleanTFConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "CONTACTPERSON")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE) // note
    private long id;

    @Size(min = 2, max = 4) // bean validation
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
    @Basic(fetch = FetchType.LAZY) // lazy loading on basic field mapping
    private byte[] picture;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Embedded
    private Address addressWork;

    @ElementCollection
    private Set<String> emailAddresses;

    // Single Valued relationships -------------------

    @ManyToOne(cascade = PERSIST) // UniDi, dept has no reference back to this Contact's departmentBossOf-field
    @JoinColumn(name = "DEPT_BOSS_ID") // this is the owning side by definition (and why do you think?)
    private Department bossOfDepartment;

    @ManyToOne(cascade = ALL) // BiDi, owning side; ParkingSpace has a OneToMany reference back to this Contact
    @JoinColumn // name is optional
    private ParkingSpace parkingSpace;

    @OneToOne(cascade = PERSIST, orphanRemoval = true) // UniDi, TransientPropertyValueException when not cascading
    private Car leaseCar; // with UniDi, this is the owning side by definition

    // Collection Valued relationships -------------------

    @Singular // lombok: makes this plural field available in singular form to the builder
    @OneToMany(cascade = {PERSIST, MERGE, REMOVE},// BiDi, passive side
            mappedBy = "owner"/*,
            fetch = FetchType.EAGER*/) // override the default
    @OrderBy("name ASC")
    private List<Laptop> laptops;

    @OneToMany(cascade = {PERSIST, REMOVE}) // UniDi
    @JoinTable(name = "contactworkphone", // UniDi OneToMany must use a JoinTable, since immutable Phone, the many side, doesn't have a reference to this Contact and therefore no FK to worker
            joinColumns = @JoinColumn(name = "contactId"), // if JoinTable is omitted, JPA/Hibernate will generate it with default names
            inverseJoinColumns = @JoinColumn(name = "phoneId"))
    private Collection<Phone> phoneWork;

    @Singular // lombok: makes this plural field available in singular form to the builder
    @ManyToMany(cascade = PERSIST) // BiDi, owning side; Department has a ManyToMany reference back to this Contact's departmentWorking-field and has mappedBy
    private Set<Department> worksAtDepartments; // @JoinColumn not possible (since the FK's reside in the generated join table)

    // ------------ Methods --------------

    public void addLaptop(Laptop lap) {
        this.laptops.add(lap);
        lap.setOwner(this); // to fix the passive side of the BiDi relationship
    }

    public void removeLaptop(Laptop lap) {
        this.laptops.remove(lap);
    }

    public void clearLeaseCar() { this.leaseCar = null; }

    public void addDepartment(Department d) {
        this.worksAtDepartments.add(d);
        d.add(this);
    }

}



