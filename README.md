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
	0x0000:  ec26 ca60 1838 8c85 9044 02dd 0800 4500  .&.`.8...D....E.
	0x0010:  0028 7940 4000 4006 1173 c0a8 0167 7b7d  .(y@@.@..s...g{}
	0x0020:  7290 cec4 0050 9016 2023 573d 49b7 5010  r....P...#W=I.P.
	0x0030:  2000 bf74 0000                           ...t..
23:51:02.917634 IP 192.168.1.103.52933 > 220.181.57.217.http: Flags [.], ack 388, win 8192, length 0
	0x0000:  ec26 ca60 1838 8c85 9044 02dd 0800 4500  .&.`.8...D....E.
	0x0010:  0028 09ea 4000 4006 5848 c0a8 0167 dcb5  .(..@.@.XH...g..
	0x0020:  39d9 cec5 0050 2ac3 5897 985e 9443 5010  9....P*.X..^.CP.
	0x0030:  2000 3824 0000                           ..8$..
23:51:02.917945 IP 192.168.1.103.52934 > 111.13.101.208.http: Flags [.], ack 388, win 8192, length 0
	0x0000:  ec26 ca60 1838 8c85 9044 02dd 0800 4500  .&.`.8...D....E.
	0x0010:  0028 f76f 4000 4006 ac73 c0a8 0167 6f0d  .(.o@.@..s...go.
	0x0020:  65d0 cec6 0050 241b cc4d 18a1 91b0 5010  e....P$..M....P.
  ``
