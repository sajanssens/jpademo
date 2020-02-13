package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car extends AbstractEntity {
    private String brand;
}
