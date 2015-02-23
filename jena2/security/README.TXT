GENERAZIONE CERTIFICATI SERVER + CLIENT
---------------------------------------

USARE LO SCRIPT 
createcert.cmd

MODIFICARE EVENTUALMENTE I PARAMETRI 

SOCAT SSL COMMUNICATION TEST
----------------------------

Il server:
socat openssl-listen:4433,reuseaddr,cert=./security/server.pem,cafile=./security/client.crt echo

Il client: 
socat stdio openssl-connect:127.0.0.1:4433,cert=./security/client.pem,cafile=./security/server.crt

