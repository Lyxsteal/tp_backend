package com.example.ms_rutas.repository;

import com.example.ms_rutas.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CamionRepository extends JpaRepository<Camion, String> {
    @Query(value = "SELECT * FROM Camion WHERE estado = '1' AND capacidadVolumen >= :volumenContenedor AND capacidadPeso >= :pesoContenedor", nativeQuery = true)
    List<Camion> encontrarDisponibles(
            @Param("pesoContenedor") Double pesoContenedor,
            @Param("volumenContenedor") Double volumenContenedor);
}
