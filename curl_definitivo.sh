# --- MS_SOLICITUD (http://localhost:8081) ---
echo "========================================="
echo "Probando ms_solicitud en http://localhost:8081"
echo "========================================="

# --- SolicitudController ---
echo "[SolicitudController]"
echo "GET DE TODAS LAS SOLICITUDES"
curl "http://localhost:8081/api/v1/solicitudes"
echo "PROBANDO FIND BY ID"
curl "http://localhost:8081/api/v1/solicitudes/1"
echo "GET DE TODAS LAS SOLICITUDES DE UN CLIENTE (ID:1)"
curl "http://localhost:8081/api/v1/solicitudes/solicitud-por-cliente/1"
echo "GET COSTO FINAL DE UNA SOLICITUD(ID:1)"
curl "http://localhost:8081/api/v1/solicitudes/costo-final/1"
echo "POST SOLICITUD"
curl -X POST -H "Content-Type: application/json" -d '{"numeroContenedor": {"idContenedor": 1}, "dniCliente": {"dni": 100}, "idTarifa": {"idTarifa": 1}, "coordenadasOrigen": -34.6037, "coordenadasDestino": -58.3816}' "http://localhost:8081/api/v1/solicitudes"
echo "PUT SOLICITUD"
curl -X PUT -H "Content-Type: application/json" -d '{"numero": 1, "costoFinal": 1500.0, "tiempoReal": 3600}' "http://localhost:8081/api/v1/solicitudes/estados/1"
echo "ACTUALIZAR RUTA A UNA SOLICITUD"
curl -X PUT -H "Content-Type: application/json" -d '1' "http://localhost:8081/api/v1/solicitudes/asignar-ruta/1"
echo "ACTUALIZAR TARIFA A UNA SOLICITUD"
curl -X PUT -H "Content-Type: application/json" -d '{"idTarifa": 1, "valorFijoTramo": 110.0, "valorPorVolumen": 6.0, "valorFijoCombustible": 55.0, "valorPorEstadia": 10.0}' "http://localhost:8081/api/v1/solicitudes/asignar-tarifa/1"
echo ""


# --- ClienteController ---
echo "[ClienteController]"
# NOTA: El método 'crearCliente' en ClienteController NO tiene @PostMapping. Asumo que es un POST.
curl -X POST -H "Content-Type: application/json" -d '{"dni": 100, "nombre": "Juan", "apellido": "Perez", "telefono": 123456789, "mail": "juan@example.com"}' "http://localhost:8081/api/v1/solicitudes/clientes"
echo ""


# --- ContenedorController ---
echo "[ContenedorController]"
echo "GET DEL CONTENEDOR 1"
curl "http://localhost:8081/api/v1/solicitudes/contenedores/10"
echo "OBTENER ESTADO DEL CONTENEDOR 1"
curl "http://localhost:8081/api/v1/solicitudes/contenedores/estado/1"
echo "OBTIENE LOS CONTENEDORES EN ESTADO PENDIENTE"
curl "http://localhost:8081/api/v1/solicitudes/contenedores/pendientes"
echo "CREAR CONTENEDOR 10"
curl -X POST -H "Content-Type: application/json" -d '{"idContenedor": 10, "peso": 500.0, "volumen": 25.0, "estado": "PENDIENTE", "tiempoEstadia": 0}' "http://localhost:8081/api/v1/solicitudes/contenedores"
echo "ACTUALIZAR CONTENEDOR 1"
curl -X PUT -H "Content-Type: application/json" -d '{"idContenedor": 10, "peso": 550.0, "volumen": 25.0, "estado": "ASIGNADO", "tiempoEstadia": 0}' "http://localhost:8081/api/v1/solicitudes/contenedores/10"
echo "DELETE DEL CONTENEDOR 1"
curl -X DELETE "http://localhost:8081/api/v1/solicitudes/contenedores/10"
echo ""


# --- TarifaController ---
echo "[TarifaController]"
echo "GET DE LAS TARIFAS"
curl "http://localhost:8081/api/v1/solicitudes/tarifas"
echo "GET TARIFA (ID:1)"
curl "http://localhost:8081/api/v1/solicitudes/tarifas/1"
echo "CREAR TARIFA"
curl -X POST -H "Content-Type: application/json" -d '{"valorFijoTramo": 100.0, "valorPorVolumen": 5.5, "valorFijoCombustible": 50.0, "valorPorEstadia": 10.0}' "http://localhost:8081/api/v1/solicitudes/tarifas"
echo "ACTUALIZAR TARIFA"
curl -X PUT -H "Content-Type: application/json" -d '{"idTarifa": 1, "valorFijoTramo": 110.0, "valorPorVolumen": 6.0, "valorFijoCombustible": 55.0, "valorPorEstadia": 10.0}' "http://localhost:8081/api/v1/solicitudes/tarifas/1"
echo "ELIMINAR TARIFA"
curl -X DELETE "http://localhost:8081/api/v1/solicitudes/tarifas/1"
echo ""


