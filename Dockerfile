FROM tomcat:9-jre8-alpine
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/bananapp-264116-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
