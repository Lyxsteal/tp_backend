package com.example.ms_rutas.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConsumoBaseResponse {
    private String patente;
    private Float consumoBase;
}
