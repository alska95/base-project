#!/bin/bash
# 서비스 이름과 이미지 이름을 입력받습니다.
# config-service config-service
# user-service user-service
# mysql mysql
SERVICE_NAME=$1
IMAGE_NAME=$2

# IMAGE_NAME이 비어 있는지 확인합니다.
if [ -z "$IMAGE_NAME" ]
then
  IMAGE_NAME=$SERVICE_NAME
fi

# Docker Compose 파일의 경로를 입력합니다.
INFRA_FILE_PATH=docker-swarm-all-infra.yml
SERVICE_FILE_PATH=docker-swarm-all-service.yml

# Docker 이미지를 빌드합니다.
docker-compose -f $INFRA_FILE_PATH build $SERVICE_NAME
docker-compose -f $SERVICE_FILE_PATH build $SERVICE_NAME
# 업데이트를 위해 이미지 푸쉬
docker push rhsalska55/$IMAGE_NAME
docker pull rhsalska55/$IMAGE_NAME
# Docker Swarm 서비스를 업데이트합니다.
docker service update --image rhsalska55/$IMAGE_NAME base-stack_$SERVICE_NAME