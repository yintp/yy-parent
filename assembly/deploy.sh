all_modules=(yy-app-web yy-admin-web yy-user-provider)

#show list for projects
num=${#all_modules[@]}
count=0

echo "******************************************"
echo "please input you select :"
echo "----------------------------------------"
for i in ${all_modules[@]}
do
  echo "$count : $i"
  echo "----------------------------------------"
 let count++
done
echo "******************************************"
echo -n "Enter your select number:"

read   selectNum
if [ ! -n "$selectNum" ]; then
  echo "input error!"
  exit
fi
if [ $selectNum -gt $num  ]; then
  echo "input error!"
  exit
else if [ $selectNum -lt -1 ]; then
   echo "input error!"
   exit
   fi
fi

SERVER_NAME=${all_modules[selectNum]}
echo '发布的项目是：'$SERVER_NAME
#项目存放路径
SERVER_PATH='/data/server'
#代码路径
CODE_PATH='/data/code/yy-parent'
#进入代码
cd $CODE_PATH
#更新代码
svn up
#编译项目
mvn -U package -pl $SERVER_NAME -am

PID=`ps -ef|grep $SERVER_PATH/$SERVER_NAME/ |grep -v grep|awk '{print $2}'`
echo 'pid ' $PID
if [ ! -n "$PID" ];then
   echo -e "\033[40;37m stop $PID now and execute $TOMCAT_BIN/shutdown.sh,Please wait...\033[0m"
else
   kill -9 $PID
   sleep 1
fi
#根据项目类型采用不同方式的项目启动
if [[ $SERVER_NAME =~ 'web' ]]
then
        echo 'web project'
        #删除tomcat内的项目内容
        rm -rf $SERVER_PATH/$SERVER_NAME/webapps/$SERVER_NAME.*
        #复制文件到tomcat
        cp $CODE_PATH/$SERVER_NAME/target/$SERVER_NAME.war $SERVER_PATH/$SERVER_NAME/webapps/
        #启动项目
        sh $SERVER_PATH/$SERVER_NAME/bin/startup.sh
else
        echo 'server project'
        #删除微服务项目的内容
        rm -rf $SERVER_PATH/$SERVER_NAME
        #解压文件到目标
        unzip $CODE_PATH/$SERVER_NAME/target/$SERVER_NAME-assembly.zip -d $SERVER_PATH/
        #启动项目
        sh $SERVER_PATH/$SERVER_NAME/bin/start.sh
fi
