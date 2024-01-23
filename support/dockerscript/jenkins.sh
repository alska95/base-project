#!/bin/bash

# 컨테이너, 이미지 이름
CONTAINER_NAME=jenkins
IMAGE_NAME=jenkins/jenkins
JENKINS_HOME=./jenkins

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

    # 이미지 다운로드
    echo "Pulling latest image..."
    docker pull $IMAGE_NAME

    # jenkins_home 디렉토리에 권한 부여
    echo "Setting permissions for jenkins_home directory..."
    chmod -R 777 $JENKINS_HOME

    # 새 컨테이너 실행
    echo "Running new container..."
    docker run -u root -d --name $CONTAINER_NAME --restart=on-failure -p 8088:8080 -p 50000:50000 -v $JENKINS_HOME:/var/jenkins_home $IMAGE_NAME
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