package com.example.ms_rutas.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "TRAMO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Tramo {
    @Id
    @Column(name="idTramo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTramo;


    @JoinColumn(name="idRuta")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Ruta ruta;

    @Column(name="nro_orden")
    private Integer  nroOrden;

    @Column(name="coordenadas_origen")
    private String coordenadasOrigen;

    @Column(name="coordenadas_destino")
    private String coordenadasDestino;

    @JoinColumn(name="patente_camion")
    @OneToOne(fetch = FetchType.EAGER)
    private Camion camion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tramo")
    private TipoTramo tipoTramo;


    @Column(name="estado_tramo")
    private String estadoTramo;


    @Column(name="fecha_hora_inicio")
    private LocalDate fechaHoraInicio;

    @Column(name="fecha_hora_fin")
    private LocalDate fechaHoraFin;


}



/*idTramo |PK
idRuta |FK
nroOrden
idUbicacionOrigen | FK
idUbicacionDestino | FK
idTipoTramo | FK
estadoTramo
costoAproximado
costoReal
fechaHoraInicio
fechaHoraFin
patenteCamion |FK */