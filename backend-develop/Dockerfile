# ========== 빌드 단계 ==========
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace/app

COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# 윈도우에서 클론했을 때 gradlew 줄바꿈(CRLF) 문제 방지
RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew

# 테스트는 일단 빼고 빌드 (원하면 -x test 제거 가능)
RUN ./gradlew bootJar -x test

# ========== 실행 단계 ==========
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]