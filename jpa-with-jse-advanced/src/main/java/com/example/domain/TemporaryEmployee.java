package com.example.domain;

import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class TemporaryEmployee extends Contact {
    private int level;

    public TemporaryEmployee() {}

    public TemporaryEmployee(String naam, int level) {
        super(naam, new Date());
        this.level = level;
    }
}
