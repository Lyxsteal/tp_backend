package com.example.ms_rutas.model.dto;

import com.example.ms_rutas.model.Ruta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RutaSugeridaDto {
    private Ruta ruta;
    private Double distancia;
    private Double duracion;
    private Integer cantidadTramos;
}
