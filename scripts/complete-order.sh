curl -X POST localhost:8080/complete-order -H 'Content-Type: application/json' -d '{"id": 1, "timestamp": '$(date +%s000)'}' | jq .
curl -X POST localhost:8080/complete-order -H 'Content-Type: application/json' -d '{"id": 2, "timestamp": '$(date +%s000)'}' | jq .
curl -X POST localhost:8080/complete-order -H 'Content-Type: application/json' -d '{"id": 3, "timestamp": '$(date +%s000)'}' | jq .
curl -X POST localhost:8080/complete-order -H 'Content-Type: application/json' -d '{"id": 4, "timestamp": '$(date +%s000)'}' | jq .
curl -X POST localhost:8080/complete-order -H 'Content-Type: application/json' -d '{"id": 5, "timestamp": '$(date +%s000)'}' | jq .
curl -X POST localhost:8080/complete-order -H 'Content-Type: application/json' -d '{"id": 6, "timestamp": '$(date +%s000)'}' | jq .
