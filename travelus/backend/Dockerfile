FROM openjdk:17-jdk
LABEL maintainer="ehddnjs6409@naver.com"
ARG JAR_FILE=build/libs/soltravel-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} soltravel-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/soltravel-springboot.jar"]
