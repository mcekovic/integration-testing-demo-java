FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080
VOLUME /var/log /tmp
ARG APP=/betting
ARG JAR_FILE=target/betting-service-*.jar
COPY ${JAR_FILE} ${APP}/betting-service.jar
WORKDIR "${APP}"
ENTRYPOINT ["java", "-server", "-Dspring.profiles.active=docker", "-Xms256m", "-Xmx256m",\
 "-jar", "betting-service.jar", "com.igt.demo.betting.BettingApplication"]