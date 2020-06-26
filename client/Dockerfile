FROM maven:3.6-adoptopenjdk-11 AS build
COPY client/src /usr/src/client/src
COPY client/pom.xml /usr/src/client
COPY pom.xml /usr/src
RUN mvn -f /usr/src/client/pom.xml clean package

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /usr/src/client/target/*.jar /usr/app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/usr/app.jar"]