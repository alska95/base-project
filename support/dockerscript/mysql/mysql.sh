#!/bin/bash

# 컨테이너, 이미지, 네트워크 이름
CONTAINER_NAME=mysql
IMAGE_NAME=rhsalska55/my-mysql:1.0
NETWORK_NAME=ecommerce-network

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
    echo "Building image..."
    docker build --tag $IMAGE_NAME .

    # 네트워크가 존재하는지 확인, 없다면 디폴트 네트워크 사용
    if docker network ls | grep -qw $NETWORK_NAME; then
        NETWORK_OPTION="--network $NETWORK_NAME"
    else
        echo "Network '$NETWORK_NAME' not found. Using default network..."
        NETWORK_OPTION=""
    fi

    # 새 컨테이너 실행
    echo "Running new container..."
    docker run -d --name $CONTAINER_NAME $NETWORK_OPTION -v /Users/user/Desktop/git/SpringCloud_Practice/supportsub/mysqlmount:/var/lib/mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=true -p 3306:3306 $IMAGE_NAME
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