#!/bin/bash

# 컨테이너, 이미지 이름
CONTAINER_NAME=ssh-server
IMAGE_NAME=ssh-server

if [ "$1" = "start" ]; then
    # 컨테이너가 이미 존재하는지 확인
    if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
        echo "Stopping and removing existing container..."
        # 컨테이너 중지 및 삭제
        docker stop $CONTAINER_NAME
        docker rm $CONTAINER_NAME
    fi

    # 이미지가 로컬에 존재하는지 확인, 있다면 삭제
    if [[ "$(docker images -q $IMAGE_NAME 2> /dev/null)" != "" ]]; then
        echo "Removing existing image..."
        docker rmi $IMAGE_NAME
    fi

    # 이미지 빌드
    echo "Building new image..."
    docker build -t $IMAGE_NAME .

    # 새 컨테이너 실행
    echo "Running new container..."
    docker run -d --name $CONTAINER_NAME -p 2222:22 -p 8000:8000 --privileged $IMAGE_NAME
elif [ "$1" = "stop" ]; then
    # 컨테이너가 이미 존재하는지 확인
    if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
        echo "Stopping and removing container..."
        # 컨테이너 중지 및 삭제
        docker stop $CONTAINER_NAME
        docker rm $CONTAINER_NAME
    fi

    # 이미지 삭제
    echo "Removing image..."
    docker rmi $IMAGE_NAME
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi