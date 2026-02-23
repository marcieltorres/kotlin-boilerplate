# ==============================================================================
# Stage 1 — Builder
# ==============================================================================
FROM gradle:8-jdk21 AS builder

WORKDIR /app
COPY --chown=gradle:gradle . .
RUN ./gradlew bootJar --no-daemon -x test

# ==============================================================================
# Stage 2 — Runtime (Alpine JRE) — dev / staging
# Use: docker build --target runtime .
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S appgroup && adduser -S -G appgroup appuser
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
USER appuser

EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

# ==============================================================================
# Stage 3 — Production (Distroless) — minimal attack surface
# Runs as non-root (UID 65532) — no shell, no package manager
# Use JAVA_TOOL_OPTIONS env var to pass JVM flags (read natively by the JVM)
# ==============================================================================
FROM gcr.io/distroless/java21-debian12:nonroot AS production

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
