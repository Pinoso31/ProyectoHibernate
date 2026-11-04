package com.example.proyectohibernate.database;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAutil {

    private static EntityManagerFactory entityManagerFactory;

    public static synchronized void iniciar () {
        if (entityManagerFactory == null) {
            String password = System.getenv("1234");
            if(password == null || password.isEmpty()) {
                throw new RuntimeException("DB_PASSWORD environment variable is empty");
            }

            Map<String, Object> propiedades = new HashMap<>();
            propiedades.put("jakarta.persistance.jdbc.password", password);
            entityManagerFactory = Persistence.createEntityManagerFactory("bibliotecaPU", propiedades);
        }
    }
    public static EntityManagerFactory getEntityManagerFactory() {
        iniciar();
        return entityManagerFactory.createEntityManager().getEntityManagerFactory();
    }

    public static synchronized void cerrar() {
        if(entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}
