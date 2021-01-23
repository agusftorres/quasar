package com.fuego.quasar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan
public class FuegoDeQuasarApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuegoDeQuasarApplication.class, args);
    }

}
