#!/bin/sh

export JAVA_HOME=/data/services/java-1.7.0_17.8
export PATH=$JAVA_HOME/bin:$PATH
echo "Using JAVA_HOME: $JAVA_HOME"

nohup java -Xms2048m -Xmx4096m -XX:PermSize=128M -XX:MaxPermSize=256m -Dapp=kesy-djob -cp djob-bootstrap.jar org.kesy.djob.lac.loader.RunLoader >/dev/null 2>&1 &
