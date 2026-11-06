package com.example.proyectohibernate.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAutil {

    private static EntityManagerFactory entityManagerFactory;

    public static synchronized void iniciar() {

        if (entityManagerFactory == null) {

            entityManagerFactory =
                    Persistence.createEntityManagerFactory("bibliotecaPU");

        }
    }

    public static EntityManagerFactory getEntityManager() {
        iniciar();
        return entityManagerFactory;
    }

    public static synchronized void cerrar() {

        if (entityManagerFactory != null
                && entityManagerFactory.isOpen()) {

            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}