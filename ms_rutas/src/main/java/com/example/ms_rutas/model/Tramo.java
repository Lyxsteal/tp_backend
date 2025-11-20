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


    @JoinColumn(name = "id_ubicacion_origen")
    @OneToOne(fetch = FetchType.EAGER)
    private Ubicacion ubicacionOrigen;


    @JoinColumn(name ="id_ubicacion_destino")
    @OneToOne(fetch = FetchType.EAGER)
    private Ubicacion ubicacionDestino;

    @JoinColumn(name="patente_camion")
    @OneToOne(fetch = FetchType.EAGER)
    private Camion camion;

    @JoinColumn(name ="id_tipo_tramo")
    @OneToOne(fetch = FetchType.EAGER)
    private TipoTramo tipoTramo;


    @Column(name="estado_tramo")
    private String estadoTramo;

    @Column(name="costo_aproximado")
    private Float  costoAproximado;

    @Column(name="costo_real")
    private Float  costoReal;

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