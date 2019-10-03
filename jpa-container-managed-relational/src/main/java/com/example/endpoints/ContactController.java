package com.example.endpoints;

import com.example.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {
    @Autowired ContactRepository contactRepository;

    @GetMapping("hello")
    public String get(){
        return "World";
    }
}
