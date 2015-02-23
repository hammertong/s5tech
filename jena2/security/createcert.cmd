@echo off
@REM 
@REM 
@REM USER CONFIGURABLE SECTION
@REM 
@REM 
SET OPENSSL_HOME=c:\OpenSSL-Win32

@REM 
@REM 
@REM 
@REM 
AT > NUL
IF %ERRORLEVEL% EQU 0 goto _adminok
echo Only Administrators can run this script
goto _eof

:_adminok
openssl version 2>nul
if "%ERRORLEVEL%" == "0" goto _sslpathok
SET PATH=%PATH%;%OPENSSL_HOME%\bin
:_sslpathok

set DAYS=3653

@REM 
@REM GENERATE SERVER KEY AND CERTIFICATE, questi sono buoni per i SOCAT listener 
@REM 
SET FILENAME=server
openssl genrsa -out %FILENAME%.key 1024
openssl req -new -key %FILENAME%.key -x509 -days %DAYS% -out %FILENAME%.crt -subj "/C=IT/ST=Italy/L=Milan/O=S5Tech/CN=www.s5tech.com"
copy %FILENAME%.key + %FILENAME%.crt %FILENAME%.pem
REM chmod 600 $FILENAME.key $FILENAME.pem

@REM 
@REM GENERATE client KEY AND CERTIFICATE questi sono buoni per le connessioni client alla SOCAT
@REM 
SET FILENAME=client
openssl genrsa -out %FILENAME%.key 1024
openssl req -new -key %FILENAME%.key -x509 -days %DAYS% -out %FILENAME%.crt -subj "/C=IT/ST=Italy/L=Milan/O=S5Tech Generic Client/CN=www.s5tech.com"
copy %FILENAME%.key + %FILENAME%.crt %FILENAME%.pem
REM chmod 600 $FILENAME.key $FILENAME.pem

@REM 
@REM CREAZIONE KEYSTORE 
@REM 

@REM 
@REM CREAZIONE DEL CERTIFICATO CLIENT IN FORMATO PKCS12, AL MOMENTO E' L'UNICO CHE DA JAVA CLIENT FUNZIONA CON QUELLI SOPRA GENERATI DELLE SOCAT
@REM 
openssl pkcs12 -export -out temp_keystore.p12 -inkey client.key -in client.crt 
keytool -v -importkeystore -srckeystore temp_keystore.p12 -srcstoretype PKCS12 -destkeystore keystore -deststoretype JKS
del temp_keystore.p12

:_eof
