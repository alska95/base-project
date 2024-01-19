# 기본 이미지를 지정함. 이 이미지는 새로 생성하는 도커 이미지의 기반이 된다. 아래는 jdk
FROM openjdk:17-ea-11-jdk-slim
# 디랙토리
VOLUME /tmp
COPY target/cloud-gateway-1.0.jar cloud-gateway-1.0.jar
# 공백 대신 리스트로나눠서
ENTRYPOINT ["java","-jar","cloud-gateway-1.0.jar"]
