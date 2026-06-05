.PHONY: db-up db-down db-status db-logs mvn-clean mvn-test mvn-build mvn-run mvn-start mvn-stop mvn-docs

# ==========================================
# 🐳 DATABASE COMMANDS (Docker)
# ==========================================

# Start the PostgreSQL database in the background
db-up:
	docker compose up -d

# Stop and remove the database container
db-down:
	docker compose down

# Check the status of the database container
db-status:
	docker ps

# View the logs of the database container
db-logs:
	docker compose logs -f

# ==========================================
# ☕ BACKEND COMMANDS (Maven & Spring Boot)
# ==========================================

# Clean the target directory
mvn-clean:
	cd backend && ./mvnw clean

# Run all unit and integration tests
mvn-test:
	cd backend && ./mvnw test

# Build the executable .jar file (skips tests)
mvn-build:
	cd backend && ./mvnw clean package -DskipTests

# Run the Spring Boot application interactively in the terminal
mvn-run:
	-make mvn-stop
	cd backend && ./mvnw spring-boot:run

# Start the application in the background (frees terminal immediately)
mvn-start:
	-make mvn-stop
	cd backend && ./mvnw spring-boot:start -Dspring.application.admin.enabled=true

# Stop the background application gracefully, with a force-kill fallback
mvn-stop:
	-cd backend && ./mvnw spring-boot:stop
	@fuser -k 8080/tcp || true

# Generate openapi.json directly from your active running server on port 8080
mvn-docs:
	cd backend && ./mvnw springdoc-openapi:generate