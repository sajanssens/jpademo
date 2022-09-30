package com.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team extends AbstractEntity {
    private String yell;
    private int number;

    @OneToMany/*(mappedBy = "team")*/ // Bidi, passive side; mappedBy is needed
    private Collection<Contact> contacts;
    // NOTE: if mappedBy is omitted, additional table parking_space_contact will be created
    // because it will become UniDi and there is a NEW relation back to Contact.
}
