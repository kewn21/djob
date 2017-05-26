#!/bin/sh

CUR_MINUTE=$(date +%M)

SAFE_TIME=$(($CUR_MINUTE%10))

echo "Current time is $(date +%M)"

if [ $SAFE_TIME -ne "3" ] && [ $SAFE_TIME -ne "8" ]; then
	echo "Current time is not a safe time to stop djob, try again when minute is end with 3 or 8"
else

	ROOT_HOME=/data/javaapps/kesy-djob-0.0.1-SNAPSHOT


	echo "check already starting djob process"

	if [ `ps -ef | grep djob | awk '{ if($13=="-Dapp=kesy-djob") print $0 }' | wc -l` -lt 1 ]; then
		echo "No found djob process"
	else
		for OLD_DJOB_PORT in `ps -ef | grep djob | awk '{ if($13=="-Dapp=kesy-djob") print $2 }'`
		do
			echo "kill djob process for port $OLD_DJOB_PORT"
			kill -9 $OLD_DJOB_PORT
		done
	fi

	echo "shutdown djob process successfully"

fi

