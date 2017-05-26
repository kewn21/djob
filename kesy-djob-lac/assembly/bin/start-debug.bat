@echo off

set "JAR_LIB=..\lib"
setlocal ENABLEDELAYEDEXPANSION
SET JARFILE=
FOR %%i IN (%JAR_LIB%\*.jar) DO (
    SET JARFILE=!JARFILE!;%%i
)

set "JAR_PLUG=..\plugins"
setlocal ENABLEDELAYEDEXPANSION
FOR %%i IN (%JAR_PLUG%\*.jar) DO (
    SET JARFILE=!JARFILE!;%%i
)

set "JAR_DROPIN=..\dropins"
setlocal ENABLEDELAYEDEXPANSION
FOR %%i IN (%JAR_DROPIN%\*.jar) DO (
    SET JARFILE=!JARFILE!;%%i
)
 
set "DJOB_CONF=..\conf"

set JARFILE=!JARFILE!;%DJOB_CONF%

java -Xms128m -Xmx256m -XX:PermSize=128M -XX:MaxPermSize=512m -cp %JARFILE% org.kesy.djob.lac.Startup
 
pause

