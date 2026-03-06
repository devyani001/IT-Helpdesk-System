# Stage 1: Build WAR using Maven
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Deploy to Tomcat
FROM tomcat:10.1-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Configure for Render's port (10000)
EXPOSE 10000
RUN sed -i 's/port="8080"/port="10000"/' /usr/local/tomcat/conf/server.xml

CMD ["catalina.sh", "run"]