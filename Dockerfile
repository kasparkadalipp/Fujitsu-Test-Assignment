FROM maven:3.9.0-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/delivery-fee-service-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
