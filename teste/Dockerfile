FROM maven:3.6-jdk-8
COPY . /usr/src/mymaven
WORKDIR /usr/src/mymaven
RUN mvn clean install -f /usr/src/mymaven && mkdir /usr/src/jars/
RUN find /usr/src/mymaven/ -iname '*.jar' -exec cp {} /usr/src/jars/ \;