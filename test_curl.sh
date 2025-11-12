echo "=== Test 1: GET solicitudes (debería devolver lista o 200/empty)==="
curl -i -X GET http://localhost:8083/api/v1/solicitudes

echo ""
echo "=== Test 2: POST solicitud (crear una nueva)==="
curl -i -X POST http://localhost:8083/api/v1/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "destino": "Buenos Aires",
    "fecha": "2025-11-12"
  }'

echo ""
echo "=== Test 3: GET solicitud por ID (por ejemplo ID=1)==="
curl -i -X GET http://localhost:8083/api/v1/solicitudes/1