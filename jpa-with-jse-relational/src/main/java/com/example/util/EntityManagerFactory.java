package com.example.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public final class EntityManagerFactory {

    private EntityManagerFactory() { }

    private static EntityManager emH2 = Persistence.createEntityManagerFactory("ContactServiceH2").createEntityManager();
    private static EntityManager emMySQL = Persistence.createEntityManagerFactory("ContactServiceMySQL").createEntityManager();

    public static EntityManager em = emH2;

}
