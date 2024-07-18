FROM openjdk:19
VOLUME /tmp
COPY target/accounts-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
