package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Lab 8: Table Relationships - Product Shop
 * CP353002 Principles of Software Design
 *
 * Entry point of the application. Kept intentionally thin (Single
 * Responsibility Principle): its only job is bootstrapping the Spring
 * context. All business behaviour lives in the layered packages below.
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
