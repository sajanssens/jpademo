package com.example.demo.domain;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class TeamDao {

    private EntityManager em;

    public TeamDao(EntityManager em) {
        this.em = em;
    }

    public void create(Team person) {
        executeInTransaction(person, this::persist);
    }

    public Optional<Team> read(long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    public void updateName(long id, String name) {
        Team person = read(id).get();
        person.setName(name);
        executeInTransaction(person, this::merge);
    }

    public Team update(Team p) {
        return executeInTransaction(p, this::merge);
    }

    public void delete(Team p) {
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

    private Team persist(Team p) {
        em.persist(p);
        return p;
    }

    private Team merge(Team p) {
        return em.merge(p);
    }

    private Team remove(Team p) {
        em.remove(p);
        return p;
    }
}
