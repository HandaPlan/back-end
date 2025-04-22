package com.org.candoit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CandoitApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandoitApplication.class, args);
    }

}
