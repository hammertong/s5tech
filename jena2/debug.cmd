@echo off

@REM
@REM change current dir to script dir (needed when double clcicking on script icon)
@REM
cd /d %0\..

@REM *** Custom PATH here
SET LIBDIR=lib

@REM *** reset java options
SET JAVA_OPTS=

@REM *** activate JVM debug server fo remote debug of source files
SET JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n

@REM *** change startup configuration file 
@REM SET JAVA_OPTS=%JAVA_OPTS% -Dcom.s5tech.net.config=conf\s5.conf

@REM ***
@REM *** BACKUP OBSOLETE FILES (rel. <= 1.6.02)
@REM *** 
if exist %LIBDIR%\S5TNET-xml.jar move %LIBDIR%\S5TNET-xml.jar %LIBDIR%\S5TNET-xml.bak
if exist %LIBDIR%\S5TNET-client.jar move %LIBDIR%\S5TNET-client.jar %LIBDIR%\S5TNET-client.bak
if exist %LIBDIR%\S5TNET-ctl.jar move %LIBDIR%\S5TNET-ctl.jar %LIBDIR%\S5TNET-ctl.bak
if exist %LIBDIR%\S5TNET-data.jar move %LIBDIR%\S5TNET-data.jar %LIBDIR%\S5TNET-data.bak

@REM *** build automatically the classpath with all jars in LIBDIR
set CLASSPATH=conf
FOR %%G IN (%LIBDIR%\*.jar) do (
	call :addpath %%G
)
if exist bin SET CLASSPATH=%CLASSPATH%;bin

@REM ***
@REM *** COPY RXTX and MS-SQL DLLs IF NOT FOUND
@REM *** 
if exist rxtxSerial.dll goto dlls_ok
if not "%PROCESSOR_ARCHITECTURE%" == "x86" goto dlls_64
echo importing 32bit dlls ...
copy /y "%LIBDIR%\dll_x86\*.dll" . >nul
goto dlls_ok
:dlls_64
echo importing 64bit dlls ...
copy /y "%LIBDIR%\dll_x64\*.dll" . >nul
:dlls_ok

@REM ***
@REM *** RUN APPLICATION !!!
@REM *** 
java -cp %CLASSPATH% %JAVA_OPTS% com.s5tech.net.desktop.S5TechDesktopApp %*

:addpath
set CLASSPATH=%CLASSPATH%;%1

