FROM openjdk:13-jdk-slim
LABEL maintainer="Andrei Varashen <voroshen.av@gmail.com>"

COPY ./target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
