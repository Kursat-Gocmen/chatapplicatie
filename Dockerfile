FROM openjdk:21

EXPOSE 8080

ADD target/chatapplicatie-0.0.1-SNAPSHOT.jar app.jar

ADD target/classes/com/backend/chatapplicatie/InsertData.class Insertdata.class

ENTRYPOINT ["java", "-jar", "/app.jar", "Inserdata.class"]