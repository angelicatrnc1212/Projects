//Author: Angelica Charvensym
//Date: 1/25/2024
//Description: Main function that may receives parameter to gets the average over a time interval
//Parameters: Taking sample every n seconds, for n period of second

#include "ksamp.h"
#include "ksamp.c"
#include <unistd.h>
#include <stdlib.h>


int main(int argc, char **argv){

	// main function goes here 
	if(argc != 3)
		ksamp();
	else{
		ksampTime(strtol(argv[1], NULL, 10), strtol(argv[2], NULL, 10));
	}
		

	return 0;
}
