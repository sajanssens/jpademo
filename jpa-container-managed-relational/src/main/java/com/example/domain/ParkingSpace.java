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

    @OneToMany(mappedBy = "parkingSpace") // Bidi, passive side
    private Collection<Contact> contacts;
}
