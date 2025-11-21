package com.example.ms_rutas.repository;

import com.example.ms_rutas.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CamionRepository extends JpaRepository<Camion, String> {
    @Query(value = "SELECT * FROM CAMIONES WHERE disponibilidad = TRUE AND capacidad_volumen >= :volumenContenedor AND capacidad_peso >= :pesoContenedor", nativeQuery = true)
    List<Camion> encontrarDisponibles(
            @Param("pesoContenedor") Double pesoContenedor,
            @Param("volumenContenedor") Double volumenContenedor);

    @Query(value = "SELECT AVG(c.consCombKm) FROM Camion c WHERE c.capacidadVolumen >= :volumenContenedor AND c.capacidadPeso >= :pesoContenedor ")
    Double promedioConsumo(
            @Param("volumenContenedor") Double volumenContenedor,
            @Param("pesoContenedor") Double pesoContenedor);
}
