package com.example.ms_rutas.model.dto;

import com.example.ms_rutas.model.Ruta;
import lombok.Getter;

@Getter
public class TramoDto {
    private Integer ubicacionOrigenId;
    private Integer ubicacionDestinoId;
    private Integer tipoTramo;
    private String estadoTramo;

}
