#include <stdio.h> 
#include <strings.h> 
#include <stdlib.h>
#include <sys/types.h> 
#include <arpa/inet.h> 
#include <sys/socket.h> 
#include<netinet/in.h> 

int main(int argc, const char * argv[]) {    
    char buffer[100]; 
    char *message = "Client connect"; 
    int listenfd; 
    socklen_t len; 
    struct sockaddr_in servaddr, cliaddr; 
    bzero(&servaddr, sizeof(servaddr)); 

    listenfd = socket(AF_INET, SOCK_DGRAM, 0);         
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY); 
    servaddr.sin_port = htons(atoi(argv[1])); 
    servaddr.sin_family = AF_INET;  
   
    bind(listenfd, (struct sockaddr*)&servaddr, sizeof(servaddr)); 
    
    len = sizeof(cliaddr); 
    int n = recvfrom(listenfd, buffer, sizeof(buffer), 0, (struct sockaddr*) &cliaddr, &len);  
    if (n < 0){ 
        perror("ERROR in RECV"); 
        exit(1); 
    }

    buffer[n] = '\0'; 
    puts(buffer); 
        
    if (sendto(listenfd, message, 255, 0, (struct sockaddr*) &cliaddr, sizeof(cliaddr)) == -1){ 
        perror("ERROR in SEND"); 
        exit(1); 
    }

    return 0; 
} 