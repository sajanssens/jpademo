package com.example.util;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class EntityManagerProducer {

    @Produces
    public EntityManager em() { return EntityManagerFactory.em; }
}
