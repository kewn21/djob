#!/bin/sh

ROOT_HOME=/data/javaapps/kesy-djob-0.0.1-SNAPSHOT-slave


echo "check already starting slave process"

if [ `ps -ef | grep djob-slave | awk '{ if($13=="-Dapp=kesy-djob-slave") print $0 }' | wc -l` -lt 1 ]; then
	echo "No found slave process"
else
	for OLD_SLAVE_PORT in `ps -ef | grep djob-slave | awk '{ if($13=="-Dapp=kesy-djob-slave") print $2 }'`
	do
		echo "kill slave process for port $OLD_SLAVE_PORT"
		kill -9 $OLD_SLAVE_PORT
	done
fi

echo "shutdown slave process successfully"

