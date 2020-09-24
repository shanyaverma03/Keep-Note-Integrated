FROM openjdk:11-jre
WORKDIR usr/lib
COPY ./target/netflix-eureka-naming-server-0.0.1-SNAPSHOT.jar /usr/lib/netflix-eureka-naming-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "netflix-eureka-naming-server-0.0.1-SNAPSHOT.jar"]
