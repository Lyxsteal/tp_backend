package com.example.ms_rutas.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CapacidadRequest {
    private Double pesoContenedor;
    private Double volumenContenedor;
}
