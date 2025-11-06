package com.example.ms_solicitud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "SOLICITUD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {
    @Id
    @Column(name="numero")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numero;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="contenedor_id")
    private Contenedor numeroContenedor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cliente_id")
    private Cliente dniCliente;

    @Column(name="costoEstimado")
    private float costoEstimado;


    @Column(name="tiempoEstimado")
    private float tiempoEstimado;


    @Column(name="costoFinal")
    private float costoFinal;


    @Column(name="tiempoReal")
    private float tiempoReal;

    @Column(name="consumoEstimado")
    private float consumoEstimado;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tarifa_id")
    private Tarifa idTarifa;


    @Column(name="coordenadasOrigen")
    private double coordenadasOrigen;
    
    @Column(name="coordenadarDestino")
    private double coordenadasDestino;
}
