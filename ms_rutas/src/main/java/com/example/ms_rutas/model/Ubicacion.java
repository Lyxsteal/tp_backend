package com.example.ms_rutas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UBICACIONES")
public class Ubicacion {
    @Id
    @Column(name = "id_ubicacion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUbicacion;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "latitud")
    private Float latitud;

    @Column(name = "longitud")
    private Float longitud;

    @JoinColumn(name = "id_tramo")
    @OneToOne(fetch =  FetchType.EAGER)
    Tramo tramo;
}