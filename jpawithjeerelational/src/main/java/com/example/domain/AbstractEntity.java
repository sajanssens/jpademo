package com.example.domain;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@MappedSuperclass
public abstract class AbstractEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private long id;
}
