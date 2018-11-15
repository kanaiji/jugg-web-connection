#!/bin/bash

TIME=$(date "+%Y%m%d%H%M")
echo "${TIME}"
GIT_REVISION=$(git log -1 --pretty=format:"%h")
echo "${GIT_REVISION}"
IMAGE_NAME=9.42.41.226:5000/jugg-web-connection/jugg-web-connection:${TIME}_${GIT_REVISION}

echo "${IMAGE_NAME}" > IMAGE_NAME
echo "jugg-web-connection" > MODULE

docker build -t ${IMAGE_NAME} .
docker push ${IMAGE_NAME}

