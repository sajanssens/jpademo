package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import java.util.Collection;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "findByName", query = "SELECT d FROM Department d WHERE d.name LIKE :name")
public class Department extends AbstractEntity {

    private String name;

    @ManyToMany(mappedBy = "departmentWorking") // BiDi, passive side
    private Collection<Contact> contacts;
}
