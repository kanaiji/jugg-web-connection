FROM  www.znf4.top:5000/java/openjdk:8-jre
MAINTAINER agui 937334241@qq.com

COPY target/wsjc-web-supewisor-0.0.1-SNAPSHOT.jar /web-supewisor.jar

ENTRYPOINT ["java", "-jar", "/web-supewisor.jar"]


FROM tomcat #基础镜像

MAINTAINER "test <yanyuan904@163.com>"  # 套路

ENV DIR_WEBAPP /usr/local/tomcat/webapps/ # 定义变量、后续会使用 ，具体路径可以先启动容器然后进入进行查看

RUN  rm -rf $DIR_WEBAPP/*   #删除webapp下所有文件，因为当前应用作为根应用

ADD virtualShop-man.war $DIR_WEBAPP/ROOT.war 

RUN  unzip $DIR_WEBAPP/ROOT.war  -d  $DIR_WEBAPP/ROOT/

CMD ["catalina.sh", "run"]
