FROM  9.42.41.226:5000/tomcat/tomcat:8-jre8  #基础镜像

MAINTAINER "lewis <vndjwzou@cn.ibm.com>"  # 套路

ENV DIR_WEBAPP /usr/local/tomcat/webapps/ # 定义变量、后续会使用 ，具体路径可以先启动容器然后进入进行查看

RUN  rm -rf $DIR_WEBAPP/*   #删除webapp下所有文件，因为当前应用作为根应用

ADD target/jugg-connection-0.0.1-SNAPSHOT.war $DIR_WEBAPP/jugg-connection.war

 #公开端口
EXPOSE 8080
#设置启动命令
ENTRYPOINT ["/usr/local/tomcat/bin/catalina.sh","run"]