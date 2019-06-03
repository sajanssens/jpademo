package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car extends AbstractEntity {
    private String brand;

    @OneToOne(mappedBy = "leaseCar", fetch = FetchType.LAZY)
    private Contact contact;
}
