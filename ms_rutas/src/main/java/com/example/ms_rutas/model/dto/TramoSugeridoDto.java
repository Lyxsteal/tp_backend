package com.example.ms_rutas.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TramoSugeridoDto {
    private String coordenadasOrigen;
    private String coordenadasDestino;
    private Integer nroOrden;
}
