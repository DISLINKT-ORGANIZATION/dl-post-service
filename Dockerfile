FROM adoptopenjdk:11-jre-hotspot
COPY "target/post-dislinkt.postservice.service.jar" post-dislinkt.postservice.service.jar
ENTRYPOINT ["java", "-jar", "post-dislinkt.postservice.service.jar"]