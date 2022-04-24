up-kafka:
	docker-compose up -d

topics:
	docker exec -t broker kafka-topics --bootstrap-server :9092 --create --topic create-order --partitions 2 --replication-factor 1
	docker exec -t broker kafka-topics --bootstrap-server :9092 --create --topic complete-order --partitions 2 --replication-factor 1

server:
	sbt "project darkServer" run

streaming:
	sbt "project darkStreaming" run

restaurants:
	./scripts/restaurants.sh

create-orders:
	./scripts/create-order.sh

complete-orders:
	./scripts/complete-order.sh

stop:
	docker-compose stop

#get-completed-orders:
#	curl -X GET localhost:8080/completed-orders -H 'Content-Type: application/json' | jq .
#
#get-created-orders:
#	curl -X GET localhost:8080/created-orders -H 'Content-Type: application/json' | jq .