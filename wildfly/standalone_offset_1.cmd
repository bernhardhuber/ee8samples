@echo on
setlocal

set BASEDIR=%~dp0
set WILDFLY_DIR=%BASEDIR%\target\wildfly\wildfly-25.0.0.Final

%WILDFLY_DIR%\bin\standalone.bat -Djboss.socket.binding.port-offset=1 

