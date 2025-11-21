package com.example.ms_rutas.repository;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TramoRepository extends JpaRepository<Tramo, Integer> {
    @Query(value = "SELECT t FROM Tramo t WHERE t.camion.camionero.cedulaCamionero = :cedula")
    List<Tramo> encontrarTramosCamionero(
            @Param("cedula") Integer cedula);
    @Query(value = "SELECT t FROM Tramo t WHERE t.nroOrden = :nroOrden AND t.ruta.idRuta = :idRuta")
    Tramo encontrarTramoPorNroOrden(
            @Param("nroOrden") Integer nroOrden,
            @Param("idRuta") Integer idRuta
    );
}
