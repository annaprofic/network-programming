#Definicja zmiennych z opcjami kompilatora/linkera
CFLAGS  := -O2 -Wall
CC := gcc

#Dyrektywa ktora mowi ze dane regula nie jest nazwa pliku
.PHONY: all clean

#Regula domyslna ktora kompiluje wszystkie programy
all: server.x client.x server_udp.x client_udp.x


#Cele i zaleznosci mozemy zastapic zmiennymi automatycznymi dla optymalizacji zapisu
%.x: %.o
	$(CC) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

#Regula, ktora usuwa pliki tymczasowe i wykonywalne
clean:
	$(RM) *.o *.x