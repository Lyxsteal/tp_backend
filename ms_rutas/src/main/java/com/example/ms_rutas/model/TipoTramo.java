package com.example.ms_rutas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TIPOSTRAMOS")
public class TipoTramo {
    @Id
    @Column(name = "idTipoTramo")
    private Integer idTipoTramo;

    @Column(name = "nombre_tipo")
    private String nombreTipoTramo;
}
