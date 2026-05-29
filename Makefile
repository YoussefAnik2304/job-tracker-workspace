.PHONY: db-up db-down db-status test-backend build-backend run-backend

db-up:
	docker compose up -d

db-down:
	docker compose down

db-status:
	docker ps

test-backend:
	cd backend && ./mvnw test

build-backend:
	cd backend && ./mvnw clean package -DskipTests

run-backend:
	cd backend && ./mvnw spring-boot:run