# --- MS_RUTAS (http://localhost:8085) ---
echo "========================================="
echo "Probando ms_rutas en http://localhost:8085"
echo "========================================="

# --- CamionController ---
echo "[CamionController]"
curl "http://localhost:8085/api/v1/rutas/camiones/ABC123"
echo ""
# NOTA: Un GET con RequestBody es inusual y no es una buena práctica, pero así está definido.
curl -X GET -H "Content-Type: application/json" -d '{"pesoContenedor": 500.0, "volumenContenedor": 25.0}' "http://localhost:8085/api/v1/camiones/capacidad-maxima/ABC123"
echo ""
curl -X GET -H "Content-Type: application/json" -d '{"pesoContenedor": 500.0, "volumenContenedor": 25.0}' "http://localhost:8085/api/v1/camiones/camiones-aptos"
echo ""
curl "http://localhost:8085/api/v1/rutas/camiones/costo-base/ABC123"
echo ""
curl "http://localhost:8085/api/v1/rutas/camiones/consumo-prom/ABC123"
echo ""
curl -X POST -H "Content-Type: application/json" -d '{"patente": "XYZ789", "camionero": {"cedulaCamionero": 201}, "capacidadPeso": 12000, "capacidadVolumen": 60, "disponibilidad": true, "costoBaseTraslado": 550.0, "consCombKm": 0.6}' "http://localhost:8085/api/v1/rutas/camiones"
echo ""
curl -X PUT -H "Content-Type: application/json" -d '{"patente": "ABC123", "camionero": {"cedulaCamionero": 200}, "capacidadPeso": 10000, "capacidadVolumen": 50, "disponibilidad": true, "costoBaseTraslado": 500.0, "consCombKm": 0.5}' "http://localhost:8085/api/v1/rutas/camiones/ABC123"
echo ""
curl -X DELETE "http://localhost:8085/api/v1/rutas/camiones/ABC123"
echo ""


# --- CamioneroController ---
echo "[CamioneroController]"
curl "http://localhost:8085/api/v1/rutas/camioneros/200"
echo ""
curl -X POST -H "Content-Type: application/json" -d '{"cedulaCamionero": 200, "idUsuario": "a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890", "nombre": "Pedro", "apellido": "Gomez", "telefono": 987654321}' "http://localhost:8085/api/v1/rutas/camioneros"
echo ""
# NOTA: El @PutMapping no tiene "/{cedula}". Asumo que debería ser "api/v1/camioneros/200".
curl -X PUT -H "Content-Type: application/json" -d '{"cedulaCamionero": 200, "telefono": 111222333}' "http://localhost:8085/api/v1/rutas/camioneros/200"
echo ""
# NOTA: El @DeleteMapping no tiene "/{cedula}". Asumo que debería ser "api/v1/camioneros/200".
curl -X DELETE "http://localhost:8085/api/v1/rutas/camioneros/200"
echo ""


# --- DepositoController ---
echo "[DepositoController]"
curl "http://localhost:8085/api/v1/rutas/depositos"
echo ""
curl -X POST -H "Content-Type: application/json" -d '{"nombre": "Deposito Norte", "direccion": "Av. Siempre Viva 742", "ubicacion": {"idUbicacion": 11, "direccion": "Av. Siempre Viva 742", "latitud": -34.5, "longitud": -58.5}, "costoEstadia": 80.0}' "http://localhost:8085/api/v1/rutas/depositos"
echo ""
curl -X PUT -H "Content-Type: application/json" -d '{"id": 1, "nombre": "Deposito Central", "direccion": "Calle Falsa 123", "ubicacion": {"idUbicacion": 10}, "costoEstadia": 75.0}' "http://localhost:8085/api/v1/rutas/depositos/1"
echo ""
# NOTA: El @DeleteMapping no tiene "/{idDeposito}". Asumo que debería ser "api/v1/depositos/1".
curl -X DELETE "http://localhost:8085/api/v1/rutas/depositos/1"
echo ""


