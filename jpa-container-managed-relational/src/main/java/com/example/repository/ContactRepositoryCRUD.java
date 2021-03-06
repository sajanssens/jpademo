package com.example.repository;

import com.example.domain.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface ContactRepositoryCRUD extends CrudRepository<Contact, Long> {
    List<Contact> findByName(String name);

    @Query("SELECT c FROM Contact c WHERE c.name=:name OR c.email=:email")
    List<Contact> findByNameOrEmail(@Param("name") String name, @Param("email") String email);

    @Query("SELECT c FROM Contact c")
    Stream<Contact> findContacts();
}

