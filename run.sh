#!/bin/sh
type="dev"
if [ $# -gt 0 ];then
    type=$1
fi
if [ $type == 'prod' ];then
    mvn clean install -Pprod
fi
if [ $type == 'test' ]; then
    mvn clean install -Ptest
    scp -r ./target/chain-api.war spiderdt@192.168.1.2:/data/tmp/
fi
#default
if [ $type ==  'dev' ]; then
    mvn clean package install -Pdev
    #chown fivebit:staff ./target/*
    cp ./target/notice-api.war /Users/fivebit/apache-tomcat-8.5.14/webapps/
fi
