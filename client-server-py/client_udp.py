import socket
import sys

UDP_IP = "::1"
UDP_PORT = 5005

while True: 
	try:
		MESSAGE = bytes(input(), "utf-8")
	except KeyboardInterrupt:
		print ("Client process was interrupted.")
		break

	sock = socket.socket(socket.AF_INET6, socket.SOCK_DGRAM) # UDP
	sock.sendto(MESSAGE, (UDP_IP, UDP_PORT))