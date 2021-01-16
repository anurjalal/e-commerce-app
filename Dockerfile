FROM maven:3.6.3-jdk-11-slim
#COPY src /home/e-commerce-app/src
#COPY pom.xml /home/e-commerce-app/pom.xml
#RUN mvn -f /home/e-commerce-app/pom.xml package

COPY target/auth-course-0.0.1-SNAPSHOT.jar /home/e-commerce-app/target/auth-course-0.0.1-SNAPSHOT.jar