FROM adoptopenjdk:11-jre-hotspot
COPY "target/post-service.jar" post-service.jar
ENTRYPOINT ["java", "-jar", "post-service.jar"]