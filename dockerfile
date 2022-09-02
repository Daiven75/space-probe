FROM openjdk:17-jdk-alpine3.14
COPY ./target/space-probe-1.0.0.jar space-probe-1.0.0.jar
CMD ["java","-jar","space-probe-1.0.0.jar"]