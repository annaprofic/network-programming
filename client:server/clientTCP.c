#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

void check_ascii(void * buf, int len){ 
   int i = 0; 
    int ch = 0; 
    for (; i < len; i++){ 
        ch = ((char *)buf)[i];  
        if (ch >= 32 || ch <= 126){
            printf("%c", ch);
        }
    } 
}

int main(int argc, const char * argv[]) { 
    int fd, len; 
    struct sockaddr_in adres; 
       if (argc != 3) { 
        perror("Wprowadz port TCP.");
        exit(1);  
    }

    int port_no = atoi(argv[1]); 
    const char * buf = ""; 

    fd = socket(AF_INET, SOCK_STREAM, 0); 
    if (fd == -1) { 
        perror("socket creation failed...\n"); 
        exit(1); 
    } 
    
    adres.sin_family = AF_INET; 
    adres.sin_addr.s_addr = inet_addr(argv[2]); 
    adres.sin_port = htons(port_no); 
  
    if (connect(fd, (struct sockaddr *)&adres, sizeof(adres)) != 0) { 
        perror("connection with the server failed...\n"); 
        exit(1); 
    } 

    if ((len = read(fd, &buf, 255)) < 0){ 
        perror("ERROR on writing message"); 
        exit(1); 
    }

    check_ascii(&buf, len);  
      
    if (close(fd) == -1) {
        perror ("Wystapil blad przy zamykniu pliku zrodlowego.\n");
        exit(1);
    }
    return 0;
} 