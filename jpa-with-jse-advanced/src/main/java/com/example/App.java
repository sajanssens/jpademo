package com.example;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;

@Singleton
public class App {

    @Inject Logger logger;

    private void run() {
        logger.info("sfgfg");
        // see test code for tests

    }

    public static void main(String[] args) {
        try (SeContainer container = Weld.newInstance().initialize()) {
            App app = container.select(App.class).get();
            app.run();
        }
    }
}
