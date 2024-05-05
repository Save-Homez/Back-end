FROM openjdk:17-jdk

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar

CMD ["nohup", "java", "-jar", "app.jar"]
