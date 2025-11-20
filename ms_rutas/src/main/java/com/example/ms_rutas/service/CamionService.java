package com.example.ms_rutas.service;

import com.example.ms_rutas.model.Camion;
import com.example.ms_rutas.model.Camionero;
import com.example.ms_rutas.model.Tramo;
import com.example.ms_rutas.model.dto.*;
import com.example.ms_rutas.repository.CamioneroRepository;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ms_rutas.repository.CamionRepository;


import java.util.List;
@RequiredArgsConstructor
@Service
public class CamionService {
    private final CamionRepository camionRepository;
    private final CamioneroRepository camioneroRepository;
    private static final Logger log = LoggerFactory.getLogger(CamionService.class);


    @Transactional(readOnly = true)
    public List<Camion> obtenerTodasLosCamiones() {
        log.info("Buscando todos los camiones");
        return camionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Camion obtenerCamionPorPatente(String patente) {
        return camionRepository.findById(patente)
                .orElseThrow(() -> {
                    log.warn("No se pudo encontrar el camión con la patente " + patente);
                    return new RuntimeException("Camión no encontrado con patente: " + patente);
                });
    }

    @Transactional
    public Camion crearCamion(CamionDto camionDto) {
        Camion camion = new Camion();
        Integer cedula = camionDto.getIdCamionero();

        Camionero camioneroExistente = camioneroRepository.findById(cedula)
                .orElseThrow(() -> {
                    log.warn("No se pudo encontrar el camionero con cédula" + cedula);
                    return new RuntimeException("Camionero no encontrado con cédula: " + cedula);
                });

        if (camionRepository.existsById(camionDto.getPatente())) {
            log.warn("El camion ya existe, no se puede crear");
            throw new RuntimeException("El camion ya existe");
        }
        camion.setCamionero(camioneroExistente);
        camion.setPatente(camionDto.getPatente());
        camion.setCapacidadPeso(camionDto.getCapacidadPeso());
        camion.setCapacidadVolumen(camionDto.getCapacidadVolumen());
        camion.setConsCombKm(camionDto.getConsCombKm());
        camion.setCostoBaseTraslado(camionDto.getCostoBaseTranslado());
        camion.setDisponibilidad(true);
        log.info("creando camion");
        return camionRepository.save(camion);
    }

    @Transactional
    public Camion actualizarCamion(String patente, Camion camionActualizada) {
        Camion camionExistente = obtenerCamionPorPatente(patente);
        return camionRepository.save(camionExistente);
    }

    @Transactional
    public void eliminarCamion(String patente) {
        if (!camionRepository.existsById(patente)) {
            log.warn("No se pudo encontrar el camion con patente" + patente);
            throw new RuntimeException("No se puede eliminar. Camión no encontrado con patente: " + patente);
        }
        camionRepository.deleteById(patente);
    }
    @Transactional
    public CapacidadResponse consultarCapacidadCamion(String patente, CapacidadRequest capacidadRequest){
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> {
                    log.warn("No se pudo encontrar el camion con patente" + patente);
                    return new RuntimeException("Camión no encontrado con patente: " + patente);
                });
        CapacidadResponse capacidadResponse = new CapacidadResponse();
        capacidadResponse.setPatente(camion.getPatente());
        if (camion.getCapacidadPeso() >= capacidadRequest.getPesoContenedor() && camion.getCapacidadVolumen() >= capacidadRequest.getVolumenContenedor()){
            capacidadResponse.setCumpleCapacidad(Boolean.TRUE);
        }else {
            capacidadResponse.setCumpleCapacidad(Boolean.FALSE);
        }
        return capacidadResponse;
    }
    @Transactional
    public List<Camion> obtenerCamionesAptos(CapacidadRequest capacidadRequest){
        Double pesoContenedor = capacidadRequest.getPesoContenedor();
        Double volumenContenedor = capacidadRequest.getVolumenContenedor();
        List<Camion> camionesAptos = camionRepository.encontrarDisponibles(pesoContenedor, volumenContenedor);
        if(camionesAptos.isEmpty()){
            log.warn("No se pudo encontrar camiones aptos");
            throw new RuntimeException("Camiones aptos no encontrados");}
        else{
            return camionesAptos;
            }
    }
    @Transactional
    public CostoTrasladoResponse obtenerCostoBaseCamion(String patente){
         Camion camion= camionRepository.findById(patente)
                 .orElseThrow(() -> {
                     log.warn("No se pudo encontrar el camión con la patente " + patente);
                     return new RuntimeException("Camión no encontrado con patente: " + patente);
                 });
         CostoTrasladoResponse costoTrasladoResponse = new CostoTrasladoResponse();
         costoTrasladoResponse.setPatente(camion.getPatente());
         costoTrasladoResponse.setCostoBaseTraslado(camion.getCostoBaseTraslado());
         return costoTrasladoResponse;

    }

    @Transactional
    public ConsumoBaseResponse obtenerConsumoPromedioCamion(String patente){
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> {
                    log.warn("No se pudo encontrar el camión con la patente " + patente);
                    return new RuntimeException("Camión no encontrado con patente: " + patente);
                });
        ConsumoBaseResponse consumoBaseResponse = new ConsumoBaseResponse();
        consumoBaseResponse.setPatente(camion.getPatente());
        consumoBaseResponse.setConsumoBase(camion.getConsCombKm());
        return consumoBaseResponse;
    }

}
