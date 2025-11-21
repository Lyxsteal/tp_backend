package com.example.ms_solicitud.model;

public enum EstadoSolicitud {
    CREADA,       // Sin ruta asignada
    APROBADA,     // Ruta asignada, todos los atributos seteados
    EN_CURSO,     // Camión andando
    FINALIZADA,   // Llegó a destino
}
