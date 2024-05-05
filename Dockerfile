FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
CMD ["nohup", "java", "-jar", "app.jar"]
