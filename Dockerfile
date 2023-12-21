FROM openjdk:13-jdk-slim

ARG APP_VERSION=0
ENV TZ=UTC

WORKDIR /app
WORKDIR /app/certs

EXPOSE 8080
COPY applications/app-service/build/libs/*.jar /app/app.jar

CMD java -XX:OnOutOfMemoryError="kill -9 %p" \
         -jar /app/app.jar