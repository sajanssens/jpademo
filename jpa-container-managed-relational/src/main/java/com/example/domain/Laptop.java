package com.example.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Laptop extends AbstractEntity {
    private String name;

    @Getter
    @ManyToOne(cascade = {PERSIST, MERGE}) // BiDi, owning side, since mappedBy is at Contact
    private Contact owner;

    public void setOwner(Contact owner) {
        owner.removeLaptop(this); // fix the passive side
        this.owner = owner;
    }
}
