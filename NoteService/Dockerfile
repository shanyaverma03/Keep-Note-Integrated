FROM openjdk:11-jre
WORKDIR usr/lib
ENV MONGO_DATABASE=keepnote
ENV MONGO_PORT=27017
ENV MONGO_HOST=localhost
COPY ./target/NoteService-0.0.1-SNAPSHOT.jar /usr/lib/NoteService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "NoteService-0.0.1-SNAPSHOT.jar"]
