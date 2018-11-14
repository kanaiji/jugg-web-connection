#!/bin/bash

ROOT=$1

IMAGE_NAME=9.42.41.226:5000/jugg-web-connection/jugg-web-connection:latest

echo "${IMAGE_NAME}" > IMAGE_NAME
echo "jugg-web-connection" > MODULE

docker build -t ${IMAGE_NAME} .
docker push ${IMAGE_NAME}

