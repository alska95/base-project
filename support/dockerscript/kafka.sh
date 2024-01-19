#!/bin/bash

if [ "$1" = "start" ]; then
  docker-compose -f docker-compose-single-broker.yml up -d
elif [ "$1" = "stop" ]; then
    docker-compose -f docker-compose-single-broker.yml down
else
    echo "Unknown command '$1'. Use 'start' or 'stop'."
fi