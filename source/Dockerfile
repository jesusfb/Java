FROM maven:3.9.3-eclipse-temurin-20 as build
WORKDIR /build
COPY . .
RUN mvn package

FROM eclipse-temurin:20-jdk-alpine
WORKDIR /app
COPY --from=build ./build/target/*.jar ./app.jar
HEALTHCHECK --interval=5s --timeout=5s CMD curl -f http://localhost:8080/actuator/health || exit 1
EXPOSE 8080
ENTRYPOINT java -jar app.jar