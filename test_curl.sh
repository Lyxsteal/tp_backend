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

curl -i -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoNGlDWjhDZjh5VS1pbVg2cEhDVC1kbE1jcF85cTFiR01ucU1XTTZ1Yk1vIn0.eyJleHAiOjE3NjMzMzUxNTUsImlhdCI6MTc2MzMzNDg1NSwianRpIjoiNzJhMWRjMDQtODU5NS00MzIwLWJhZmYtMjc0YmQwNTM2MGI2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy90cF9iYWNrZW5kIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6Ijk3YmM2NDk3LTE2YTktNDAxZS1iOWRhLTliZmY0MjMyM2MxYSIsInR5cCI6IkJlYXJlciIsImF6cCI6InRwX2JhY2tlbmQiLCJzZXNzaW9uX3N0YXRlIjoiNzQwYjI1YmItYzdmNy00YTRjLWEzMjAtMTI2NWRlNzdlOGU0IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODMiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy10cF9iYWNrZW5kIiwiYWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6Ijc0MGIyNWJiLWM3ZjctNGE0Yy1hMzIwLTEyNjVkZTc3ZThlNCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkxvbGEgQmF6YW4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJsb2xhIiwiZ2l2ZW5fbmFtZSI6IkxvbGEiLCJmYW1pbHlfbmFtZSI6IkJhemFuIiwiZW1haWwiOiJsb2xhZ2F0YUBnbWFpbC5jb20ifQ.CEyyFXLeozN2ZRDD6omwT6P8kJ13hQyNNo2yMRpjtVieWzo0zON6-zmu3eu1BEt3BdyrXmkOHh25AmYT1FmQYdnbleJG8kUTbXy8PcPhmflgKGESx6VijWtZlE7tDzIcDMgWIW_5i-fDCZM_AclTc_uxB_P8cPMHHaRcHFyFSpMyBDHd1yjGvqcAdSl5TtO0jw0uA8vKpamyyw8czDJtj-OsLOid12hZax023CAy0aAcWZdEc24OqngHJ8khP7ouxlTmuXi1PATijE1AjxGdmWSEVYujo9xxYcEOfthVEgc9nqMi4pA8sTmrk0mv0EKByLzeEVBsGAOHE9Jnvgj9Jg" http://localhost:8083/api/v1/solicitudes


