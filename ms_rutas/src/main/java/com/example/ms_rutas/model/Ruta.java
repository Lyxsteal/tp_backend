package com.example.ms_rutas.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RUTAS")
public class Ruta {
    @Id
    @Column(name="idRuta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRuta;

    @Column(name = "idSolicitud")
    private Integer idSolicitud;


    @OneToMany(mappedBy = "ruta",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tramo> tramos;


    public Integer obtenerDiasEstadia(){
        Integer diasEstadia = 0;
        for(Integer i = 0; i < tramos.size() -1; i++){
            diasEstadia += diferenciaDiasEstadia(tramos.get(i).getFechaHoraFin(),tramos.get(i+1).getFechaHoraInicio());
        }
        return diasEstadia;
    }

    public Integer diferenciaDiasEstadia(LocalDate fechaInicio, LocalDate fechaFin){
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        return (int) dias;
    }


}