FROM openjdk:11-jre
WORKDIR usr/lib
ENV MONGO_DATABASE=keepnote
ENV MONGO_PORT=27017
ENV MONGO_HOST=localhost
COPY ./target/UserService-0.0.1-SNAPSHOT.jar /usr/lib/UserService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "UserService-0.0.1-SNAPSHOT.jar"]
