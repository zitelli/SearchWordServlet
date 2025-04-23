# Use Ubuntu 18.04 as the base image
FROM ubuntu:18.04

# Install JDK 11
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget && \
    apt-get clean

# Download and install WildFly 26
ENV WILDFLY_VERSION 26.1.3.Final
ENV WILDFLY_HOME /opt/wildfly

RUN wget https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz -O /tmp/wildfly.tar.gz && \
    tar -xzf /tmp/wildfly.tar.gz -C /opt && \
    mv /opt/wildfly-$WILDFLY_VERSION $WILDFLY_HOME && \
    rm /tmp/wildfly.tar.gz

# Add a WildFly management user (username: admin, password: admin)
RUN $WILDFLY_HOME/bin/add-user.sh -u admin -p admin --silent

# Copy your WAR file into the deployments directory
COPY ./target/searchword.war $WILDFLY_HOME/standalone/deployments/

# Expose ports (HTTP and management)
EXPOSE 8080 9990

# Start WildFly in standalone mode
CMD ["/opt/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
