package com.example.ms_rutas.model.dto;

import com.example.ms_rutas.model.Ruta;
import com.example.ms_rutas.model.TipoTramo;
import lombok.Getter;

@Getter
public class TramoDto {
    private String coordenadasOrigen;
    private String coordenadasDestino;
    private TipoTramo tipoTramo;
    private String estadoTramo;

}
