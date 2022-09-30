package com.example.demo;

import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 *
 */
@Path("/persons")
@Singleton
public class PersonsResource {

    @PersistenceContext EntityManager em;

    @GET
    // @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<Person> sayHello() {
        return List.of(new Person("Smits"), new Person("Bram"));
    }

    public static class Person {
        public final String name;

        public Person(String name) {
            this.name = name;
        }
    }
}
