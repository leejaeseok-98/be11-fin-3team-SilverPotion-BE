# 필요프로그램 설치
FROM openjdk:17-jdk-alpine as stage1
# 파일 복사
WORKDIR /app
COPY gradle gradle
COPY src src
COPY build.gradle .
COPY gradlew .

# 빌드
RUN chmod +x gradlew
RUN ./gradlew bootJar

# 두 번째 스테이지
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=stage1 /app/build/libs/*.jar app.jar

# 컨테이너 내부 시간 한국시간으로 맞추는 설정.
RUN apk update && apk add --no-cache tzdata
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo "Asia/Seoul" > /etc/timezone

# 실행 : CMD 또는 ENTRYPOINT를 통해 컨테이너를 배열 형태의 명령어로 실행
ENTRYPOINT ["java", "-jar", "app.jar"]