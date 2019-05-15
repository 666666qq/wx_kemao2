# 进入项目所在的目录，启动项目
mvn clean install

cd kemao_2
mvn  spring-boot:start

#启动关注程序
cd ../subscribe
mvn  spring-boot:start

#启动取消关注程序

cd ../unsubscribe
mvn  spring-boot:start

