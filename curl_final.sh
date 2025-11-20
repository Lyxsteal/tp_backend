#!/bin/bash

# ==============================================================================
# CONFIGURACIÓN DEL ENTORNO
# ==============================================================================
GATEWAY_URL="http://localhost:8083"
KEYCLOAK_URL="http://localhost:8080"
REALM="tp_backend"
CLIENT_ID="tp_backend"
USER="lola"           # Usuario configurado en curl_gateway.sh
PASS="Clave123"

echo "======================================================================"
echo "1. AUTENTICACIÓN (Obteniendo Token)"
echo "======================================================================"

# Solicitud de token a Keycloak
TOKEN_RESPONSE=$(curl -s -X POST "$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=$USER" \
  -d "password=$PASS" \
  -d "grant_type=password" \
  -d "client_id=$CLIENT_ID")

# Extraer el access_token (usando grep/sed para compatibilidad básica)
TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"access_token":"[^"]*' | grep -o '[^"]*$')

if [ -z "$TOKEN" ]; then
  echo "❌ Error: No se pudo obtener el token. Respuesta de Keycloak:"
  echo "$TOKEN_RESPONSE"
  exit 1
else
  echo "✅ Token obtenido correctamente."
fi

# Función para ejecutar llamadas curl
# Uso: exec_curl "METODO" "RUTA_RELATIVA" "JSON_BODY (Opcional)"
exec_curl() {
  METHOD=$1
  ENDPOINT=$2
  BODY=$3

  echo ""
  echo "➡️  $METHOD $ENDPOINT"

  if [ -z "$BODY" ]; then
    curl -s -X $METHOD "$GATEWAY_URL$ENDPOINT" \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json"
  else
    curl -s -X $METHOD "$GATEWAY_URL$ENDPOINT" \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" \
      -d "$BODY"
  fi
  echo ""
}

echo "======================================================================"
echo " MICROSERVICIO SOLICITUD (ms_solicitud) - Puerto 8081 via Gateway"
echo "======================================================================"

# --- 1. CLIENTES (ClienteController) ---
echo "--- [Clientes] ---"
# POST: Crear Cliente
exec_curl "POST" "/api/v1/solicitudes/clientes" '{"dni": 12345678, "nombre": "Juan", "apellido": "Perez", "telefono": "555-1234", "mail": "juan.perez@email.com"}'
# GET: Todos los clientes
exec_curl "GET" "/api/v1/solicitudes/clientes"
# GET: Cliente por DNI
exec_curl "GET" "/api/v1/solicitudes/clientes/12345678"
# PUT: Actualizar Cliente
exec_curl "PUT" "/api/v1/solicitudes/clientes/12345678" '{"dni": 12345678, "nombre": "Juan Ernesto", "apellido": "Perez", "telefono": "555-9999", "mail": "juan.26@email.com"}'
# DELETE: Eliminar Cliente
exec_curl "DELETE" "/api/v1/solicitudes/clientes/12345678"


# --- 2. CONTENEDORES (ContenedorController) ---
echo "--- [Contenedores] ---"
# POST: Crear Contenedor
exec_curl "POST" "/api/v1/solicitudes/contenedores" '{"idContenedor": 100, "peso": 500.0, "volumen": 30.0, "estado": "PENDIENTE", "tiempoEstadia": 0}'
# GET: Todos los contenedores
exec_curl "GET" "/api/v1/solicitudes/contenedores"
# GET: Contenedor por ID
exec_curl "GET" "/api/v1/solicitudes/contenedores/100"
# GET: Estado del contenedor
exec_curl "GET" "/api/v1/solicitudes/contenedores/estado/100"
# GET: Contenedores pendientes
exec_curl "GET" "/api/v1/solicitudes/contenedores/pendientes"
# PUT: Actualizar Contenedor
exec_curl "PUT" "/api/v1/solicitudes/contenedores/100" '{"idContenedor": 100, "peso": 550.0, "volumen": 30.0, "estado": "EN_PROCESO", "tiempoEstadia": 2}'
# DELETE: Eliminar Contenedor
exec_curl "DELETE" "/api/v1/solicitudes/contenedores/100"


# --- 3. TARIFAS (TarifaController) ---
echo "--- [Tarifas] ---"
# POST: Crear Tarifa
exec_curl "POST" "/api/v1/solicitudes/tarifas" '{"valorFijoTramo": 100.0, "valorPorVolumen": 10.0, "valorFijoCombustible": 50.0, "valorPorEstadia": 20.0}'
# GET: Todas las tarifas
exec_curl "GET" "/api/v1/solicitudes/tarifas"
# Asumimos ID 1 (o el ID que retorne la BD, ajustar según respuesta anterior)
# GET: Tarifa por ID
exec_curl "GET" "/api/v1/solicitudes/tarifas/1"
# PUT: Actualizar Tarifa
exec_curl "PUT" "/api/v1/solicitudes/tarifas/1" '{"idTarifa": 1, "valorFijoTramo": 120.0, "valorPorVolumen": 12.0, "valorFijoCombustible": 60.0, "valorPorEstadia": 25.0}'
# DELETE: Eliminar Tarifa
exec_curl "DELETE" "/api/v1/solicitudes/tarifas/1"


# --- 4. SOLICITUDES (SolicitudController) ---
echo "--- [Solicitudes] ---"
# Nota: Para crear una solicitud necesitamos un DNI cliente y objetos anidados.
# POST: Crear Solicitud (SolicitudDto)
exec_curl "POST" "/api/v1/solicitudes" '{
  "cliente": {"dni": 87654321, "nombre": "Maria", "apellido": "Lopez", "telefono": "111-222", "mail": "maria@mail.com"},
  "contenedor": {"idContenedor": 200, "peso": 1000.0, "volumen": 50.0, "estado": "PENDIENTE", "tiempoEstadia": 0},
  "coordenadasOrigen": -34.6037,
  "coordenadasDestino": -58.3816
}'

