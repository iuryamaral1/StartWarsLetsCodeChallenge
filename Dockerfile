FROM openjdk:11-jre-slim
ARG JAR_FILE=target/starwarsresistence-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]