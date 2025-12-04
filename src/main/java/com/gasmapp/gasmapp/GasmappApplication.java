package com.gasmapp.gasmapp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
public class GasmappApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(GasmappApplication.class, args);
    }

    @PostConstruct
    public void init() throws SQLException {
        // Teste de conexão com o banco
        System.out.println("DB URL: " + dataSource.getConnection().getMetaData().getURL());

        // Teste do hash de senha
        String senha = "senha123"; // senha em texto claro
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(senha);

        System.out.println("Senha em texto claro: " + senha);
        System.out.println("Hash da senha: " + hash);
    }
}
