FROM alpine:3.19

RUN apk add openjdk17

WORKDIR $HOME/app
COPY ./target/farmflow-0.0.1-SNAPSHOT.jar farmflow-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT java -jar farmflow-0.0.1-SNAPSHOT.jar