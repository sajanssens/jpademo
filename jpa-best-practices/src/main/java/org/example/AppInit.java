package org.example;

import javax.persistence.EntityManager;

import static org.example.Config.dropAndCreateDatabase;
import static org.example.Config.em;

public abstract class AppInit {

    protected final EntityManager em;

    public AppInit(String em) {
        dropAndCreateDatabase();
        this.em = em(em);
    }
}
