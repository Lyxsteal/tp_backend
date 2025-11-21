package com.example.ms_rutas.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TramoSugeridoDto {
    private String cordenadasOrigen;
    private String cordenadasDestino;
    private Integer nroOrden;
}
