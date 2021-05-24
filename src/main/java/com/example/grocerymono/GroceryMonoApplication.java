package com.example.grocerymono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class GroceryMonoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryMonoApplication.class, args);
    }

}
