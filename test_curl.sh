echo "=== Test 1: GET solicitudes (debería devolver lista o 200/empty)==="
curl -i -X GET http://localhost:8081/api/v1/solicitudes

echo ""
echo "=== Test 2: POST solicitud (crear una nueva)==="
curl -i -X POST http://localhost:8081/api/v1/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "destino": "Buenos Aires",
    "fecha": "2025-11-12"
  }'

echo ""
echo "=== Test 3: GET solicitud via GATEWAY==="
curl -i -X GET http://localhost:8083/api/v1/solicitudes
echo "=== Test 4: CODE ==="
curl -X POST "http://localhost:8080/realms/tp_backend/protocol/openid-connect/token" \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=lola" \
 -d "password=Clave123" \
 -d "grant_type=password" \
 -d "client_id=tp_backend" \

