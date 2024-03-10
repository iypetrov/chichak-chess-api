FROM amazoncorretto:17 AS build-stage
ADD . /app/
WORKDIR /app/
RUN chmod +x mvnw && ./mvnw clean install -DskipTests

FROM build-stage AS run-stage
WORKDIR /app/
EXPOSE 8080
COPY --from=build-stage /app/target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]