FROM openjdk:8-jdk

EXPOSE 8080
ADD ./build/libs/*.jar app.jar

CMD ["sh", "./config_set.sh"]