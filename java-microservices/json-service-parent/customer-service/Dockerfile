FROM openjdk:17.0.2-slim


ARG JAR_FILE

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dlogging.config=file:/logback-spring.xml", "-jar","/app.jar"]

EXPOSE 8080
