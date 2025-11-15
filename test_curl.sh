echo "=== Test 1: GET solicitudes (debería devolver lista o 200/empty)==="
curl -i -X GET http://localhost:8082/api/v1/solicitudes

echo ""
echo "=== Test 2: POST solicitud (crear una nueva)==="
curl -i -X POST http://localhost:8082/api/v1/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "destino": "Buenos Aires",
    "fecha": "2025-11-12"
  }'

echo ""
echo "=== Test 3: GET solicitud por ID (por ejemplo ID=1)==="
curl -i -X GET http://localhost:8083/api/v1/solicitudes


curl --location --request POST 'http://localhost:8080/realms/tp_backend/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code=120c5a3e-9ab7-4e0c-9493-fc100c1f4c5a.1a9df3a2-6444-4269-abc6-b6674e8185b6.0483e72b-957e-476a-ad7b-7e4555f123cd' \
--data-urlencode 'client_id=tp_backend_client' \
--data-urlencode 'redirect_uri=http://localhost:8083/api/login/oauth2/code/keycloak' \

echo "Metodo verificar token (whoAmI)"
curl -i -H "Authorization: Bearer 120c5a3e-9ab7-4e0c-9493-fc100c1f4c5a.1a9df3a2-6444-4269-abc6-b6674e8185b6.0483e72b-957e-476a-ad7b-7e4555f123cd" http://localhost:8083/gateway/whoami