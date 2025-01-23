FROM maven:3.6-jdk-8


# Install wget
#RUN apk --no-cache add wget
#RUN apt-get update && \
#    apt-get install -y wget && \
#    rm -rf /var/lib/apt/lists/*

# Install Tomcat 9
ENV TOMCAT_VERSION 9.0.46
WORKDIR /opt
RUN wget https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar -xf apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    rm apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    mv apache-tomcat-${TOMCAT_VERSION} tomcat


# Set working directory to /code
WORKDIR /code

# Copy the local code to the container
COPY . .

# Run Maven clean and install commands
WORKDIR /code
RUN mvn clean install

# Copy the generated WAR file to Tomcat webapps directory
RUN cp -r /code/target/ems.war /opt/tomcat/webapps/
RUN cp -r /code/target/ems /opt/tomcat/webapps/


# Change directory to Tomcat bin
WORKDIR /opt/tomcat/bin

# Shutdown Tomcat
#RUN sh shutdown.sh

EXPOSE 8080
# Start Tomcat and tail the logs
#CMD ["sh", "startup.sh"]
#CMD ["sh", "-c", "./startup.sh"]
CMD ["sh", "catalina.sh", "run"]
