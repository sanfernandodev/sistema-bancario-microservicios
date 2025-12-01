package com.banksystem.cuenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.banksystem.cuenta")
public class CuentaMovimientoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuentaMovimientoServiceApplication.class, args);
    }
}
