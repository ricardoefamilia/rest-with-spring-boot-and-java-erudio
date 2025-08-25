FROM eclipse-temurin:21-jdk
COPY target/*.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar"]