
//Author: Angelica Charvensym
//Date: 2/9/2024
//Description: Consists of functions to help make the loop works like a shell

#include "minishell.h"
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

// Function code goes in this file


/**
 * Primary function that consists of the infinite loop that works like a shell
 * Starts every loop with '$' and waits for a command to be inputed by user
 * Parses the input using parse function
 * Exits only if the first word is exit
 * 
 * INPUT: -
 * OUTPUT: -
 * 
*/
void loop(void){

    char* exit = "exit"; 
    char* ch = "ch";
    char* ampersand = "&";
    const int WRITE_END = 1;
    const int READ_END = 0;
    while(1){
        printf("$");
        char input[100];
        fgets(input, 100, stdin);
        input[strlen(input)-1] = '\0';
        char* arg[100];
        char s[100];

        int len = parse(input, arg);

        if(strcmp(arg[0], exit) == 0){
            printf("Exiting program...\n\n");
            return;
        }else if(strcmp(arg[0], ch) == 0){
            printf("Your current directory:\n%s\n\n", getcwd(s, 100));
            if(chdir(arg[1]) == -1){
                printf("Error on changing current directory, continuing program..\n\n");
            }else{
                printf("Your changed directory:\n%s\n\n", getcwd(s, 100));
            }
            
            continue;
        }
        
        int pipeLoc = checkPipe(len, arg);
        int run;
        if(pipeLoc != -1){
            int fd[2];

            if(pipe(fd) == -1){ //make pipe
                printf("Error on creating pipe, continuing program..\n\n");
                continue;
            }
            pid_t pid = fork();

            if(pid < 0){
                printf("Error on fork, continuing program..\n\n");
                continue;
            }
            if(pid == 0){
                close(fd[0]);
                dup2(fd[1], 1);
                close(fd[1]);

                char* arg1[100];
                for(int i = 0 ; i < pipeLoc; i++){
                    arg1[i] = arg[i];
                }

                arg1[pipeLoc] = NULL;
                run = execvp(arg1[0], arg1);
                printf("Error! Process can't run, exiting program.\n");
            }
            
            pid_t pid2 = fork();
            if(pid2 < 0){
                printf("Error on fork, continuing program..\n\n");
                continue;
            }
            if(pid2 == 0){
                close(fd[1]);
                dup2(fd[0], 0);
                close(fd[0]);
                char* arg2[100];
                int i = 0;
                for(int k = pipeLoc+1 ;k < len; i++, k++){
                    arg2[i] = arg[k];
                }
                arg2[i] = NULL;
                run = execvp(arg2[0], arg2);
                printf("Error! Process can't run, exiting program.\n");
            }

                close(fd[1]);
                close(fd[0]);
                waitpid(pid, NULL, 0);
                waitpid(pid2, NULL, 0);
                
                printf("Done waiting\n\n");
        }else{
            pid_t pid = fork();
            if(pid < 0){
                printf("Error on fork, continuing program..\n\n");
                continue;
            }

            if(pid == 0){
                run = execvp(arg[0], arg);
                printf("Error! Process can't run, exiting program.\n");
                break;
            }

            if(pid > 0){
                wait(NULL);
                printf("Done waiting\n\n");
            }
        }
    }

	waitpid(-1, NULL, 0);
}


/***
 * Checks whether there is a '|' in the string by going through each word, and compares it with '|'
 * INPUT: int length of the array, char* arg[] array of characters
 * OUTPUT: returns -1 if there's no '|', returns the index of the '|' if found
 * 
*/
int checkPipe(int len, char* arg[]){
    int i = -1;
    for(int j = 0; j < len; j++){
        if(strcmp(arg[j], "|") == 0){
            i = j;
            break;
        }
            
    }
    return i;
}


/***
 * Parses the string char* input to an array char* arg[] by delimiter space " "
 * INPUT: the string, the outputed array
 * OUTPUT: returns 0 if it did not parses the string, returns the length of array if the array is filled
*/
int parse(char* input, char* arg[]){
    char* word = strtok(input, " ");
    int len = 0;
    while(word != NULL){
        arg[len++] = word;
        word = strtok(NULL, " ");
    }
    arg[len] = NULL;
    return len;
}



