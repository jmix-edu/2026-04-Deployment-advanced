FROM bellsoft/liberica-openjdk-alpine
WORKDIR opt
COPY build/libs/Jmix-pm-2_7_1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]