package com.scit45.kiminomenyuwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KiminomenyuwaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiminomenyuwaApplication.class, args);
    }

}
