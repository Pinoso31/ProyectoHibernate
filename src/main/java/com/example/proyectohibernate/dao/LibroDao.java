package com.example.proyectohibernate.dao;

import com.example.proyectohibernate.database.JPAutil;
import com.example.proyectohibernate.model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import static com.example.proyectohibernate.database.JPAutil.getEntityManager;
public class LibroDao {

    public void guardar(Libro libro) {
        EntityManager em = getEntityManager().createEntityManager();
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
        EntityManager em = getEntityManager().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT l FROM Libro l ORDER BY l.id",
                    Libro.class
            ).getResultList();
        } finally {
            em.close();
        }
    }


    public boolean modificar(Libro libro) {
        EntityManager em = getEntityManager().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libroBD = em.find(Libro.class, libro.getId());

            if (libroBD == null) {
                tx.rollback();
                return false;
            }
            libroBD.setTitulo(libro.getTitulo());
            libroBD.setAutor(libro.getAutor());
            libroBD.setAnioPublicacion(libro.getAnioPublicacion());
            libroBD.setPrecio(libro.getPrecio());
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Devuelve true si el libro existía y se eliminó, false si no se encontró
    public boolean eliminar(Long id){
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Libro libroBD = em.find(Libro.class, id);
            if(libroBD == null){
                tx.rollback();
                return false;
            }
            em.remove(libroBD);
            tx.commit();
            return true;
        } catch (RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        } finally{
            em.close();
        }
    }
}
