#!/bin/bash


######### PARAM ######################################

JARFILE=`ls -1r baseframework-*.jar 2>/dev/null | head -n 1`
PID_FILE=pid.file
RUNNING=N
PWD=`pwd`
JAVA_OPT="-Xms2g -Xmx2g -XX:+UseG1GC -XX:+PrintGCDetails -Xloggc:jvm.log -XX:+PrintGCDateStamps"
######### DO NOT MODIFY ########

if [ -f $PID_FILE ]; then
    PID=`cat $PID_FILE`
    if [ ! -z "$PID" ] && kill -0 $PID 2>/dev/null; then
        RUNNING=Y
    fi
fi

start()
{
    if [ $RUNNING == "Y" ]; then
        echo "Application already started"
    else
        if [ -z "$JARFILE" ]
        then
            echo "ERROR: jar file not found"
        else
            nohup java  $JAVA_OPT -Djava.security.egd=file:/dev/./urandom -jar $PWD/$JARFILE > /dev/null 2>&1  &
            echo $! > $PID_FILE
            echo "Application $JARFILE starting..."
        fi
    fi
}

stop()
{
    if [ $RUNNING == "Y" ]; then
        kill -9 $PID
        rm -f $PID_FILE
        RUNNING=N
        echo "Application stopped"
    else
        echo "Application not running"
    fi
}

restart()
{
    stop
    start
}

case "$1" in

    'start')
        start
        ;;

    'stop')
        stop
        ;;

    'restart')
        restart
        ;;

    *)
        echo "Usage: $0 { start | stop | restart }"
        exit 1
        ;;
esac
exit 0