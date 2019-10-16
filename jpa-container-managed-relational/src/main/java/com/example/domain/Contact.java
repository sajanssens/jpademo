package com.example.domain;

import com.example.util.BooleanTFConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "CONTACTPERSON")
@Builder
@Getter
@Setter
@ToString(exclude = {"phones", "laptops", "worksAtDepartments", "emailAddresses"})
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Contact { // doesn't extend abstractentity so we can choose a different id generation strategy here (as an example)

    @Id @GeneratedValue(strategy = IDENTITY)
    private long id;

    // Fields: --------------

    @Size(min = 2, max = 4) // bean validation
    @Column(name = "C_NAME", length = 50, nullable = false)
    private String name;

    @Temporal(value = DATE) // choose which part we want: date, time, or datetime (=default)
    private Date birthDate;

    @Column(unique = true, length = 250) // column must be smaller than 1000 bytes for the unique constraint to work
    private String email;

    @Convert(converter = BooleanTFConverter.class)
    private Boolean hasDriversLicence;

    @Lob
    @Basic(fetch = FetchType.LAZY) // lazy fetch on single field (instead of collections)
    private String resume; // can be a long piece of text, therefore fetch lazy

    @Lob // to instruct jdbc to treat this field specifically
    @Basic(fetch = FetchType.LAZY) // lazy loading on basic field mapping
    private byte[] picture;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Embedded
    private Address addressWork;

    // Relationships: ---------------------------------------------

    // --- Value collection ----------

    @ElementCollection // automatically becomes separate table
    private Set<String> emailAddresses;

    // --- Single Valued relationships  (@...ToOne) ------------

    // this is the owning side by definition (and why do you think?)
    @ManyToOne(cascade = PERSIST) // UniDi, dept has no reference back to this Contact's departmentBossOf-field
    @JoinColumn(name = "DEPT_BOSS_ID") // optionally specify FK name
    private Department bossOfDepartment;

    @ManyToOne(cascade = ALL) // BiDi, owning side; ParkingSpace has a OneToMany reference back to this Contact
    // @JoinColumn // optional
    private ParkingSpace parkingSpace;

    @OneToOne(cascade = PERSIST, orphanRemoval = true) // UniDi, TransientPropertyValueException when not cascading
    private Car leaseCar; // with UniDi, this is the owning side by definition; Contact just has the Car

    // --- Collection Valued relationships (@...ToMany) -------------------

    @Builder.Default // tell lombok's builder to use this initial value instead of the builder's default (which is unmodifiable list in this case)
    @OneToMany(cascade = {PERSIST, REMOVE}) // UniDi
    // @JoinTable(name = "contactworkphone", // UniDi OneToMany can use a JoinTable, since Phone, the many side, doesn't have a reference to this Contact and therefore no FK to contact
    //         joinColumns = @JoinColumn(name = "contactId"), // if JoinTable is omitted, JPA/Hibernate will generate it with default names
    //         inverseJoinColumns = @JoinColumn(name = "phoneId"))
    // @JoinColumn(name = "contact_id") // create FK in phone back to this contact to prevent a jointable from being created
    private List<Phone> phones = new ArrayList<>();

    @Builder.Default // tell lombok's builder to use this initial value instead of the builder's default (which is unmodifiable list in this case)
    @OneToMany(cascade = ALL,// BiDi, passive side
            mappedBy = "contact"/*,
            fetch = FetchType.EAGER*/) // optionally override the default
    @OrderBy("brand ASC")
    private List<Laptop> laptops = new ArrayList<>();

    @Singular // lombok: makes this plural field available in singular form to the builder
    @ManyToMany(cascade = PERSIST) // BiDi, owning side; Department has a ManyToMany reference back to this Contact's departmentWorking-field and has mappedBy
    // @JoinTable(name = "contactdepartment", // optional, to specify the names
    //         joinColumns = @JoinColumn(name = "contactId"), // if JoinTable is omitted, JPA/Hibernate will generate it with default names
    //         inverseJoinColumns = @JoinColumn(name = "departmentId"))
    private Set<Department> worksAtDepartments; // single @JoinColumn not possible (since the FK's reside in the generated join table)

    // ------------ Methods --------------

    public void addLaptop(Laptop lap) {
        this.laptops.add(lap);
        lap.setContact(this); // fix the other side of the BiDi-relationship; choose whether to do it on this side or on the other side
    }

    public void removeLaptop(Laptop lap) {
        this.laptops.remove(lap);
        lap.setContact(null); // fix the other side of the BiDi-relationship; choose whether to do it on this side or on the other side
    }

    public void clearLeaseCar() { this.leaseCar = null; }

    public void addDepartment(Department d) {
        this.worksAtDepartments.add(d);
        d.add(this); // this is the owning side, so we make it responsible for managing the (BiDi) relationship.
    }

}


