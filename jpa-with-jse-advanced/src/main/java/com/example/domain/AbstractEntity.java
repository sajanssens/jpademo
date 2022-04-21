package com.example.domain;

import com.example.dao.Identifiable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {
    @Id @GeneratedValue(strategy = IDENTITY)
    protected long id;

    public Long getId() { return id; }
}
