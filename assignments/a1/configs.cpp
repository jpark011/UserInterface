#include "configs.h"
#include <iostream>
#include <sys/time.h>
#include <cstdlib>


const unsigned int MAX_WIDTH = 850;
const unsigned int MAX_HEIGHT = 250;
const int Border = 5;
const int BufferSize = 10;
// frames per second to run animation loop
int FPS = 30;
int level = 1;


// get microseconds
unsigned long now() {
	timeval tv;
	gettimeofday(&tv, NULL);
	return tv.tv_sec * 1000000 + tv.tv_usec;
}


// Function to put out a message on error and exits
void error( string str ) {
	cerr << str << endl;
	exit(0);
}
