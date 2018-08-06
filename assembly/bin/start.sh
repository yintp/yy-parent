#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
SERVER_NAME=${DEPLOY_DIR##*/}
 
if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PID=`ps -ef|grep java |grep $DEPLOY_DIR |grep -v grep|awk '{print $2}'`
if [ -n "$PID" ]; then
 echo -e "\033[40;31m ERROR: The $SERVER_NAME PID: $PID already started! \033[0m"
    exit 1
fi

LOGS_DIR=/data/logs/$SERVER_NAME
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi

STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "

JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi

JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi

ENV=`sed '/env/!d;s/.*=//' /data/conf/config_center.properties | tr -d '\r'`

JAVA_MEM_OPTS=" -server -Xms256M -Xmx256M -Denv=$ENV -DmoduleName=$SERVER_NAME"

echo -e "Starting the $SERVER_NAME java_opts $JAVA_MEM_OPTS \c"

nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS com.alibaba.dubbo.container.Main > $STDOUT_FILE 2>&1 &

while [ -z "$PID" ]; do    
    echo -e ".\c"
    sleep 1 
    PID=`ps -ef|grep java |grep $DEPLOY_DIR |grep -v grep|awk '{print $2}'`
    if [ -n "$PID" ]; then
        break
    fi
done

echo -e "\t"
echo -e "\033[40;32m SUCCESS!The $SERVER_NAME started! PID: $PID \033[0m"





