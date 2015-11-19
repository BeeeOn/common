#!/bin/sh
#Script for running adaapp from installDir passed by first command line argument $1

APP_NAME="adaapp"
FINISH_TIMEOUT=60
RUN_TIME=30

if [ ! -f $1/usr/bin/adaapp ];then
	echo "Cant find adaapp binary file"
	exit -1;
fi

echo "Starting adaapp..."
cd $1/usr/bin/
./${APP_NAME} &
APP_PID=$!

# let the app initialize components and run for a while
sleep $RUN_TIME
# no check if process is still running

ps -p $APP_PID> /dev/null
if [ $? -eq 0 ] && [ "$APP_NAME" == $(ps -p $APP_PID -o comm=) ]; then
	echo "Process is still running."
	echo "killing the process."
	kill $APP_PID

	echo "Waiting for adaapp to finish"
	counter=0
	while [ $? -eq 0 ]; do
		if [ $counter -eq $FINISH_TIMEOUT ]; then
			# if process cant finish in $FINISH_TIMEOUT seconds, we kill it
			# with force
			echo "Timeout expired, forcing killing process"
			kill -9 $APP_PID
			exit 1;
		fi
		sleep 1
		((counter++))
		ps -p $APP_PID> /dev/null
	done

	exit 0
else
	echo "Process is not running, adaapp probably crashed."
	exit 1
fi

cd -
