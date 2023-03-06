FROM maven:3.8-openjdk-11-slim AS MAVEN_TOOL_CHAIN
WORKDIR /opt
COPY . .
RUN mvn install

FROM fabric8/java-alpine-openjdk11-jre:1.8
WORKDIR /opt
COPY --from=MAVEN_TOOL_CHAIN /opt/target/cm-coding-challenge-*.jar app.jar

CMD ["/bin/sh", "-c", "java -jar app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE --server.port=8080"]