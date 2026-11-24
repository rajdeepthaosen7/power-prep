FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/power-prep-0.0.1.jar app.jar
# Render free = 512 MB RAM, 0.1 CPU: keep the JVM small and start fast
ENV JAVA_TOOL_OPTIONS="-Xmx300m -Xss512k -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -XX:+ExitOnOutOfMemoryError"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
