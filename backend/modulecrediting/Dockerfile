# development
FROM maven:3-openjdk-17-slim as development
RUN mkdir /app
WORKDIR /app
ENTRYPOINT ["mvn", "spring-boot:run"]


# build
FROM development as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
ENTRYPOINT ["mvn", "package"]

# production
FROM build as production
WORKDIR /app
COPY --from=build /app/target/modulecrediting-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "application.jar"]