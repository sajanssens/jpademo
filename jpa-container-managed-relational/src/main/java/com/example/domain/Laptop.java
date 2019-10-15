package com.example.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Builder
@ToString(exclude = "user")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Laptop extends AbstractEntity {

    private String brand;

    @Getter
    @ManyToOne(cascade = {PERSIST, MERGE}) // BiDi, owning side, since mappedBy is at Contact
    private Contact user;

    public void setUser(Contact user) {
        this.user = user;
        user.addLaptop(this); // fix the passive side, since this is the owning side.
    }
}
