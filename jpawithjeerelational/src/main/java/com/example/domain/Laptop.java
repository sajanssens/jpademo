package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(cascade = CascadeType.MERGE) // BiDi, owning side
    private Contact owner;

    public void setOwner(Contact owner) {
        owner.removeLaptop(this);
        this.owner = owner;
    }
}
