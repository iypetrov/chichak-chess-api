FROM maven:3.9.6-amazoncorretto-17 AS build-stage
ADD . /app/
WORKDIR /app/
RUN mvn clean install -DskipTests

FROM build-stage AS run-stage
WORKDIR /app/
EXPOSE 8080
COPY --from=build-stage /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
