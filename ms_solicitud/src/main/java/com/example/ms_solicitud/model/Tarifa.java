package com.example.ms_solicitud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "TARIFA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarifa {
    @Id
    @Column(name="idTarifa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarifa;

    @Column(name="valorFijoTramo")
    private float valorFijoTramo;


    @Column(name="valorPorVolumen")
    private float valorPorVolumen;


    @Column(name="valorFijoCombustible")
    private float valorFijoCombustible;

}
