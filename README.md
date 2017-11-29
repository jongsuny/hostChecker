# hostChecker

basic function runs well.

## supports 
http/https

## Testify:
1. run HostCheckerTest

2. run below command and watch tcp connections.

`` sudo /usr/sbin/tcpdump -X  'tcp dst port 443 or dst port 80' ``

``
23:51:02.914629 IP 192.168.1.103.52932 > 123.125.114.144.http: Flags [.], ack 388, win 8192, length 0

23:51:02.917634 IP 192.168.1.103.52933 > 220.181.57.217.http: Flags [.], ack 388, win 8192, length 0

23:51:02.917945 IP 192.168.1.103.52934 > 111.13.101.208.http: Flags [.], ack 388, win 8192, length 0
  ``
