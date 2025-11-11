package com.example.ms_rutas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CAMIONEROS")
public class Camionero {
    @Id
    @Column(name="cedula")
    private Integer cedulaCamionero;

    @Column(name="idUsuario")
    private UUID idUsuario;

    @Column(name="nombre")
    private String nombre;

    @Column(name="apellido")
    private String apellido;

    @Column(name="telefono")
    private Integer telefono;

}