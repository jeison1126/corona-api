FROM clcrnafindev01acr.azurecr.io/stack/java11/openjdk11:jre-11-alpine
ENV TZ=America/Santiago
ENV JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/application.jar"]