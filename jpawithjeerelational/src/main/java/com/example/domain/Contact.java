package com.example.domain;

import com.example.util.BooleanTFConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;

@Table(name = "CONTACTPERSON")
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinTable(name = "contactworkphone", // UniDi OneToMany must use a JoinTable, since Phone, the many side, doesn't have a reference to this Contact
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "phoneId"))
    private Collection<Phone> phoneWork;

    @ManyToMany(cascade = PERSIST) // BiDi, owning side; Department has a ManyToMany reference back to this Contact's departmentWorking-field and has mappedBy
    private Collection<Department> departmentWorking; // @JoinColumn not possible (since the FK's reside in the generated join table)
}



