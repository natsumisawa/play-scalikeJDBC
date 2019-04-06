FROM openjdk:8u151-jre

RUN mkdir /app
COPY target/universal/ /app
CMD unzip app/draco-sample-2.0-SNAPSHOT.zip -d /app && ls /app/draco-sample-2.0-SNAPSHOT/bin/draco-sample && mkdir /app/draco-sample-2.0-SNAPSHOT/bin/draco-sample/sql && cp sql/ /app/draco-sample-2.0-SNAPSHOT/bin/draco-sample/sql && app/draco-sample-2.0-SNAPSHOT/bin/draco-sample -Dplay.http.secret.key="dracokey20190323dracokey20190323"
