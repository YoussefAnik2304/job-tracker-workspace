.PHONY: db-up db-down db-status test-backend build-backend run-backend start-backend stop-backend generate-docs

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
	-make stop-backend
	cd backend && ./mvnw spring-boot:run

# Starts the application in the background (frees terminal immediately)
start-backend:
	-make stop-backend
	cd backend && ./mvnw spring-boot:start -Dspring.application.admin.enabled=true

# Stops the background application gracefully, with a force-kill fallback
stop-backend:
	-cd backend && ./mvnw spring-boot:stop
	@fuser -k 8080/tcp || true

# Generates openapi.json directly from your active running server on port 8080
generate-docs:
	cd backend && ./mvnw springdoc-openapi:generate