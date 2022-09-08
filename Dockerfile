FROM openjdk:18-alpine3.15
EXPOSE 8080
#EXPOSE 8081
ADD target/IterativeTesting-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]