package com.example.ms_solicitud.model;

import com.example.ms_solicitud.model.cambioEstado.CambioEstado;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;
import java.util.UUID;

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
    private Integer numero;

    @Column(name="ruta_id")
    private Integer idRuta;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="contenedor_id")
    private Contenedor Contenedor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cliente_id")
    private Cliente Cliente;

    @Column(name="costoEstimado")
    private Double costoEstimado;

    @Column(name="tiempoEstimado")
    private Double tiempoEstimado;

    @Column(name="costoFinal")
    private Double costoFinal;

    @Column(name="tiempoReal")
    private Double tiempoReal;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tarifa_id")
    private Tarifa idTarifa;

    @Column(name="coordenadasOrigen")
    private String coordenadasOrigen;
    
    @Column(name="coordenadarDestino")
    private String coordenadasDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoSolicitud estadoActual;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL , mappedBy = "solicitud")
    private List<CambioEstado>  cambioEstado;


}
