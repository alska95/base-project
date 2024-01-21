FROM maven:3.8.1-openjdk-11-slim as build
WORKDIR /app
COPY . /app
# Maven 빌드 프로파일을 변수로 전달
ARG MAVEN_PROFILE=local
# Docker 이미지 내에서 애플리케이션의 패키징이 이루어진다.
# Maven clean package 명령을 통해 애플리케이션의 빌드와 패키징이 수행된다.
RUN mvn clean package -P${MAVEN_PROFILE} -DskipTests
# OpenJDK 17 Early Access 버전이 부모 이미지로 사용된다.
FROM openjdk:17-ea-11-jdk-slim
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일이 복사된다.
# --from=build 옵션은 파일을 가져올 단계를 지정한다.
# /app/target/service-discovery-1.0.jar는 복사할 파일의 경로다.
# service-discovery-1.0.jar는 파일이 복사될 위치다.
COPY --from=build /app/target/service-discovery-1.0.jar service-discovery-1.0.jar
# java -jar service-discovery-1.0.jar 명령을 통해 애플리케이션이 시작된다. 메이븐 프로필과 같은 스프링 프로필을 사용한다.
ENTRYPOINT ["java","-jar","service-discovery-1.0.jar"]