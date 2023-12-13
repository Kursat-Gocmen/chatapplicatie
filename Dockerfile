FROM openjdk:22-ea-26

EXPOSE 8080

ADD target/chatapplicatie-0.0.1-SNAPSHOT.jar app.jar

ADD target/classes/com/backend/chatapplicatie/InsertData.class InsertData.class

ENTRYPOINT ["java", "-jar", "/app.jar", "InsertData.class"]