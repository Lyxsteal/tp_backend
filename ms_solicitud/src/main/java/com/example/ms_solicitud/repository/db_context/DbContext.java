package com.example.ms_solicitud.repository.db_context;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbContext {
    private final EntityManager manager;

    public static DbContext instance = null;

    private DbContext() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lego");
        manager = emf.createEntityManager();
    }

    public static DbContext getInstance() {
        if (instance == null) {
            instance = new DbContext();
        }
        return instance;
    }

    public EntityManager getManager() {
        return this.manager;
    }
}
