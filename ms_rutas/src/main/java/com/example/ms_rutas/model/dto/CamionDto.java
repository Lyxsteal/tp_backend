package com.example.ms_rutas.model.dto;

import lombok.Getter;

@Getter
public class CamionDto {
    private String patente;
    private Integer idCamionero;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Float costoBaseTranslado;
    private Float consCombKm;
}
