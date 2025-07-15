# 1단계: 빌드
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /build
COPY . .
RUN gradle clean build -x test

# 2단계: 실행
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar /app/collector.jar
EXPOSE 9000
CMD ["java", "-jar", "/app/collector.jar"]
