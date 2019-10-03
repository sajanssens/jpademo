package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // @Basic is present implicitly on each field
    private String firstname;

    private Date birthdate;

    public Contact() {}

    public Contact(String firstname, Date birthdate) {
        this.firstname = firstname;
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public long getId() {
        return id;
    }
}
