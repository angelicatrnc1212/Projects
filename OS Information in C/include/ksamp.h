//Author: Angelica Charvensym
//Date: 1/22/2024
//Description: Header file for ksamp.c


#ifndef KSAMP_H
#define KSAMP_H

//Define all global variables and function definitions here

void procType();
void kerVersion();
void amTimeLastBooted();
long long int amTimeLastBootedintime();
void timeProcessor();
void timeProcessorTime(int every, double period);
void compDisks();
void compDisksTime(int every, double period);
void contextSwitches();
void contextSwitchesTime(int every, double period);
void lastBootTime();
void numOfProcesses();
void numOfProcessesTime(int every, double period);
void memConfigured();
void memConfiguredTime(int every, double period);
void freeMemory();
void freeMemoryTime(int every, double period);
void ksampTime(int every, double period);
void ksamp();


#endif
