//Author: Angelica Charvensym
//Date: 2/9/2024
//Description: Function signatures for the minishell.c file


#ifndef MINISHELL_H
#define MINISHELL_H

//Define all global variables and function definitions here


void loop(void);
int checkPipe(int len, char* arg[]);
int parse(char*, char* arg[]);


#endif
