package com.example.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Laptop extends AbstractEntity {
    private String name;

    @Getter
    @ManyToOne(cascade = CascadeType.MERGE) // BiDi, owning side
    private Contact owner;

    public void setOwner(Contact owner) {
        owner.removeLaptop(this);
        this.owner = owner;
    }
}
