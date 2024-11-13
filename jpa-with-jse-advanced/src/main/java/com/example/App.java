package com.example;

import jakarta.enterprise.inject.se.SeContainer;
import org.jboss.weld.environment.se.Weld;

public class App {

    // - First run `docker compose up` from jpademo
    // - Then
    //  -   connect to the database @ localhost:3307
    //  -   create a database `jpa-with-jse`
    public static void main(String[] args) {
        try (SeContainer ballenbak = Weld.newInstance().initialize()) {
            Boot boot = ballenbak.select(Boot.class).get();
            boot.runDemo();
        }
    }
}
