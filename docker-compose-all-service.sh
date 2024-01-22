#!/bin/bash

# Docker Compose 파일의 경로
COMPOSE_SERVICE_FILE=docker-compose-all-service.yml

if [ "$1" = "start" ]; then
    # Docker Compose로 서비스 시작
    if [ -z "$2" ]; then
        echo "Stopping all services without cache..."
        docker-compose -f $COMPOSE_SERVICE_FILE down
        echo "Building all services without cache..."
        docker-compose -f $COMPOSE_SERVICE_FILE build --no-cache
        echo "Starting all services..."
        docker-compose -f $COMPOSE_SERVICE_FILE up -d
    else
        echo "Stopping service '$2'..."
        docker-compose -f $COMPOSE_SERVICE_FILE down $2
        echo "Building service '$2'..."
        docker-compose -f $COMPOSE_SERVICE_FILE build --no-cache $2
        echo "Starting service '$2'..."
        docker-compose -f $COMPOSE_SERVICE_FILE up -d $2
    fi
    docker image prune -a -f
elif [ "$1" = "stop" ]; then
    # Docker Compose로 서비스 중지
    if [ -z "$2" ]; then
        echo "Stopping all services without cache..."
        docker-compose -f $COMPOSE_SERVICE_FILE down
    else
        echo "Stopping service '$2'..."
        docker-compose -f $COMPOSE_SERVICE_FILE stop $2
    fi
    docker image prune -a -f
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi