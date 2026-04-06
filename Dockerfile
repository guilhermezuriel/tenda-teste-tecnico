# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN chown -R spring:spring /app

USER spring:spring

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]