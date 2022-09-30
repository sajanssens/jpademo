package com.example.domain;

import jakarta.persistence.Entity;

@Entity
public class PermanentEmployee extends Contact {
    private int grade;
}
