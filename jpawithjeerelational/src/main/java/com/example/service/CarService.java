package com.example.service;

import com.example.domain.Car;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class CarService {

    @PersistenceContext
    private EntityManager em;

    public void save(Car c) {
        em.persist(c);
    }

    public Car update(Car c) {
        return em.merge(c);
    }

    public Car find(long id) {
        return em.find(Car.class, id);
    }

    public void delete(long id) {
        Car car = find(id);
        if (car != null) {
            em.remove(car);
            em.flush();
        }
    }
}