# --- RutaController ---
echo "[RutaController]"
echo "GET COSTO DE RUTA 1"
curl "http://localhost:8085/api/v1/rutas/costos/1"
echo "POST DE RUTA"
curl -X POST -H "Content-Type: application/json" -d '{"cantidadTramos": 2, "cantidadDepositos": 0, "tramos": []}' "http://localhost:8085/api/v1/rutas"
echo "ACTUALIZAR RUTA 1"
curl -X PUT -H "Content-Type: application/json" -d '{"idRuta": 1, "cantidadTramos": 3, "cantidadDepositos": 1}' "http://localhost:8085/api/v1/rutas/1"
echo "ASIGNAR TRAMO A UNA RUTA"
curl -X PUT -H "Content-Type: application/json" -d '[{"nroOrden": 1, "ubicacionOrigen": {"idUbicacion": 10}, "ubicacionDestino": {"idUbicacion": 11}, "tipoTramo": {"idTipoTramo": 1}, "estadoTramo": "PENDIENTE"}]' "http://localhost:8085/api/v1/rutas/asignacion-tramos/1"
echo "ELIMINAR RUTA 1"
# NOTA: El @DeleteMapping no tiene "/{id}". Asumo que debería ser "api/v1/rutas/1".
curl -X DELETE "http://localhost:8085/api/v1/rutas/1"
echo ""


# --- TramoController ---
echo "[TramoController]"
echo "GET TRAMOS ASIGNADOS DEL CAMIONERO CEDULA 200"
curl "http://localhost:8085/api/v1/rutas/tramos/tramos-asignados/200"
echo "POST DE UN TRAMO"
curl -X POST -H "Content-Type: application/json" -d '{"ruta": {"idRuta": 1}, "nroOrden": 2, "ubicacionOrigen": {"idUbicacion": 1}, "ubicacionDestino": {"idUbicacion": 2}, "tipoTramo": {"idTipoTramo": 1}, "estadoTramo": "PENDIENTE"}' "http://localhost:8085/api/v1/rutas/tramos"
echo "ACTUALIZAR ESTADO DE UN TRAMO"
curl -X PUT -H "Content-Type: application/json" -d '"EN_CURSO"' "http://localhost:8085/api/v1/rutas/tramos/1"
echo "ACTUALIZAR TRAMO "
# NOTA: Hay un @PutMapping sin path. Asumo que es "api/v1/tramos/1".
curl -X PUT -H "Content-Type: application/json" -d '{"idTramo": 1, "nroOrden": 1, "estadoTramo": "COMPLETADO"}' "http://localhost:8085/api/v1/rutas/tramos/1"
echo "ASIGNAR CAMION A TRAMO"
curl -X PUT -H "Content-Type: application/json" -d '"XYZ789"' "http://localhost:8085/api/v1/rutas/tramos/camion/ABC123"
echo "ELIMINAR TRAMO 1"
# NOTA: El @DeleteMapping no tiene "/{id}". Asumo que debería ser "api/v1/tramos/1".
curl -X DELETE "http://localhost:8085/api/v1/rutas/tramos/1"
echo ""


# --- UbicacionController ---
echo "[UbicacionController]"
echo "GET UNA UBICACION"
curl "http://localhost:8085/api/v1/rutas/ubicaciones/1"
curl "http://localhost:8085/api/v1/rutas/ubicaciones/2"
echo "CREAR UBICACION"
curl -X POST -H "Content-Type: application/json" -d '{"direccion": "Punto de Carga A", "latitud": -34.55, "longitud": -58.45}' "http://localhost:8085/api/v1/rutas/ubicaciones"
echo "ACTUALIZAR UBICACION"
curl -X PUT -H "Content-Type: application/json" -d '{"idUbicacion": 10, "direccion": "Punto de Carga A (Corregido)", "latitud": -34.551, "longitud": -58.451}' "http://localhost:8085/api/v1/rutas/ubicacion/10"
echo "ELIMINAR UBICACION"
# NOTA: El @DeleteMapping no tiene "/{id}". Asumo que debería ser "api/v1/ubicacion/10".
curl -X DELETE "http://localhost:8085/api/v1/rutas/ubicacion/10"
echo ""

echo "--- 1. POST: CREAR TIPO TRAMO (ID 99) ---"
curl -i -X POST "http://localhost:8085/api/v1/rutas/tipo-tramos" -H "Content-Type: application/json" -d '{"idTipoTramo": 1, "nombreTipoTramo": "ORIGEN-DEPOSITO"}'

echo "========================================="
echo "Pruebas finalizadas."
echo "========================================="