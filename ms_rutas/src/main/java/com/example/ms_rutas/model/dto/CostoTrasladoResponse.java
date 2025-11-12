package com.example.ms_rutas.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CostoTrasladoResponse {
    private String patente;
    private Float costoBaseTraslado;
}
