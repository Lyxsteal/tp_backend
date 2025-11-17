#!/bin/bash

# ==============================================================================
# CONFIGURACIÓN
# ==============================================================================
GATEWAY_URL="http://localhost:8083"
KEYCLOAK_URL="http://localhost:8080"
REALM="tp_backend"
CLIENT_ID="tp_backend"
USER="lola"
PASS="Clave123"

echo "======================================================================"
echo "1. OBTENIENDO TOKEN DE KEYCLOAK..."
echo "======================================================================"

# 1. Pedir token y extraer el access_token usando grep/sed (para no depender de jq)
TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/token" \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=$USER" \
 -d "password=$PASS" \
 -d "grant_type=password" \
 -d "client_id=$CLIENT_ID" | grep -o '"access_token":"[^"]*' | grep -o '[^"]*$')

if [ -z "$TOKEN" ]; then
  echo "❌ ERROR: No se pudo obtener el token. Verifica usuario/contraseña en Keycloak."
  exit 1
else
  echo "✅ Token obtenido exitosamente."
fi

# Función helper para hacer curls
# Uso: call_api "METODO" "ENDPOINT" "DATA (opcional)"
call_api() {
  METHOD=$1
  ENDPOINT=$2
  DATA=$3

  echo ""
  echo "➡️  $METHOD $ENDPOINT"
  if [ -z "$DATA" ]; then
    curl -s -X $METHOD "$GATEWAY_URL$ENDPOINT" \
      -H "Authorization: Bearer $TOKEN"
  else
    curl -s -X $METHOD "$GATEWAY_URL$ENDPOINT" \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" \
      -d "$DATA"
  fi
  echo ""
}

echo "======================================================================"
echo "2. PROBANDO MS_SOLICITUD (vía Gateway)"
echo "   Ruta Base: /api/v1/solicitudes"
echo "======================================================================"

# --- SOLICITUDES ---
call_api "GET" "/api/v1/solicitudes"
call_api "GET" "/api/v1/solicitudes/1"
call_api "POST" "/api/v1/solicitudes" '{"numeroContenedor": {"idContenedor": 1}, "dniCliente": {"dni": 100}, "idTarifa": {"idTarifa": 1}, "coordenadasOrigen": -34.6037, "coordenadasDestino": -58.3816}'
# Nota: ID 356 basado en tu último log exitoso, ajusta si es necesario
call_api "PUT" "/api/v1/solicitudes/estados/1" '{"numero": 1, "costoFinal": 1500.0, "tiempoReal": 3600}'

# --- CLIENTES ---
call_api "POST" "/api/v1/solicitudes/clientes" '{"dni": 100, "nombre": "Juan", "apellido": "Perez", "telefono": 123456789, "mail": "juan@example.com"}'

# --- CONTENEDORES ---
call_api "POST" "/api/v1/solicitudes/contenedores" '{"idContenedor": 10, "peso": 500.0, "volumen": 25.0, "estado": "PENDIENTE", "tiempoEstadia": 0}'
call_api "GET" "/api/v1/solicitudes/contenedores/10"

# --- TARIFAS ---
call_api "POST" "/api/v1/solicitudes/tarifas" '{"valorFijoTramo": 100.0, "valorPorVolumen": 5.5, "valorFijoCombustible": 50.0}'
call_api "GET" "/api/v1/solicitudes/tarifas"


echo "======================================================================"
echo "3. PROBANDO MS_RUTAS (vía Gateway)"
echo "   Ruta Base: /api/v1/rutas"
echo "   NOTA: Rutas corregidas según tus Controllers de Java"
echo "======================================================================"

# --- CAMIONES ---
# Ruta Java: @RequestMapping("api/v1/rutas/camiones")
call_api "POST" "/api/v1/rutas/camiones" '{"patente": "ABC1234", "capacidadPeso": 12000, "capacidadVolumen": 60, "disponibilidad": true, "costoBaseTraslado": 550.0, "consCombKm": 0.6}'
call_api "GET" "/api/v1/rutas/camiones/ABC1234"
call_api "GET" "/api/v1/rutas/camiones/costo-base/ABC1234"

# --- CAMIONEROS ---
# Ruta Java: @RequestMapping("api/v1/rutas/camioneros")
call_api "POST" "/api/v1/rutas/camioneros" '{"cedulaCamionero": 200, "nombre": "Pedro", "apellido": "Gomez", "telefono": 987654321}'
call_api "GET" "/api/v1/rutas/camioneros/200"

# --- DEPOSITOS ---
# Ruta Java: @RequestMapping("api/v1/rutas/depositos")
call_api "POST" "/api/v1/rutas/depositos" '{"nombre": "Deposito Norte", "direccion": "Av. Siempre Viva 742", "ubicacion": {"idUbicacion": 11}, "costoEstadia": 80.0}'
call_api "GET" "/api/v1/rutas/depositos"

# --- RUTAS ---
# Ruta Java: @RequestMapping("api/v1/rutas")
call_api "POST" "/api/v1/rutas" '{"cantidadTramos": 2, "cantidadDepositos": 0, "tramos": []}'
# Asumimos ID 1 creado
call_api "GET" "/api/v1/rutas/costos/1"

# --- TRAMOS ---
# Ruta Java: @RequestMapping("api/v1/rutas/tramos")
call_api "POST" "/api/v1/rutas/tramos" '{"nroOrden": 1, "estadoTramo": "PENDIENTE"}'
# Asignar camión (PUT /camion/{idTramo})
# Nota: Asumo ID Tramo 1. El body es solo el string de la patente según tu controller.
call_api "PUT" "/api/v1/rutas/tramos/camion/1" "ABC1234"

# --- UBICACIONES ---
# Ruta Java: @RequestMapping("api/v1/rutas/ubicaciones")
call_api "POST" "/api/v1/rutas/ubicaciones" '{"direccion": "Punto A", "latitud": -34.55, "longitud": -58.45}'
call_api "GET" "/api/v1/rutas/ubicaciones/1"

echo "======================================================================"
echo "FIN DE LAS PRUEBAS"
echo "=====