FROM amazoncorretto:17

WORKDIR /app
COPY target/*.jar .
CMD ["sh", "-c", "exec java -jar *.jar"]
