package com.example.ms_solicitud.model.dto;

import com.example.ms_solicitud.model.Cliente;
import com.example.ms_solicitud.model.Contenedor;
import lombok.Getter;

@Getter
public class SolicitudDto {
    private Contenedor contenedor;
    private Cliente cliente;
    private String coordenadasOrigen;
    private String coordenadasDestino;
}
