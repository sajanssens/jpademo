package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ContactSpecial extends Contact {
    private String reasonBeingSpecial;
}