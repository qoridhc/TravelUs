FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/travelus-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} travelus-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/travelus-springboot.jar"]
