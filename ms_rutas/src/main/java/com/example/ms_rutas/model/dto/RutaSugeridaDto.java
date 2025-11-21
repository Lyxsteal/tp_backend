package com.example.ms_rutas.model.dto;

import com.example.ms_rutas.model.Ruta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class  RutaSugeridaDto {
    private Integer numeroDeAlternativa;
    private Double distancia;
    private Double duracion;
    private List<TramoSugeridoDto> tramos = new ArrayList<>(); //estará bien???
    private Double costoEstimado;
}
