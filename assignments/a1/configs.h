#ifndef __CONFIGS_H__
#define __CONFIGS_H__

#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <string>

using namespace std;

typedef enum {
  LEFT,
  RIGHT,
  UP,
  DOWN
} Direction;

extern const unsigned int MAX_WIDTH;
extern const unsigned int MAX_HEIGHT;
extern const int Border;
extern const int BufferSize;
// frames per second to run animation loop
extern int FPS;
extern int level;

// handy struct to save display, window, and screen
struct XInfo {
    Display*  display;
    Window   window;
    GC       gc;
};

// get microseconds
unsigned long now();


// Function to put out a message on error and exits
void error( std::string str );

#endif
