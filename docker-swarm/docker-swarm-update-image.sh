#!/bin/bash

# 서비스 이름과 이미지 이름을 입력받습니다.
# config-service rhsalska55/config-service
SERVICE_NAME=$1
IMAGE_NAME=$2

# Docker Compose 파일의 경로를 입력합니다.
COMPOSE_FILE_PATH=docker-swarm-all-infra.yml

# Docker 이미지를 빌드합니다.
docker-compose -f $COMPOSE_FILE_PATH build $SERVICE_NAME
# 업데이트를 위해 이미지 푸쉬
docker push $IMAGE_NAME
# Docker Swarm 서비스를 업데이트합니다.
docker service update --image $IMAGE_NAME base-stack_$SERVICE_NAME