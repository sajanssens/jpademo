package com.example.demo.domain;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class PersonDao {

    private EntityManager em;

    public PersonDao(EntityManager em) {
        this.em = em;
    }

    public void create(Person person) {
        executeInTransaction(person, this::persist);
    }

    public Optional<Person> read(long id) {
        return Optional.ofNullable(em.find(Person.class, id));
    }

    public void updateName(long id, String name) {
        Person person = read(id).get();
        person.setName(name);
        executeInTransaction(person, this::merge);
    }

    public Person update(Person p) {
        return executeInTransaction(p, this::merge);
    }

    public void delete(Person p) {
        executeInTransaction(p, this::remove);
    }

    private <T> T executeInTransaction(T t, Function<T, T> action) {
        try {
            em.getTransaction().begin();
            T apply = action.apply(t);
            em.getTransaction().commit();
            em.clear();
            return apply;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            em.getTransaction().rollback();
        }
        return null;
    }

    private Person persist(Person p) {
        em.persist(p);
        return p;
    }

    private Person merge(Person p) {
        return em.merge(p);
    }

    private Person remove(Person p) {
        em.remove(p);
        return p;
    }
}
