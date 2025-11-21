package com.example.ms_solicitud.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatosSolicitudDto {
    private  String coordenadasOrigen;
    private  String coordenadasDestino;
    private  Double valorVolumenTarifa;
    private  Double valorTramoTarifa;
    private Double volumenContendor;
    private Double pesoContenedor;
}

