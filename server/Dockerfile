FROM maven:3.6-adoptopenjdk-11 AS build
COPY server/src /usr/src/server/src
COPY server/pom.xml /usr/src/server
COPY pom.xml /usr/src
RUN mvn -f /usr/src/server/pom.xml clean package

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /usr/src/server/target/*.jar /usr/app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/usr/app.jar"]