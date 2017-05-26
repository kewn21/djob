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



echo "Start to set up slave"

if [ -d "$ROOT_HOME" ]; then
	rm -rf $ROOT_HOME
fi


cp -r /data/javaapps/kesy-djob-0.0.1-SNAPSHOT $ROOT_HOME

sed -i 's/8100/8101/g' $ROOT_HOME/conf/job/job-server.properties

sed -i 's/\/djob\//\/djob-slave\//g' $ROOT_HOME/conf/logback.xml

sed -i 's/kesy-djob/kesy-djob-slave/g' $ROOT_HOME/bin/startup.sh



echo "Start to launch slave"

$ROOT_HOME/bin/startup.sh


if [ `ps -ef | grep djob-slave | awk '{ if($13=="-Dapp=kesy-djob-slave") print $0 }' | wc -l` -ne 1 ]; then
	echo "Slave start up unsuccessfully"
else
	SLAVE_PORT=`ps -ef | grep djob-slave | awk '{ if($13=="-Dapp=kesy-djob-slave") print $2 }'`

	echo "The port of slave using is $SLAVE_PORT"
fi


