@echo off
echo Cleaning up old processes...
for %%p in (8761 8085 8080 8081 8082) do (
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%p') do (
        taskkill /F /PID %%a 2>nul
    )
)

echo Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka-server && mvnw.cmd spring-boot:run > ..\eureka.log 2>&1"
timeout /t 15

echo Starting Microservices...
start "Auth Service" cmd /k "cd auth-service && mvnw.cmd spring-boot:run > ..\auth.log 2>&1"
start "Application Service" cmd /k "cd application-service && mvnw.cmd spring-boot:run > ..\application.log 2>&1"
start "Doc Verification Service" cmd /k "cd doc-verification && mvnw.cmd spring-boot:run > ..\doc.log 2>&1"
timeout /t 20

echo Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvnw.cmd spring-boot:run > ..\gateway.log 2>&1"

echo All services are booting up! Please wait 30 seconds for them to register with Eureka.
