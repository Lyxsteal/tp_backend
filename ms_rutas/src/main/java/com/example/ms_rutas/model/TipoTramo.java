package com.example.ms_rutas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;



public enum TipoTramo {
    ORIGEN_DESTINO,
    ORIGEN_DEPOSITO,
    DEPOSITO_DESTINO,
    DEPOSITO_DEPOSITO
}
