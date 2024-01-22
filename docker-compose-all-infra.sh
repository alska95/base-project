# Docker Compose 파일의 경로
COMPOSE_INFRA_FILE=docker-compose-all-infra.yml

if [ "$1" = "start" ]; then
    # Docker Compose로 서비스 시작
    if [ -z "$2" ]; then
        echo "Building all services without cache..."
        docker-compose -f $COMPOSE_INFRA_FILE build --no-cache
        echo "Starting all services..."
        docker-compose -f $COMPOSE_INFRA_FILE up -d
    else
        echo "Building service '$2' without cache..."
        docker-compose -f $COMPOSE_INFRA_FILE build --no-cache $2
        echo "Starting service '$2'..."
        docker-compose -f $COMPOSE_INFRA_FILE up -d $2
    fi
    docker image prune -a -f
elif [ "$1" = "stop" ]; then
    # Docker Compose로 서비스 중지
    if [ -z "$2" ]; then
        echo "Stopping all services..."
        docker-compose -f $COMPOSE_INFRA_FILE down
    else
        echo "Stopping service '$2'..."
        docker-compose -f $COMPOSE_INFRA_FILE stop $2
    fi
    docker image prune -a -f
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi