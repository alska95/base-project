#!/bin/bash

# Kubernetes 리소스 파일의 경로
POD_FILE=service-discovery-pod.yaml
SERVICE_FILE=service-discovery-service.yaml

if [ "$1" = "start" ]; then
    # 파드와 서비스 시작
    echo "Starting pod..."
    kubectl apply -f $POD_FILE

    echo "Starting service..."
    kubectl apply -f $SERVICE_FILE

elif [ "$1" = "stop" ]; then
    # 파드와 서비스 중지
    echo "Stopping service..."
    kubectl delete -f $SERVICE_FILE

    echo "Stopping pod..."
    kubectl delete -f $POD_FILE

else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi