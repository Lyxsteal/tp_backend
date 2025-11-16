package com.example.ms_rutas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deposito {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @JoinColumn(name = "id_ubicacion")
    @OneToOne(fetch =  FetchType.EAGER, cascade = CascadeType.ALL)
    Ubicacion ubicacion;

}
/*identificacion | PK
nombre
direccion
idUbicacion | FK
costoPorDiaEstadia*/