@echo off

java -Xms512m -Xmx1024m -XX:PermSize=128M -XX:MaxPermSize=512m -cp djob-bootstrap.jar org.kesy.djob.lac.loader.RunLoader
 
pause