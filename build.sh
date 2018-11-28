#!/bin/bash

HARBOR_IP=$1

echo "${HARBOR_IP}"
IMAGE_LATEST=${HARBOR_IP}:5000/jugg-web-connection/jugg-web-connection:latest

docker build -f ${DOCKERFILE_PATH}/Dockerfile  -t ${IMAGE_LATEST} .
docker push ${IMAGE_LATEST}
echo "update image latest success, op have build and push ${HARBOR_IP} harbor"

TIME=$(date "+%Y%m%d%H%M")
echo "${TIME}"
GIT_REVISION=$(git log -1 --pretty=format:"%h")
echo "${GIT_REVISION}"
IMAGE_NAME=${HARBOR_IP}:5000/jugg-web-connection/jugg-web-connection:${TIME}_${GIT_REVISION}

echo "${IMAGE_NAME}" > IMAGE_NAME
echo "jugg-web-connection" > MODULE

DOCKERFILE_PATH=$2
docker build -f ${DOCKERFILE_PATH}/Dockerfile  -t ${IMAGE_NAME} .
docker push ${IMAGE_NAME}

