FROM openjdk:19
VOLUME /tmp
COPY target/converter-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
