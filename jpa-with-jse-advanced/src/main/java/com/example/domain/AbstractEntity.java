package com.example.domain;

import com.example.dao.Identifiable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import static jakarta.persistence.GenerationType.IDENTITY;

@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {
    @Id @GeneratedValue(strategy = IDENTITY)
    protected long id;

    public Long getId() { return id; }
}
