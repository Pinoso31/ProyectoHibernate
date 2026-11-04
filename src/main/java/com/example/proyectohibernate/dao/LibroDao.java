package com.example.proyectohibernate.dao;

import com.example.proyectohibernate.database.JPAutil;
import com.example.proyectohibernate.model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class LibroDao {

    public void guardar(Libro libro) {
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
//            iniciar la transacion de postgresql
            tx.begin();
//           Decirle a Hibernate que preare la persistencia del objeto
            em.persist(libro);
//            confirmar los cambios en la base de datos
            tx.commit();
        }catch(RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        }finally{
            em.close();
        }
    }

    public List<Libro> listar() {
        EntityManager em = JPAutil.getEntityManager();
        try{
            return em.createQuery("SELECT l from Libro ORDER BY l.id", Libro.class).getResultList();
        }
        finally {
            em.close();
        }
    }
}
