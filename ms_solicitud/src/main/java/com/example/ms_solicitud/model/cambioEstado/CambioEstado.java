package com.example.ms_solicitud.model.cambioEstado;
import com.example.ms_solicitud.model.EstadoSolicitud;
import com.example.ms_solicitud.model.Solicitud;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "CAMBIO_ESTADO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CambioEstado {
    @EmbeddedId
    private CambioEstadoId cambioEstadoId;

    @Column(name="estado")
    private EstadoSolicitud estado;

    @JsonIgnore
    @ManyToOne
    @MapsId("idSolicitud")
    private Solicitud solicitud;
}