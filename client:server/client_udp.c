#include <stdio.h> 
#include <strings.h> 
#include <sys/types.h> 
#include <arpa/inet.h> 
#include <sys/socket.h> 
#include<netinet/in.h> 
#include<unistd.h> 
#include<stdlib.h> 
  

int main(int argc, const char * argv[]) {    
    char buffer[100]; 
    char *message = "Server connect"; 
    int sockfd; 
    struct sockaddr_in servaddr; 
      

    bzero(&servaddr, sizeof(servaddr)); 
    servaddr.sin_addr.s_addr = inet_addr("127.0.0.1"); 
    servaddr.sin_port = htons(atoi(argv[1])); 
    servaddr.sin_family = AF_INET; 
      
  
    sockfd = socket(AF_INET, SOCK_DGRAM, 0); 
      
 
    if(connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0) { 
        perror("\n Error : Connect Failed \n"); 
        exit(1); 
    } 

    if(sendto(sockfd, message, 255, 0, (struct sockaddr*)NULL, sizeof(servaddr)) == -1){ 
        perror("ERROR in SEND"); 
        exit(1); 
    }
      
  
    if (recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)NULL, NULL) < 0){ 
        perror("ERROR in RECV"); 
        exit(1); 
    }

    puts(buffer); 

    if (close(sockfd) == -1){ 
        perror ("Wystapil blad przy zamykniu pliku zrodlowego.\n");
        exit(1); 
    }
} 