# Delivery Fee Calculator API

This is a Java Spring Boot project that implements a sub-functionality of a food delivery application, which calculates 
the delivery fee for food couriers based on regional base fee, vehicle type, and weather conditions. 

The project uses an H2 database for storing and manipulating data, and includes a scheduled task for 
importing weather data from the Estonian Environment Agency's weather portal.

## Requirements

- Java 17
- Maven 3.9

## Usage

1. Run the project: `mvn spring-boot:run` or `docker compose up`
2. Send requests to the REST endpoint using Swagger UI: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
3. Inspect created H2 database: [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)


