package com.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public final class EntityManagerFactory {

    private EntityManagerFactory() { }

    public static EntityManager emH2 = Persistence.createEntityManagerFactory("ContactServiceH2").createEntityManager();
    public static EntityManager emMySQL = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    public static EntityManager em = emH2;

}
