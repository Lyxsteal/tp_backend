package com.example.ms_solicitud.model.dto;

import com.example.ms_solicitud.model.EstadoSolicitud;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class HistorialDto {
    private LocalDateTime fechaInicio;
    private EstadoSolicitud Estado;
}
