#!/bin/bash

ROOT=$1
mvn -U -pl wsjc-web-supewisor -am clean package -Dmaven.test.skip=true

TIME=$(date "+%Y%m%d%H%M")

echo "${TIME}"
GIT_REVISION=$(git log -1 --pretty=format:"%h")
echo "${GIT_REVISION}"
IMAGE_NAME=www.znf4.top:5000/wsjc-web/web-supewisor:${TIME}_${GIT_REVISION}

echo "${IMAGE_NAME}" > IMAGE_NAME

#jenkins 根目录/root/.jenkins/workspace/wsjc-boot
cd ${ROOT}/wsjc-web-supewisor
docker build -t ${IMAGE_NAME} .
cd -

docker push ${IMAGE_NAME}

echo "web-supewisor" > MODULE