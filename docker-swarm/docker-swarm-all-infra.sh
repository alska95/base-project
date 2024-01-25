# Docker Compose 파일의 경로
COMPOSE_INFRA_FILE=docker-swarm-all-infra.yml
STACK_NAME=base-stack

if [ "$1" = "start" ]; then
    # Docker compose로 이미지 먼저 빌드
    echo "Building all services without cache..."
    docker-compose -f $COMPOSE_INFRA_FILE build --no-cache
    # Docker Stack으로 서비스 시작
    echo "Deploying all services to the stack..."
    docker stack deploy -c $COMPOSE_INFRA_FILE $STACK_NAME
elif [ "$1" = "stop" ]; then
    # Docker Stack으로 서비스 중지
    echo "Removing all services from the stack..."
    docker stack rm $STACK_NAME
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi