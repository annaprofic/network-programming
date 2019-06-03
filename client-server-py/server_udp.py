import socket
  
UDP_IP = "::" 
UDP_PORT = 5005

sock = socket.socket(socket.AF_INET6, socket.SOCK_DGRAM) # UDP
sock.bind((UDP_IP, UDP_PORT))

while True:
	try:
		data, addr = sock.recvfrom(1024)
	except KeyboardInterrupt:
		print ("Server process was interrupted.")
		break
	print ("Received message:", str(data, "utf-8"))