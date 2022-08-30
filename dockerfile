FROM maven:3.8.6-eclipse-temurin-17-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:17-jdk-alpine3.14
COPY --from=build /usr/src/app/target/space-probe-1.0.0.jar /usr/app/space-probe-1.0.0.jar
EXPOSE 6868
ENTRYPOINT ["java","-jar","/usr/app/space-probe-1.0.0.jar"]