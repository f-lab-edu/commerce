FROM openjdk:8-jdk

ADD ./commerce/build/libs/*.jar /usr/src/app/app.jar
ADD ./application-db.yml /usr/src/app/application-db.yml
WORKDIR /usr/src/app
CMD ["java", "-jar", "-Dspring.config.location=classpath:/application.yml,./application-db.yml", "app.jar"]