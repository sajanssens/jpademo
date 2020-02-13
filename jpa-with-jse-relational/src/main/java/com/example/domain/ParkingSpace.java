package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace extends AbstractEntity {
    private int number;

    @OneToMany/*(mappedBy = "parkingSpace")*/ // Bidi, passive side; mappedBy is needed
    private Collection<Contact> contacts;
    // NOTE: if mappedBy is omitted, additional table parking_space_contact will be created
    // because it will become UniDi and there is a NEW relation back to Contact.
}
