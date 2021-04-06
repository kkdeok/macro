FROM ubuntu:18.04
MAINTAINER doubleknd26
MAINTAINER doubleknd26@gmail.com

ARG TYPE 

ENV TYPE_VAL $TYPE
ENV MACRO_PATH=/program/macro

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk

RUN apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
    && localedef -i ko_KR -c -f UTF-8 -A /usr/share/locale/locale.alias ko_KR.UTF-8
ENV LANG ko_KR.UTF-8

# Make directories
RUN mkdir -p $MACRO_PATH

# Copy binary and others we need 
COPY build/libs/macro-application-1.0-SNAPSHOT-all.jar $MACRO_PATH

# Go to work dir
WORKDIR $MACRO_PATH

ENV DEBUG="-Xms128m -Xmx128m \
-Dcom.sun.management.jmxremote=true \
-Dcom.sun.management.jmxremote.port=11619 \
-Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.authenticate=false"

CMD java $DEBUG \
  -cp /program/macro/macro-application-1.0-SNAPSHOT-all.jar \
  com.kkd.macro.notifier.NotifierApp --type $TYPE_VAL 