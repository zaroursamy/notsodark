curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 1, "userId": "samy", "latitude": 0, "longitude": 0, "timestamp": '$(date +%s)', "menuId": "kebab", "price": 8}'
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 1, "userId": "samy", "latitude": 0, "longitude": 0, "timestamp": '$(eat)', "menuId": "kebab", "price": 8}' | jq .
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 2, "userId": "samia", "latitude": 0, "longitude": 0, "timestamp": '$(date +%s)', "menuId": "burger", "price": 12}' | jq .
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 3, "userId": "samy", "latitude": 0, "longitude": 0, "timestamp": '$(date +%s)', "menuId": "couscous", "price": 10}' | jq .
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 4, "userId": "isaac", "latitude": 0, "longitude": 0, "timestamp": '$(date +%s)', "menuId": "couscous", "price": 8}' | jq .
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 5, "userId": "therock", "latitude": 19, "longitude": 20, "timestamp": '$(date +%s)', "menuId": "couscous", "price": 12}' | jq .
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 6, "userId": "therock", "latitude": 19, "longitude": 20, "timestamp": '$(date +%s)', "menuId": "tajine", "price": 10}' | jq .
curl -X POST localhost:8080/create-order -H 'Content-Type: application/json' -d '{"id": 7, "userId": "bobby", "latitude": 200, "longitude": 200, "timestamp": '$(date +%s)', "menuId": "veganBowl", "price": 100}' | jq .
