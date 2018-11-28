#!/bin/bash

IMAGE=$(cat IMAGE_NAME)
DEPLOYMENTS=$1
MODULE=$(cat MODULE)

echo "start deploy jugg connection...${DEPLOYMENTS}  ${MODULE}=${IMAGE}"

#操作
OPERATE=$2
echo "OPERATE = ${OPERATE}"
if [ ${OPERATE} == 'apply' ];then
    echo "kubectl apply -f /root/.jenkins/workspace/jugg-web-connection/cicd/pro/jugg-web-connection-deployment.yaml"
         #第一次部署, 找 yaml
   kubectl apply -f /root/.jenkins/workspace/jugg-web-connection/cicd/pro/jugg-web-connection-deployment.yaml
elif [ ${OPERATE} == 'update' ];then
    echo "kubectl set image deployments/${DEPLOYMENTS}  ${MODULE}=${IMAGE}"
          # 直接更新版本
    kubectl set image deployments/${DEPLOYMENTS}  ${MODULE}=${IMAGE}
else
    echo "Unknow argument...."
fi