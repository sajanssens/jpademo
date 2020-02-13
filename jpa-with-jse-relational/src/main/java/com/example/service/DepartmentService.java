package com.example.service;

import com.example.domain.Department;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DepartmentService {

    private EntityManager em;

    public DepartmentService(EntityManager em) {
        this.em = em;
    }

    public void save(Department d) {
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
    }

    public List<Department> findAll() {
        TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d", Department.class);
        return query.getResultList();
    }

    public Department find(long id) {
        return em.find(Department.class, id);
    }

    public void remove(long id) {
        Department d = find(id);
        if (d != null) {
            em.getTransaction().begin();
            em.remove(d);
            em.getTransaction().commit();
        }
    }

}
