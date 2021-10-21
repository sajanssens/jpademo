package com.example.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Builder
@ToString(exclude = "contact")
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "findAllLaptopsForContact", query = "SELECT lap FROM Laptop lap WHERE lap.contact.id = :id")
public class Laptop extends AbstractEntity {

    private String brand;

    @Getter
    @ManyToOne(cascade = {PERSIST, MERGE}) // BiDi, owning side, since FK is in this table; mappedBy is at Contact-side
    private Contact contact;

    public void setContact(Contact contact) {
        this.contact = contact;
        // contact.addLaptop(this); // fix the other side of the BiDi-relationship; choose whether to do it on this side or on the other side
    }
}
