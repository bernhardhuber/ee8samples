@echo off
setlocal

set BASEDIR=%~dp0
set WILDFLY_DIR=%BASEDIR%\target\wildfly\wildfly-25.0.0.Final

@rem set JAVA_HOME=c:\Users\berni3\scoop\apps\openjdk11\current
set JAVA_HOME=c:\Users\berni3\scoop\apps\openjdk17\current


%WILDFLY_DIR%\bin\standalone.bat -Djboss.socket.binding.port-offset=1 %*

