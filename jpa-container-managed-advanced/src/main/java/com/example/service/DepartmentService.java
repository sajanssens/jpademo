package com.example.service;

import com.example.domain.Department;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DepartmentService {

    @PersistenceContext
    private EntityManager em;

    public void create(Department d) { em.persist(d); }

    public Department find(long id) { return em.find(Department.class, id); }

    public List<Department> findDepartmentsByNameWithNamedQuery(String name) {
        TypedQuery<Department> query = em.createNamedQuery("findByName", Department.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }

}
