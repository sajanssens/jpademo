package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class ParkingSpace extends AbstractEntity {
    private int number;

    @OneToMany(mappedBy = "parkingSpace")
    private Collection<Contact> contacts;
}
