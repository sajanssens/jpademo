package com.example;

import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

@Singleton
@Alternative
public class EntityManagerProducerAlt {

    @Produces
    public static EntityManager h2() {
        return Persistence.createEntityManagerFactory("H2-IT").createEntityManager();
    }
}
