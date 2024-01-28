#!/bin/bash
# 서비스 이름과 이미지 이름을 입력받습니다.
# config-service config-service
# user-service user-service
# mysql mysql
SERVICE_NAME=$1
COMMAND=$2
IMAGE_NAME=$3

# Docker Compose 파일의 경로를 입력합니다.
INFRA_FILE_PATH=docker-swarm-all-infra.yml
SERVICE_FILE_PATH=docker-swarm-all-service.yml
STACK_NAME=base-stack
# IMAGE_NAME이 비어 있는지 확인합니다.
if [ -z "$IMAGE_NAME" ]
then
  IMAGE_NAME=$SERVICE_NAME
fi

# 명령에 따라 동작을 수행합니다.
case $COMMAND in
  "update")
    # Docker 이미지를 빌드하고 업데이트합니다.
    docker-compose -f $INFRA_FILE_PATH build $SERVICE_NAME
    docker-compose -f $SERVICE_FILE_PATH build $SERVICE_NAME
    docker push rhsalska55/$IMAGE_NAME
    docker pull rhsalska55/$IMAGE_NAME
    docker service update --image rhsalska55/$IMAGE_NAME base-stack_$SERVICE_NAME
    ;;
  "start")
    # Docker Swarm에서 서비스를 시작합니다.
    echo "Deploying all services to the stack..."
    docker stack deploy -c $INFRA_FILE_PATH $STACK_NAME
    docker stack deploy -c $SERVICE_FILE_PATH $STACK_NAME
    ;;
  "stop")
    # Docker Swarm에서 서비스를 중지합니다.
    docker service rm base-stack_$SERVICE_NAME
    ;;
  *)
    echo "Invalid command. Please use 'update', 'start', or 'stop'."
    ;;
esac