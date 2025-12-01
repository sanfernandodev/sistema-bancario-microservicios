package com.banksystem.cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.banksystem.cliente")
public class ClientePersonaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientePersonaServiceApplication.class, args);
    }
}
