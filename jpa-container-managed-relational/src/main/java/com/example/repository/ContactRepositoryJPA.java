package com.example.repository;

import com.example.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepositoryJPA extends JpaRepository<Contact, Long> {
    List<Contact> findByEmail(String email);
}
