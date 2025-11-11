package com.example.ms_rutas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CIUDADES")
public class Ciudad {
    @Id
    @Column(name = "idCiudad")
    private Integer idCiudad;

    @Column(name = "nombre")
    private String nombre;
}