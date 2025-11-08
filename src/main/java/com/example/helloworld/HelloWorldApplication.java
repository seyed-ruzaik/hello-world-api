package com.example.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Spring Boot application.
 *
 * This class:
 * - Boots the embedded web server (e.g., Tomcat).
 * - Scans for components (@RestController, @Service, etc.) in this package.
 */
@SpringBootApplication
public class HelloWorldApplication {

    /**
     * Main method used when running the application via:
     * - `mvn spring-boot:run`
     * - or running the built JAR.
     *
     * @param args command-line arguments (not used in this app)
     */
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }
}
