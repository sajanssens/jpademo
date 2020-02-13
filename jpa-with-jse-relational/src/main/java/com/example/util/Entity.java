package com.example.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public enum Entity {
    EM;

    public static EntityManager manager = Persistence.createEntityManagerFactory("ContactServiceH2").createEntityManager();

}
