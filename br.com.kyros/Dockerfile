FROM maven:3.6-jdk-8 as builder
LABEL Name=kyrosb01 Version=1.0.0

COPY . /usr/src/kyrosb01
WORKDIR /usr/src/kyrosb01

RUN mvn clean install -f /usr/src/kyrosb01 && mkdir /usr/src/wars/
RUN find /usr/src/kyrosb01/ -iname '*.war' -exec cp {} /usr/src/wars/ \;

FROM tomcat:9.0.8-jre8
COPY --from=builder /usr/src/wars/* /usr/local/tomcat/webapps/