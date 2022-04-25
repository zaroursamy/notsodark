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

batch:
	export JAVA_HOME=/usr/bin/java && sbt "project darkBatch" run

stop:
	docker-compose stop