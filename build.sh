#!/bin/bash

ROOT=$1
mvn -U -am clean package -Dmaven.test.skip=true

IMAGE_NAME=9.42.41.226:5000/jugg-web-connection/jugg-web-connection:latest

echo "${IMAGE_NAME}" > IMAGE_NAME

#jenkins 根目录/root/.jenkins/workspace/jugg-web-connection
cd ${ROOT}/jugg-web-connection
docker build -t ${IMAGE_NAME} .
cd -

docker push ${IMAGE_NAME}

echo "jugg-web-connection" > MODULE