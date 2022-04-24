curl -X POST localhost:8080/create-restaurants -H 'Content-Type: application/json' -d '{"id": "royalKebabMontpellier", "latitude": 0, "longitude": 0}' | jq .
curl -X POST localhost:8080/create-restaurants -H 'Content-Type: application/json' -d '{"id": "McdoMontpellier", "latitude": 0.1, "longitude": 0}' | jq .
curl -X POST localhost:8080/create-restaurants -H 'Content-Type: application/json' -d '{"id": "McdoParis", "latitude": 20, "longitude": 20}' | jq .
