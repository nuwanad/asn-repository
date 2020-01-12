FROM openjdk:8-jdk-alpine

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} application.jar
COPY docker/application/wait-for-it.sh wait-for-it.sh
COPY docker/application/entrypoint.sh entrypoint.sh

RUN apk update && apk add bash
CMD exec ./entrypoint.sh