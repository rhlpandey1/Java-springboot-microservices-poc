# ---- Stage 1: BUILD (compile the jar with Gradle + JDK) ----
FROM gradle:8.9-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon || true   # warm the dependency cache
COPY src ./src
RUN gradle clean bootJar --no-daemon          # produces build/libs/demo-1.0.0.jar

# ---- Stage 2: RUN (small JRE-only image) ----
FROM eclipse-temurin:17-jre
WORKDIR /
COPY --from=build /app/build/libs/*.jar demo-java-app-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo-java-app-micro-1.0.0.jar"]