# Docker Compose 파일의 경로
COMPOSE_INFRA_FILE=docker-compose-jenkins-env.yml
PROJECT_NAME=jenkins-env

if [ "$1" = "start" ]; then
    # Docker Compose로 서비스 시작
    if [ -z "$2" ]; then
        echo "Building all services without cache..."
        docker-compose -f $COMPOSE_INFRA_FILE -p $PROJECT_NAME build --no-cache
        echo "Starting all services..."
        docker-compose -f $COMPOSE_INFRA_FILE -p $PROJECT_NAME up -d
    else
        echo "Building service '$2' without cache..."
        docker-compose -f $COMPOSE_INFRA_FILE -p $PROJECT_NAME build --no-cache $2
        echo "Starting service '$2'..."
        docker-compose -f $COMPOSE_INFRA_FILE -p $PROJECT_NAME up -d $2
    fi
    docker image prune -a -f
elif [ "$1" = "stop" ]; then
    # Docker Compose로 서비스 중지
    if [ -z "$2" ]; then
        echo "Stopping all services..."
        docker-compose -f $COMPOSE_INFRA_FILE -p $PROJECT_NAME down
    else
        echo "Stopping service '$2'..."
        docker-compose -f $COMPOSE_INFRA_FILE -p $PROJECT_NAME stop $2
    fi
    docker image prune -a -f
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi