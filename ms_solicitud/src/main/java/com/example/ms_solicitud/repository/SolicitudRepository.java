package com.example.ms_solicitud.repository;

import com.example.ms_solicitud.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    @Query(value = "SELECT * FROM Solicitud  WHERE dniCliente = :idCliente", nativeQuery = true)
    List<Solicitud> findAllByCliente(@Param("idCliente") Integer idCliente);
}
