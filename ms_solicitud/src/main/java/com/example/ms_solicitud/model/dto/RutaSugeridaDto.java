package com.example.ms_solicitud.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RutaSugeridaDto {
    private Integer numeroDeAlternativa;
    private Double distancia;
    private Double duracion;
    private List<TramoSugeridoDto> tramos;
    private Double costoEstimado;
}
