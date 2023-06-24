FROM tomcat:9-jdk17-corretto
COPY /build/libs/ROOT.war /usr/local/tomcat/webapps/
