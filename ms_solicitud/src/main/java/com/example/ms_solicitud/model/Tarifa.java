package com.example.ms_solicitud.model;

import com.example.ms_solicitud.model.dto.CostoFinalDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "TARIFA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarifa {
    @Id
    @Column(name="idTarifa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTarifa;

    @Column(name="valorFijoTramo")
    private Double valorFijoTramo;


    @Column(name="valorPorVolumen")
    private Double valorPorVolumen;


    @Column(name="valorFijoCombustible")
    private Double valorFijoCombustible;

    @Column(name = "valorPorEstadia")
    private Double valorPorEstadia;

    public Double calcularCostos(CostoFinalDto costos, Double volumenContenedor){
        Double totalTramos = valorFijoTramo * costos.getCantTramos();
        Double totalComb = valorFijoCombustible * costos.getConsumoTotalComb();
        Double totalestadia = valorPorEstadia * costos.getDiasTotalEstadia();
        Double totalVolumen = valorPorVolumen * volumenContenedor;
        return totalTramos + totalComb + totalestadia + totalVolumen;
    }

}
