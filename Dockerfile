FROM bellsoft/liberica-openjre-alpine:11.0.12-7

ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar *.jar"]