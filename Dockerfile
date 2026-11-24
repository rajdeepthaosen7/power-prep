FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Use a Maven Central mirror: repo.maven.apache.org returns 429 to shared build servers
COPY settings.xml /root/.m2/settings.xml
COPY pom.xml .
COPY src ./src
# Retry a few times in case of a temporary download error
RUN for i in 1 2 3 4 5; do mvn -B -DskipTests package && exit 0; echo "Maven failed, retry $i"; sleep 20; done; exit 1

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/power-prep-0.0.1.jar app.jar
# Render free = 512 MB RAM, 0.1 CPU: keep the JVM small and start fast
ENV JAVA_TOOL_OPTIONS="-Xmx300m -Xss512k -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -XX:+ExitOnOutOfMemoryError"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
