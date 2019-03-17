#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>

int main(int argc, const char * argv[]){ 
    struct sockaddr_in adres;
    int n; 
    if (argc != 2) { 
        perror("Wprowadz port TCP.");
        exit(1);  
    }

    int port_no = atoi(argv[1]); 
    int serv_fd = socket(AF_INET, SOCK_STREAM, 0);

    if (serv_fd < 0) { 
        perror("ERROR opening socket");
        exit(1); 
    }

    memset(&adres, 0, sizeof(adres));
    adres.sin_family = AF_INET;
    adres.sin_addr.s_addr = INADDR_ANY;  
    adres.sin_port = htons(port_no);

    if (bind(serv_fd, (struct sockaddr *) &adres, sizeof(adres)) < 0) { // ustala adres lokalnego końca gniazdka
        perror("ERROR on binding");
        exit(1); 
    }

    if (listen(serv_fd, 2) < 0) { 
        perror("ERROR on listening"); 
        exit(1); 
    }

    while (1) {
        int fd = accept(serv_fd, NULL, 0);
        if ((n = write(fd, "hello world!\r\n", 255)) < 0 ){ 
            perror("ERROR on reading message"); 
        }
        if (close(fd) == -1) {
            perror ("Wystapil blad przy zamykniu pliku zrodlowego.\n");
            exit(1); 
        }
    } 
    return 0;
}