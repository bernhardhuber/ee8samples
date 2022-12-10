@echo on
setlocal

set BASEDIR=%~dp0
set SRC_WAR_DIR=%BASEDIR%\..\web\target\web-1.0-SNAPSHOT
set DST_DEPLOYMENT_DIR=%BASEDIR%\target\wildfly\wildfly-25.0.0.Final\standalone\deployments

@rem set DST_DEPLOYMENT_WAR_FILE=%DST_DEPLOYMENT_DIR%\ko_pure_web.war
set DST_DEPLOYMENT_WAR_FILE=%DST_DEPLOYMENT_DIR%\ee8samples_web.war

rmdir %DST_DEPLOYMENT_WAR_FILE%
mklink /J %DST_DEPLOYMENT_WAR_FILE% %SRC_WAR_DIR%
