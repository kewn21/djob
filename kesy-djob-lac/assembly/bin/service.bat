@echo off
setlocal

set SERVICE_NAME=djob

if "%1" == "" goto displayUsage
set SERVICE_NAME=%1

:displayUsage

rem my location
set MYPATH=%~dp0

rem location of Prunsrv
set PATH_PRUNSRV=%MYPATH%
set PR_LOGPATH=%PATH_PRUNSRV%
rem location of jarfile
set PATH_JAR=%MYPATH%
set PATH_LOG=%MYPATH%..\logs

rem set prunsrv exe
set PRUNSRV=%PATH_PRUNSRV%djob

rem Install the djob

echo Installing %SERVICE_NAME%
%PRUNSRV% //IS//%SERVICE_NAME%

echo Setting the parameters for %SERVICE_NAME%
%PRUNSRV% //US//%SERVICE_NAME% --Jvm=auto --LogPath=%PATH_LOG% --StdOutput auto --StdError auto ^
--Classpath=%PATH_JAR%djob-bootstrap.jar ^
--StartMode=jvm --StartClass=org.kesy.djob.lac.loader.RunLoader --StartMethod=start ^
--StopMode=jvm  --StopClass=org.kesy.djob.lac.loader.RunLoader  --StopMethod=stop ^
--JvmOptions=-Xms512m;-Xmx1024m;-XX:PermSize=128M;-XX:MaxPermSize=512m ^
--Startup=auto

echo Installation of %SERVICE_NAME% is complete
 
pause