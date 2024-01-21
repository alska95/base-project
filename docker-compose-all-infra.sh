#!/bin/bash

# Docker Compose 파일의 경로
COMPOSE_INFRA_FILE=docker-compose-all-infra.yml

if [ "$1" = "start" ]; then
    # Docker Compose로 서비스 시작
    echo "Starting services..."
    docker-compose -f $COMPOSE_INFRA_FILE up -d
elif [ "$1" = "stop" ]; then
    # Docker Compose로 서비스 중지
    echo "Stopping services..."
    docker-compose -f $COMPOSE_INFRA_FILE down
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi