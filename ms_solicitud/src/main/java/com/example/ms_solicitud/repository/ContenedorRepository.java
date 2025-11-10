package com.example.ms_solicitud.repository;

import com.example.ms_solicitud.model.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {

    @Query(value = "SELECT * FROM Contenedor WHERE estado = 'PENDIENTE'", nativeQuery = true)
    List<Contenedor> obtenerPendientes();

}
