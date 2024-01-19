# 기본 이미지를 지정함. 이 이미지는 새로 생성하는 도커 이미지의 기반이 된다. 아래는 jdk
FROM openjdk:17-ea-11-jdk-slim
# 디랙토리
VOLUME /tmp
# jar파일을 /tmp/user-service.jar로 복사
COPY target/config-service-1.0.jar config-service-1.0.jar
COPY encryptionKey.jks encryptionKey.jks
# 공백 대신 리스트로나눠서
ENTRYPOINT ["java","-jar","config-service-1.0.jar"]
