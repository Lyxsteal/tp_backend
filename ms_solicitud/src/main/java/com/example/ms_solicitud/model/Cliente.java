package com.example.ms_solicitud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLIENTES")
public class Cliente {
    @Id
    @Column(name="dni")
    private Integer dni;

    @Column(name="nombre")
    private String nombre;

    @Column(name="apellido")
    private String apellido;

    @Column(name="telefono")
    private String telefono;

    @Column(name="mail")
    private String mail;

    //@JoinColumn(referencedColumnName = "id_usuario")
    //@OneToOne(fetch = FetchType.EAGER)
    //Usuario usuario;

}
