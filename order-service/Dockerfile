# Maven 3.8.1과 OpenJDK 11이 포함된 공식 Maven 런타임 이미지가 부모 이미지로 사용된다.
FROM maven:3.8.1-openjdk-11-slim as build
WORKDIR /app
# 현재 디렉토리의 모든 내용이 컨테이너의 /app 디렉토리로 복사된다.
COPY . /app
# Docker 이미지 내에서 애플리케이션의 패키징이 이루어진다.
# Maven clean package 명령을 통해 애플리케이션의 빌드와 패키징이 수행된다.
# -DskipTests 옵션은 테스트를 수행하지 않는다.
RUN mvn clean package -DskipTests
# OpenJDK 17 Early Access 버전이 부모 이미지로 사용된다.
FROM openjdk:17-ea-11-jdk-slim
# 작업 디렉토리가 컨테이너 내 /app로 설정된다.
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일이 복사된다.
# --from=build 옵션은 파일을 가져올 단계를 지정한다.
# /app/target/service-discovery-1.0.jar는 복사할 파일의 경로다.
# service-discovery-1.0.jar는 파일이 복사될 위치다.
COPY --from=build /app/target/order-service-1.0.jar order-service-1.0.jar
# java -jar service-discovery-1.0.jar 명령을 통해 애플리케이션이 시작된다.
ENTRYPOINT ["java","-jar","order-service-1.0.jar"]