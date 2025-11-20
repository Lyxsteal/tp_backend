package com.example.ms_rutas.model.dto;

import lombok.Getter;

@Getter
public class CamionDto {
    private String patente;
    private Integer idCamionero;
    private Integer capacidadPeso;
    private Integer capacidadVolumen;
    private Float costoBaseTranslado;
    private Float consCombKm;
}
