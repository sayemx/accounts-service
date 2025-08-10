#Start with a base image ontaining Java runtime
FROM openjdk:17-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER eazybytes.com
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="sayem.com"

#Add the jar to the image
COPY target/accounts-service-0.0.1-SNAPSHOT.jar accounts-service-0.0.1-SNAPSHOT.jar

#Execute the jar
ENTRYPOINT ["java", "-jar" ,"accounts-service-0.0.1-SNAPSHOT.jar"]