package com.gasmapp.gasmapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GasmappApplication {

    public static void main(String[] args) {
        SpringApplication.run(GasmappApplication.class, args);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("senha123");
        System.out.println("Hash da senha 'senha123': " + hash);
    }
}
