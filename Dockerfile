FROM java:8

EXPOSE 8081

ADD pts-service.jar pts-service.jar
RUN bash -c 'touch /pts-service.jar'
ENV JAVA_OPTS=""

#指定执行启动spring boot项目     ENTRYPOINT 为容器启动后执行的命令
ENTRYPOINT ["java","-jar","pts-service.jar"]
