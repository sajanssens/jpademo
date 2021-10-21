package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "findByName", query = "SELECT d FROM Department d WHERE d.name LIKE :name")
public class Department extends AbstractEntity {

    private String name;

    @ManyToMany/*(mappedBy = "worksAtDepartments")*/ // BiDi, passive side
    private List<Contact> workers;

    public Department add(Contact contact) {
        this.workers.add(contact);
        return this;
    }
}
