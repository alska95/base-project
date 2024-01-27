# Docker Compose 파일의 경로
COMPOSE_INFRA_FILE=docker-swarm-all-infra.yml
STACK_NAME=base-stack

# Docker Stack으로 서비스 중지
echo "Removing all services from the stack..."
docker stack rm $STACK_NAME
# Docker 이미지 삭제
echo "Removing all images..."
docker image rm $(docker image ls -q)

if [ "$1" = "start" ]; then
    # Docker compose로 이미지 먼저 빌드
    echo "Building all services without cache..."
    docker-compose -f $COMPOSE_INFRA_FILE build --no-cache
    # 빌드된 이미지들을 Docker Hub에 푸시
    echo "Pushing all images to Docker Hub..."
    docker-compose -f $COMPOSE_INFRA_FILE push
    echo "Pulling all images to Docker Hub..."
    docker-compose -f $COMPOSE_INFRA_FILE pull
    # Docker Stack으로 서비스 시작
    echo "Deploying all services to the stack..."
    docker stack deploy -c $COMPOSE_INFRA_FILE $STACK_NAME
fi