# GET: Todas las solicitudes
exec_curl "GET" "/api/v1/solicitudes"

# Asumiendo ID de solicitud generado es 1
# GET: Solicitud por ID
exec_curl "GET" "/api/v1/solicitudes/1"

# GET: Solicitudes por Cliente
exec_curl "GET" "/api/v1/solicitudes/solicitud-por-cliente/87654321"

# GET: Costo Final
exec_curl "GET" "/api/v1/solicitudes/costo-final/1"

# PUT: Actualizar Estado (String body)
exec_curl "PUT" "/api/v1/solicitudes/estados/1" '"COMPLETADO"'

# PUT: Asignar Ruta (Integer body)
exec_curl "PUT" "/api/v1/solicitudes/asignar-ruta/1" '1'

# PUT: Asignar Tarifa (Tarifa Object body)
exec_curl "PUT" "/api/v1/solicitudes/asignar-tarifa/1" '{"idTarifa": 1, "valorFijoTramo": 100.0, "valorPorVolumen": 10.0, "valorFijoCombustible": 50.0, "valorPorEstadia": 20.0}'


echo "======================================================================"
echo " MICROSERVICIO RUTAS (ms_rutas) - Puerto 8085 via Gateway"
echo "======================================================================"

# --- 5. UBICACIONES (UbicacionController) ---
echo "--- [Ubicaciones] ---"
# POST: Crear Ubicacion
exec_curl "POST" "/api/v1/rutas/ubicaciones" '{"direccion": "Puerto Madero", "latitud": -34.61, "longitud": -58.36}'
# GET: Todas las ubicaciones
exec_curl "GET" "/api/v1/rutas/ubicaciones"
# Asumiendo ID 1
# GET: Ubicacion por ID
exec_curl "GET" "/api/v1/rutas/ubicaciones/1"
# PUT: Actualizar Ubicacion
exec_curl "PUT" "/api/v1/rutas/ubicaciones/1" '{"idUbicacion": 1, "direccion": "Puerto Madero Norte", "latitud": -34.62, "longitud": -58.37}'
# DELETE: Eliminar Ubicacion
exec_curl "DELETE" "/api/v1/rutas/ubicaciones/1"


# --- 6. TIPO TRAMO (TipoTramoController) ---
echo "--- [Tipo Tramo] ---"
# POST: Crear Tipo Tramo
exec_curl "POST" "/api/v1/rutas/tipo-tramos" '{"nombreTipoTramo": "ORIGEN-DESTINO"}'
# GET: Todos
exec_curl "GET" "/api/v1/rutas/tipo-tramos"
# GET: Por ID (Asumiendo 1)
exec_curl "GET" "/api/v1/rutas/tipo-tramos/1"
# PUT: Actualizar
exec_curl "PUT" "/api/v1/rutas/tipo-tramos/1" '{"idTipoTramo": 1, "nombreTipoTramo": "ORIGEN-DEPOSITO"}'
# DELETE
exec_curl "DELETE" "/api/v1/rutas/tipo-tramos/1"


# --- 7. CAMIONEROS (CamioneroController) ---
echo "--- [Camioneros] ---"
# POST: Crear Camionero
exec_curl "POST" "/api/v1/rutas/camioneros" '{"cedulaCamionero": 300, "nombre": "Roberto", "apellido": "Carlos", "telefono": "999-888"}'
# GET: Todos
exec_curl "GET" "/api/v1/rutas/camioneros"
# GET: Por Cedula
exec_curl "GET" "/api/v1/rutas/camioneros/300"
# PUT: Actualizar
exec_curl "PUT" "/api/v1/rutas/camioneros/300" '{"cedulaCamionero": 300, "nombre": "Roberto", "apellido": "Gomez", "telefono": "111-000"}'
# DELETE
exec_curl "DELETE" "/api/v1/rutas/camioneros/300"


# --- 8. CAMIONES (CamionController) ---
echo "--- [Camiones] ---"
# Requisito: Crear un camionero primero para asignarlo (usaremos cedula 300 si no se borró, o crea uno nuevo)
exec_curl "POST" "/api/v1/rutas/camioneros" '{"cedulaCamionero": 400, "nombre": "Luis", "apellido": "Suarez"}'
# POST: Crear Camion (CamionDto)
exec_curl "POST" "/api/v1/rutas/camiones" '{"patente": "AA123BB", "idCamionero": 400, "capacidadPeso": 20000, "capacidadVolumen": 80, "costoBaseTranslado": 1500.0, "consCombKm": 0.8}'
# GET: Todos
exec_curl "GET" "/api/v1/rutas/camiones"
# GET: Por Patente
exec_curl "GET" "/api/v1/rutas/camiones/AA123BB"
# GET: Capacidad Maxima (Query params)
exec_curl "GET" "/api/v1/rutas/camiones/capacidad-maxima/AA123BB?pesoContenedor=1000&volumenContenedor=50"
# GET: Camiones Aptos (Query params)
exec_curl "GET" "/api/v1/rutas/camiones/camiones-aptos?pesoContenedor=1000&volumenContenedor=50"
# GET: Costo Base
exec_curl "GET" "/api/v1/rutas/camiones/costo-base/AA123BB"
# GET: Consumo Promedio
exec_curl "GET" "/api/v1/rutas/camiones/consumo-prom/AA123BB"
# PUT: Actualizar Camion
exec_curl "PUT" "/api/v1/rutas/camiones/AA123BB" '{"patente": "AA123BB", "capacidadPeso": 22000, "capacidadVolumen": 80, "disponibilidad": true, "costoBaseTraslado": 1600.0, "consCombKm": 0.9}'
# DELETE
exec_curl "DELETE" "/api/v1/rutas/camiones/AA123BB"


# --- 9. DEPOSITOS (DepositoController) ---
echo "--- [Depositos] ---"
# POST: Crear Deposito
exec_curl "POST" "/api/v1/rutas/depositos" '{"nombre": "Deposito Central", "direccion": "Ruta 2 km 50", "ubicacion": {"direccion": "Ruta 2", "latitud": -35.0, "longitud": -58.0}}'
# GET: Todos
exec_curl "GET" "/api/v1/rutas/depositos"
# Asumiendo ID 1
# GET: Por ID
exec_curl "GET" "/api/v1/rutas/depositos/1"
# PUT: Actualizar
exec_curl "PUT" "/api/v1/rutas/depositos/1" '{"id": 1, "nombre": "Deposito Sur", "direccion": "Ruta 2 km 60"}'
# DELETE
exec_curl "DELETE" "/api/v1/rutas/depositos/1"


# --- 10. TRAMOS (TramoController) ---
echo "--- [Tramos] ---"
# Requisitos: Ubicaciones IDs y TipoTramo ID.
# POST: Crear Tramo (TramoDto)
exec_curl "POST" "/api/v1/rutas/tramos" '{"ubicacionOrigenId": 1, "ubicacionDestinoId": 1, "tipoTramo": 1, "estadoTramo": "PENDIENTE"}'
# GET: Todos
exec_curl "GET" "/api/v1/rutas/tramos"
# Asumiendo ID 1
# GET: Por ID Tipo (Nombre variable confuso en controller, pero busca por ID tramo)
exec_curl "GET" "/api/v1/rutas/tramos/1"
# PUT: Asignar Camion (String body con patente)
exec_curl "PUT" "/api/v1/rutas/tramos/camion/1" "AA123BB"
# PUT: Actualizar Estado (String body)
exec_curl "PUT" "/api/v1/rutas/tramos/estado/1" "EN_VIAJE"
# GET: Tramos asignados a camionero (por cedula)
exec_curl "GET" "/api/v1/rutas/tramos/tramos-asignados/400"
# PUT: Actualizar Tramo completo
exec_curl "PUT" "/api/v1/rutas/tramos/1" '{"nroOrden": 2, "estadoTramo": "FINALIZADO"}'
# DELETE
exec_curl "DELETE" "/api/v1/rutas/tramos/1"


# --- 11. RUTAS (RutaController) ---
echo "--- [Rutas] ---"
# POST: Crear Ruta
exec_curl "POST" "/api/v1/rutas" '{"cantidadTramos": 0, "cantidadDepositos": 0, "tramos": []}'
# GET: Todas
exec_curl "GET" "/api/v1/rutas"
# Asumiendo ID 1
# GET: Por ID
exec_curl "GET" "/api/v1/rutas/1"
# GET: Costos
exec_curl "GET" "/api/v1/rutas/costos/1"
# POST: Rutas Tentativas (Lista de Rutas)
exec_curl "POST" "/api/v1/rutas/rutas-tentativas" '[{"cantidadTramos":1}]'
# PUT: Actualizar Ruta
exec_curl "PUT" "/api/v1/rutas/1" '{"idRuta": 1, "cantidadTramos": 5, "cantidadDepositos": 2}'
# PUT: Asignar Tramos (Lista de Tramos)
exec_curl "PUT" "/api/v1/rutas/asignacion-tramos/1" '[{"idTramo": 1}]'
# DELETE
exec_curl "DELETE" "/api/v1/rutas/1"


echo "======================================================================"
echo " EXTRA (Gateway)"
echo "======================================================================"
exec_curl "GET" "/gateway/whoami"

echo "Pruebas finalizadas."