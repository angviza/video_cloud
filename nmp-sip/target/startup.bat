set base=%~dp0

start "nmp-sip-master" java -classpath %base%classes;%base%libs\*;  com.hdvon.sip.NmpSipApplication