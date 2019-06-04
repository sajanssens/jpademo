package com.example.service;

import com.example.domain.ParkingSpace;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class ParkingSpaceService {

    @PersistenceContext
    private EntityManager em;

    public void create(ParkingSpace p) { em.persist(p); }
}
