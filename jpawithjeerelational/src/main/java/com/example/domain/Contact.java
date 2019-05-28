package com.example.domain;

import com.example.util.BooleanTFConverter;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Table(name = "MYCONTACT")
@Data
@Builder
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

    @OneToOne(cascade = CascadeType.PERSIST) // UniDi
    private Car leaseCar;

    @Embedded
    private Address addressWork;

    @ManyToOne(cascade = CascadeType.PERSIST) // UniDi, dept has no reference back to this Contact
    @JoinColumn(name = "DEPT_BOSS_ID")
    private Department departmentBossOf;

    @ManyToOne(cascade = CascadeType.PERSIST) // BiDi, ParkingSpace has a OneToMany reference back to this Contact
    @JoinColumn // name is optional
    private ParkingSpace parkingSpace;

    @ManyToMany(cascade = CascadeType.PERSIST)
    // BiDi, Department has a ManyToMany reference back to these Contacts, and has mappedBy so is the passive side
    private Collection<Department> departmentWorking;

    @OneToMany(cascade = CascadeType.PERSIST)
    // UniDi OneToMany must use a JoinTable, since Phone, the many side, doesn't have a reference to this Contact
    @JoinTable(name = "contactworkphone",
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "phoneId"))
    private Collection<Phone> phoneWork;
}



