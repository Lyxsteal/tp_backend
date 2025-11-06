package com.example.ms_solicitud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CONTENEDORES")
public class Contenedor {
    @Id
    @Column(name="id_contenedor")
    private int idContenedor;

    @Column(name="peso")
    private Double peso;


    @Column(name="volumen")
    private Double volumen;


    @Column(name="estado")
    private String estado;

    @Column(name="tiempoEstadia")
    private int tiempoEstadia;
}
