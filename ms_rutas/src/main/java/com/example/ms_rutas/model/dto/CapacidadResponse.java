package com.example.ms_rutas.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CapacidadResponse {
    private String patente;
    private Boolean cumpleCapacidad;
}
