/* ****************************************************************************
 *
 *         S5TECH NETWORK APPLICATION DOCUMENTATION AND LICENSE
 *                     Version 1.6, September 2014
 *                   http://www.s5tech.com/licenses/
 *
 * ****************************************************************************/

S5TECH® SOFTWARE NOTICE AND LICENCES http://www.s5tech.com/

Permission to copy, modify, and distribute this software and its documentation,
with or without modification, for any purpose and without fee or royalty is
hereby granted.

THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.

COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.

The name and trademarks of copyright holders may NOT be used in advertising or
publicity pertaining to the software without specific, written prior permission.
Title to copyright in this software and any associated documentation will at
all times remain with copyright holders.

FOR INFORMATION ABOUT OBTAINING, INSTALLING AND RUNNING THIS SOFTWARE WRITE AN
EMAIL TO assist@s5tech.com

S5Tech Development Team, assist@s5tech.com 2015-01-15


INDEX OF CONTENTS
-----------------

    1) INSTALLATION NOTES

    2) CONFIGURATION NOTES

    3) SSL CERTIFICATES GENERATION

    4) RUNNING NETWORK APPLICATION 

    
1) INSTALL NOTES
----------------

    FROM WINDOWS & OSX
    copy the appropriate system dlls (shared objects) to network application directory
       
       windows 32bit:
       copy dlls from .\lib\rxtx\win32 to .
       
       windows 64bit:
       copy dlls from .\lib\rxtx\win64 to .
       
       from OSX (Tested on OSX 10.5)
       copy *.jnlib from lib/rxtx/mac-10.5 to .
       
       from Linux
       no rxtx dll needed
       
    FROM LINUX
    open bash prompt as root:
     
       1) stty -F /dev/ttyACM0 raw 115200
       2) chmod 777 /dev/ttyACM0

2) CONFIGURATION NOTES
----------------------

    Open with any text editor the main configuration file
    conf/s5.conf and changeù,add or remove needed options.

    or alternatilvely open the web console at url 
    http://localhost:9090/files/config.html

3) SSL CERTIFICATES GENERATION
------------------------------
     
    After installing OpenSSL on your system if not present yet
    open command prompt and follow the instructions...

       1) openssl genrsa -out s5.key 1024
       2) openssl req -new -key s5.key -x509 -days 3653 -out s5.crt
       3) openssl req -new -key s5.key -x509 -days 3653 -out client.crt

    from network application directory... 

       1) cat s5.key client.crt >s5client.pem
       2) openssl pkcs12 -export -in s5client.pem -out conf/s5.p12

    from each hub system...

       1) cat s5.key s5.crt >s5.pem
       2) copiare su HUB in /etc s5.pem e client.crt
       3) socat openssl-listen:4433,reuseaddr,cert=/etc/s5.pem,cafile=/etc/client.crt open:/dev/ttyACM0
   
4) RUNNING  NETWORK APPLICATION 
-------------------------------

    1) RUNNING OPTIONS

    FROM COMMAND LINE AFTER SETTING CLASSPATH, EXECUTE 

    call run.cmd com.s5tech.net.desktop.S5TechDesktopApp 

    to run Esl Network Application

    or alternatively to run complementary functions...

    call run.cmd java com.s5tech.net.desktop.S5TechDesktopApp [config <configuration-file>] [options]

	default configuration files are in order:
	
		./conf/system.properties  (properties format)
		./conf/s5.conf            (conf format)
	
    available options:

    encode 
            [<S5 input-file output-file>]      convert input to output with symmetric local encoder
                | [<hash> <base64-payload>]    create hash code from base64 byte array data
                | [<[MD5|...]> <password>]     digest with specified alg. (e.g. MD5) given password
                    NOTE: for both 2nd and 3rd mode output is given in hexadecimal upper case digits                

    ssh 
            -h <10.1.1.100> 
            -u <root> 
            -w <s5tech123!> 
            -c <"ps ax | grep socat | grep -v grep">  

    shutdown 
            -p <9000> 
            -h <127.0.0.1> 
            -c <./conf/s5.conf> 
            
    client 
            -u <tcp://127.0.0.1:61616> -q <dynamicQueues/UP> -p <activemq|rabbit> 
            -a <publish | receive | count> 
            -i <inputfile>  (only publish)
            -v <schema.xsd> (publish & receive)
            -A (only receive, authorize all, switch on unauthorized events on network app)
            -n (only publish, don't send message, combined with -v to validate only xml file)
            -t <timeout in ms> (only receive) timeout between messages consumption used to 
                    emulate latency backend upstream elaboration

    hubpwd 
            -h <hostname> if not specified, listen for all broadcast notifications see -p
            -p <broadcast port>
            -w <root password>
            -W <new AP password>
        
    logscan
            -G start logviewer graphic user interface
            -o <filename> output 
            -x <schema.xsd> 
            -f <date> YYYY-MM-DDTHH:mm:ss 
            -l <directory> ./logs
            -a <action> dump (d), validate, unauth, zip, view
            -r <filter> format is mac=<eslmac>,coordinator=<mac>... 

    csv2xml    
            -i <input csv file>    
            -o <output xml file>
            -r <xml root element> default: data
            -s <separator> \t for tab, \b for space 

    xmlbuild
            -m <mac list> with separator ',' (alternative to -x option)
            -x <input xml file> data file (alternative to -x option)
            -o <output file>
            -t <xslt template> prepend 'file://' to use filesystem xslt file
            -p <key=value> can be reiterrated ... (optional parameters)

    payload
            -a activation time in format yyyy-MM-dd'T'HH:mm:ss
                    default is null that means zero (01/01/2000) 
                    specifying '-a now' use current time
            -e <epaper pngfile>
            -p <key=value> multiple parameters option
            -f <properties file> for LCD 7SEGMENT ESL (see -s for an example)
            -s show an example of properties file and exit    
            -d <base64data> decode LCD 7SEGMENT ESL data in base64 format
            -o <xml|text> output format when build output ESL message (default is text)
            -c <filepath[#startpos]> calculate hash code from binary file from 
                    optional start position startpos (default is BOF)
            -x <left> position for png file, default and the only running is 0
            -t <top> position for png file, default and the only running is 0
            -P <number> of pages only for epaper (for LCD 7SEG use -p or -f option)
                
    pngcreate
            -w <width> in pixels
            -h <height> in pixels
            -s <mxn> (e.g.: 200x96) same of above two options
            -i <input> text file for png definition (display.txt) see -s for example
            -o <output> pngfile
            -S show example
            
    simulator
            -c or --conf <filename>            configuration .xml or .properties file (default is simulator.properties)
            -j or --jdbc <filename>            jdbc properties configuration file (ignored if xml configuration set)
            -l or --logger-config <filename>   jul logger configuration
            -o or --output-file <filename>     file csv where to save results

    noapp
            initialize extensions and stop before running ESL network application
 