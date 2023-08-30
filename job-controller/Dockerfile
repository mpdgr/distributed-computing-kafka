FROM openjdk:19-jdk-alpine
EXPOSE 8081
COPY target/job-controller-1.0-SNAPSHOT.jar job-controller-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/job-controller-1.0-SNAPSHOT.jar"]