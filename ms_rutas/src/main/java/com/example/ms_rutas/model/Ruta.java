package com.example.ms_rutas.model;


import jakarta.persistence.*;
import lombok.*;

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

    @Column(name="idSolicitud")
    private UUID idSolicitud;

    @Column(name="cantidadTramos")
    private Integer cantidadTramos;

    @Column(name="cantidadDepositos")
    private Integer cantidadDepositos;

    @OneToMany(mappedBy = "ruta")
    private List<Tramo> tramos;

}