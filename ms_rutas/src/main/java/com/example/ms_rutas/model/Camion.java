package com.example.ms_rutas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CAMIONES")
public class Camion {
    @Id
    @Column(name="patente")
    private String patente;

    @JoinColumn(name = "id_camionero")
    @OneToOne(fetch =  FetchType.LAZY)
    private Camionero camionero;

    @Column(name="capacidad_peso")
    private Integer capacidadPeso;

    @Column(name="capacidad_volumen")
    private Integer capacidadVolumen;

    @Column(name="disponibilidad")
    private Boolean disponibilidad;

    @Column(name="costo_base_traslado")
    private Float costoBaseTraslado;

    @Column(name = "cons_comb_km")
    private Float consCombKm;


}
