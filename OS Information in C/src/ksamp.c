
//Author: Angelica Charvensym
//Date: 1/22/2024
//Description: Functions to get the state of the system using /proc

#include "ksamp.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

// Function code goes in this file

/***
 * Processor Type taken from /proc/cpuinfo in the model name.
 * The while loop takes each line and compare whether it includes "model name"
*/
void procType(){
    FILE* ptr;
    ptr = fopen("/proc/cpuinfo", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    char line[255];
    char* model = "model name";
    char name[255];
    while(fgets(line, 255, ptr) != NULL){
        if(strstr(line, model) != NULL){
            sscanf(line, "%*s %*s %*s %[^@]", name);
            break;
        }
    }
    if(strlen(name) == 0)
        printf("Processor Type\t\t\t\t: Not Found\n");
    else(printf("Processor Type\t\t\t\t: %s\n", name)); 

}


/***
 * Kernel Version taken from /proc/version
*/
void kerVersion(){
    FILE* ptr;
    char str[256];
    ptr = fopen("/proc/version", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    if(fscanf(ptr, "%*s %*s %s", str)==1)
        printf("Kernel Version\t\t\t\t: %s\n", str);
    else
        printf("Kernel Version\t\t\t\t: Not Found\n");
}


/***
 * Amount of Time since it was last booted taken from /proc/uptime
 * Converts from second to minutes for better vision
 * Returns the prompt and result
*/
void amTimeLastBooted(){
    FILE* ptr;
    double d;
    ptr = fopen("/proc/uptime", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    if(fscanf(ptr, "%lf", &d)==1)
        printf("Minutes Since Last Boot\t\t\t: %.2f minutes\n", d/60);
    else
        printf("Minutes Since Last Boot\t\t\t: Not Found\n");
}


/***
 * Amount of Time since it was last booted taken from /proc/uptime
 * Converts from second to minutes for better vision
 * Returns the amount of second
*/
long long int amTimeLastBootedintime(){
    FILE* ptr;
    long long int d;
    ptr = fopen("/proc/uptime", "r");
    if(ptr == NULL){
        printf("Error!");
        return 0;
    }
    if(fscanf(ptr, "%lld", &d)==1)
        return d;
    else return 0;
}


/***
 * The amount of time the processor has spend in user mode, system mode and idle time.
 * They are all taken from /proc/stat. 
 * User mode is accumulated from time processed in user mode and in nice mode(user mode but below-priority)
 * As the information received are in UNIT_HZ, the report is converted to seconds for better vision
*/
void timeProcessor(){
    FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    int user1, user2, sys, idle;
    if(fscanf(ptr, "%*s %d %d %d %d", &user1, &user2, &sys, &idle) > 0){
        printf("Time processed in user mode\t\t: %.2fs\n", (user1+user2)/100.0);
        printf("Time processed in system mode\t\t: %.2fs\n", sys/100.0);
        printf("Time processed in idle mode\t\t: %.2fs\n", idle/100.0);
        }
    else(printf("Time processed\t\t\t\t: Not Found\n"));
}


/***
 * The average amount of time the processor has spend in user mode, system mode and idle time within the period of time inputed in main function.
 * Samples are taken every inputed number of second.
 * They are all taken from /proc/stat. 
 * User mode is accumulated from time processed in user mode and in nice mode(user mode but below-priority).
 * The results are accumulated from every sample and divided by the amount of sample taken (period/every).
*/
void timeProcessorTime(int every, double period){
	FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    int user1, user2, sys, idle, totalUser, totalSys, totalIdle;
    int avg = period/every;
    
    printf("Taking a sample every %ds for %.2lfs...\n", every, period);
    for(int i = 0; i < avg; i++){
	 fscanf(ptr, "%*s %d %d %d %d", &user1, &user2, &sys, &idle);
	totalUser +=(user1+user2);
	totalSys += sys;
	totalIdle += idle;
	sleep(every);
	}

	printf("Average Time processed in user mode\t\t: %.2fs\n", totalUser/100.0);
	printf("Average Time processed in system mode\t\t: %.2fs\n", totalSys/100.0);
	printf("Average Time processed in idle mode\t\t: %.2fs\n", totalIdle/100.0);
}

/**
 * The number of completed disk read/write made on the system taken from /proc/stat
 * Total completed disk are the sum of disk read and disk write
 * 
 */
void compDisks(){
    FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int read, compRead, write, compWrite;
    char line[255];
    while(fgets(line, 255, ptr) != NULL){
        sscanf(line, "%*d %*d %*s %lld %*d %*d %*d %lld", &read, &write);
        compRead += read;
        compWrite += write;
    }
    
    printf("Number of completed disk read\t: %lld\n", compRead);
    printf("Number of completed disk write\t: %lld\n", compWrite);
    printf("Number of total completed disk\t: %lld\n", (compRead+compWrite));
}

/**
 * The average number of completed disk read/write made on the system taken from /proc/stat
 * The result are average number of disk taken from every sample
 * Total completed disk are the sum of disk read and disk write
 * 
 */
void compDisksTime(int every, double period){
	FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int read, compRead, write, compWrite;
	int avg = period/every;
    char line[255];
    
    printf("Taking a sample every %ds for %lfs...\n", every, period);
    for(int i = 0; i < avg; i++){
	    while(fgets(line, 255, ptr) != NULL){
		sscanf(line, "%*d %*d %*s %lld %*d %*d %*d %lld", &read, &write);
		compRead += read;
		compWrite += write;
	    }
	sleep(every);
    }
    
    printf("Average Number of completed disk read\t: %lld\n", compRead/avg);
    printf("Average Number of completed disk write\t: %lld\n", compWrite/avg);
    printf("Average Number of total completed disk\t: %lld\n", (compRead+compWrite)/avg);

}

/***
 * The number of context switches the kernel has performed taken from /proc/stat
 * Information is taken from the line ctxt 
*/
void contextSwitches(){
    FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int context = -1;
    char line[255];
    char* ctxt = "ctxt";
    while(fgets(line, 255, ptr) != NULL){
        if(strstr(line, ctxt) != NULL){
            sscanf(line, "%*s %lld", &context);
            break;
        }
    }
    if(context == -1)
        printf("Number of Context Switches: Not Found\n");
    else(printf("Number of Context Switches:\t\t %lldkB\n", context)); 
}


/**
 *  The average number of context switches the kernel has performed taken from /proc/stat
 *  Average number is taken from the sample over the period time of every n seconds
 *  Information is taken from the line ctxt 
 *  
 */
void contextSwitchesTime(int every, double period){
	FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int context = -1;
    long long int total = 0;
    int avg = period/every;
    char line[255];
    char* ctxt = "ctxt";
    
    printf("Taking a sample every %ds for %lfs...\n", every, period);
    for(int i = 0; i < avg; i++){
	    while(fgets(line, 255, ptr) != NULL){
		if(strstr(line, ctxt) != NULL){
		    sscanf(line, "%*s %lld", &context);
		    break;
		}
	    }
	  total += context;
	  sleep(every); 
    }
    
    if(context == -1)
        printf("Number of Context Switches: Not Found\n");
    else(printf("Average Number of Context Switches:\t %lldkB\n", total/avg)); 
}


/***
 * Time the system was last booted is the difference from current time and the amount of time the system is last booted
*/
void lastBootTime(){
    time_t now;
    time(&now);
    time_t uptime = amTimeLastBootedintime();
    time_t lastBoot = now - uptime;

    printf("Last booted at\t\t\t\t: %s", ctime(&lastBoot));
}

/***
 * The number of processes that have been created since the last boot taken from /proc/stat
 * The while loop takes each line and compare whether it includes "processes"
*/
void numOfProcesses(){
    FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int process=-1;
    char line[255];
    char* processes = "processes";
    while(fgets(line, 255, ptr) != NULL){
        if(strstr(line, processes) != NULL){
            sscanf(line, "%*s %lld", &process);
            break;
        }
    }
    if(process == -1)
        printf("Number of Processes\t\t\t: Not Found\n");
    else(printf("Number of Processes\t\t\t: %lldkB\n", process)); 
}


/**
 * The average number of processes that have been created since the last boot taken from /proc/stat
 * The while loop takes each line and compare whether it includes "processes".
 * The while loop process is done until we could the accumulated number of processes across all samples to get the average number
 * 
*/
void numOfProcessesTime(int every, double period){
	FILE* ptr;
    ptr = fopen("/proc/stat", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int process=-1;
    long long int avg = period/every;
    long long int total = 0;
    char line[255];
    char* processes = "processes";
    
    printf("Taking a sample every %ds for %lfs...\n", every, period);
    for(int i = 0; i < avg; i++){
	    while(fgets(line, 255, ptr) != NULL){
		if(strstr(line, processes) != NULL){
		    sscanf(line, "%*s %lld", &process);
		    break;
		}
	    }
	    total += process;
	    sleep(every);
	}
    if(process == -1)
        printf("Average Number of Processes\t\t: Not Found\n");
    else(printf("Average Number of Processes\t\t: %lldkB\n", total/avg)); 
}

/**
 * The amount of memory configured for this computer taken from /proc/meminfo
 * The while loop takes each line and compare whether it includes "MemTotal:"
 * 
 */
void memConfigured(){
    FILE* ptr;
    ptr = fopen("/proc/meminfo", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int memory=-1;
    char line[255];
    char* memTotal = "MemTotal:";
    
    
    while(fgets(line, 255, ptr) != NULL){
        if(strstr(line, memTotal) != NULL){
            sscanf(line, "%*s %lld", &memory);
            break;
        }
    }
    if(memory == -1)
        printf("Memory configured\t\t\t: Not Found\n");
    else(printf("Memory configured\t\t\t: %lld kB\n", memory)); 
}

/**
 * The average amount of memory configured for this computer taken from /proc/meminfo
 * The while loop takes each line and compare whether it includes "MemTotal:"
 * The while loop process is done until we could the accumulated number of processes across all samples to get the average number
*/
void memConfiguredTime(int every, double period){
	FILE* ptr;
    ptr = fopen("/proc/meminfo", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int memory=-1;
    long long int avg = period/every;
    long long int total = 0;
    char line[255];
    char* memTotal = "MemTotal:";
    
    printf("Taking a sample every %ds for %lfs...\n", every, period);
    for(int i = 0; i < avg; i++){
	    while(fgets(line, 255, ptr) != NULL){
		if(strstr(line, memTotal) != NULL){
		    sscanf(line, "%*s %lld", &memory);
		    break;
		}
	    	}
	    total += memory;
	    sleep(every);
    }
    if(memory == -1)
        printf("Average Memory configured\t\t: Not Found\n");
    else(printf("Average Memory configured\t\t: %lld kB\n", total/avg)); 
}


/**
 * @brief the amount of free memory on this system taken from /proc/meminfo
 * 
 */
void freeMemory(){
    FILE* ptr;
    ptr = fopen("/proc/meminfo", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int memory=-1;
    char line[255];
    char* memFree = "MemFree:";
    while(fgets(line, 255, ptr) != NULL){
        if(strstr(line, memFree) != NULL){
            sscanf(line, "%*s %lld", &memory);
            break;
        }
    }
    if(memory == -1)
        printf("Free memory\t\t\t\t: Not Found\n");
    else(printf("Free memory\t\t\t\t: %lld kB\n", memory)); 
}

/***
 * The average amount of free memory on this system taken from /proc/meminfo
*/
void freeMemoryTime(int every, double period){
	FILE* ptr;
    ptr = fopen("/proc/meminfo", "r");
    if(ptr == NULL){
        printf("Error!");
        return;
    }
    long long int memory=-1;
    char line[255];
    long long int avg = period/every;
    long long int total = 0;
    char* memFree = "MemFree:";
    
    printf("Taking a sample every %ds for %lfs...\n", every, period);
    for(int i = 0; i < avg; i++){
	    while(fgets(line, 255, ptr) != NULL){
		if(strstr(line, memFree) != NULL){
		    sscanf(line, "%*s %lld", &memory);
		    break;
		}
	    }
	    total += memory;
	    sleep(every);
    }
    if(memory == -1)
        printf("Average Free memory\t\t\t: Not Found\n");
    else(printf("Average Free memory\t\t\t: %lld kB\n", total/avg)); 
}

/**
 * @brief Prints out all of the functions to create the needed report about the system
 * Some functions are different then no-arg main function because of averaging
 * 
 * @param every Getting samples on every n second
 * @param period Getting samples over a n period of time
 */
void ksampTime(int every, double period){
	procType();
 	kerVersion();
	amTimeLastBooted();
	timeProcessorTime(every, period);
 	compDisksTime(every, period);
 	contextSwitches(every, period);
 	lastBootTime();
 	numOfProcessesTime(every, period);
 	memConfiguredTime(every, period);
 	freeMemoryTime(every, period);
 	
}

/**
 * @brief Prints out all of the functions to create the needed report about the system
 * 
 */
void ksamp(){
    procType();
    kerVersion();
    amTimeLastBooted();
    timeProcessor();
    compDisks();
    contextSwitches();
    lastBootTime();
    numOfProcesses();
    memConfigured();
    freeMemory();

}
