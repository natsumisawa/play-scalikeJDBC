FROM openjdk:8u151-jre

RUN mkdir /app
COPY target/universal/ /app
CMD unzip app/draco-sample-2.0-SNAPSHOT.zip -d /app && ls /app && app/draco-sample-2.0-SNAPSHOT/bin/draco-sample -Dplay.http.secret.key="dracokey20190323dracokey20190323"
