package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Department extends AbstractEntity {
    private String name;

    // Bidi, passive non-config side (passive, since FK is in join table, non-config since mappedBy/JoinTable is on the other side)
    @ManyToMany
    private List<Contact> employees = new ArrayList<>();

    public Department() { }

    public Department(String name) { this.name = name; }

    public void addEmployee(Contact contact) { this.employees.add(contact); }

    public String getName() { return name; }
}
