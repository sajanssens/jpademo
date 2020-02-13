package com.example.domain;

import javax.persistence.Entity;

@Entity
public class Department extends AbstractEntity {
    private String name;

    public Department() { }

    public Department(String name) { this.name = name; }

}
