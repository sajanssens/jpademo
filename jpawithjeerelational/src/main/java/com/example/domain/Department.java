package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department extends AbstractEntity {

    private String name;

    @ManyToMany(mappedBy = "departmentWorking")
    private Collection<Contact> contacts;
}
