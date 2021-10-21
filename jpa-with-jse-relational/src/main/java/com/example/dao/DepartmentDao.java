package com.example.dao;

import com.example.domain.Department;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DepartmentDao {

    @Inject
    private EntityManager em;

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
