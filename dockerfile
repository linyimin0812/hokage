FROM java:8
ARG JAR_FILE
ADD target/${JAR_FILE} hokage.jar
ENTRYPOINT ["java", "-jar", "/hokage.jar"]