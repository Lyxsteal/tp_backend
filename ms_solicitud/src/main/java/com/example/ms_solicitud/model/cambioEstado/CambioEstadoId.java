package com.example.ms_solicitud.model.cambioEstado;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable

public class CambioEstadoId implements Serializable {

    @JsonIgnore
    private Integer idSolicitud;

    private LocalDateTime fechaCambio;

}
