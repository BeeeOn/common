#!/bin/sh
#Script for running adaapp from installDir passed by first command line argument $1

if [ ! -f $1/usr/bin/adaapp ];then
	echo "Cant find adaapp binary file"
	exit -1;
fi

cd $1/usr/bin/

./adaapp

cd -
