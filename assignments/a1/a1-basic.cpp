/*
CS 349 Code Examples: X Windows and XLib

    drawing     Demos drawing functions and graphics contexts

- - - - - - - - - - - - - - - - - - - - - -

See associated makefile for compiling instructions

*/

#include <iostream>
#include <list>
#include <cstdlib>
#include <unistd.h>
#include <vector>
#include <sys/time.h>
#include <string>
#include <sstream>

// Header files for X functions
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include "configs.h"
#include "displayables.h"

using namespace std;

// Function to repaint a display list
void repaint( list<Displayable*> dList, XInfo& xinfo) {
    list<Displayable*>::const_iterator begin = dList.begin();
    list<Displayable*>::const_iterator end = dList.end();

    XClearWindow( xinfo.display, xinfo.window );
    while ( begin != end ) {
        Displayable* d = *begin;
        d->paint(xinfo);
        begin++;
    }
    XFlush( xinfo.display );
}


// The loop responding to events from the user.
void eventloop(XInfo& xinfo) {
    XEvent event;
    KeySym key;
    char text[BufferSize];
    list<Displayable*> dList;

    // frog
    Frog* frog = new Frog(MAX_WIDTH/2, MAX_HEIGHT - 50, 50, 50);
    dList.push_back(frog);

    // level Text
    Text* lvl_txt = new Text(MAX_WIDTH-100, 20, "Level: ");
    dList.push_back(lvl_txt);

    vector<Rectangle*>::iterator it;
    int i;  // second counter for loop
    // obstacles row 1
    vector<Rectangle*> row1(3);
    for (i=0, it = row1.begin(); it != row1.end(); i++, it++) {
      (*it) = new Rectangle(MAX_WIDTH/3 * i, 50, 50, 50);
      dList.push_back(*it);
    }

    // obstacles row 2
    vector<Rectangle*> row2(4);
    for (i=0, it = row2.begin(); it != row2.end(); i++, it++) {
      (*it) = new Rectangle(MAX_WIDTH/4 * i, 100, 20, 50);
      dList.push_back(*it);
    }

    // obstacles row 3
    vector<Rectangle*> row3(2);
    for (i=0, it = row3.begin(); it != row3.end(); i++, it++) {
      (*it) = new Rectangle(MAX_WIDTH/2 * i, 150, 100, 50);
      dList.push_back(*it);
    }

    // time of last window paint
  	unsigned long lastRepaint = 0;

    while ( true ) {
      if (XPending(xinfo.display) > 0) {
        XNextEvent( xinfo.display, &event );

        switch ( event.type ) {
          case KeyPress:
            int i = XLookupString(
              (XKeyEvent*)&event, text, BufferSize, &key, 0 );
              // cout << "KeySym " << key
              // << " text='" << text << "'"
              // << " at " << event.xkey.time
              //
              // << endl;
              if ( i == 1 && text[0] == 'q' ) {
                cout << "Terminated normally." << endl;
                XCloseDisplay(xinfo.display);
                return;
              }
              // new level?
              if (text[0] == 'n' && frog->isAtTop()) {
                cout << "Level up";
                frog->move(DOWN, 200);
                level++;
              }

              switch(key){
                case XK_Up:
                frog->move(UP, 50);
                break;
                case XK_Down:
                if (!frog->isAtTop())
                  frog->move(DOWN, 50);
                break;
                case XK_Left:
                frog->move(LEFT, 50);
                break;
                case XK_Right:
                frog->move(RIGHT, 50);
                break;
              }

              break;
              }
      }

      unsigned long end = now();	// get time in microsecond

  		if (end - lastRepaint > 1000000 / FPS) {
  			// clear background
  			//XClearWindow(xinfo.display, xinfo.window);
        // move row1
        for (it = row1.begin(); it != row1.end(); it++) {
          (*it)->move(RIGHT, level);
          if ((*it)->offLimit(RIGHT)) {
            (*it)->move(LEFT, MAX_WIDTH);
          }
          // did collide?
          if (frog->isCollision(*it)) {
            frog->toInit();
            level = 1;
          }
        }
        // move row2
        for (it = row2.begin(); it != row2.end(); it++) {
          (*it)->move(LEFT, level);
          if ((*it)->offLimit(LEFT)) {
            (*it)->move(RIGHT, MAX_WIDTH);
          }
          // did collide?
          if (frog->isCollision(*it)) {
            frog->toInit();
            level = 1;
          }
        }

        // move row1
        for (it = row3.begin(); it != row3.end(); it++) {
          (*it)->move(RIGHT, level);
          if ((*it)->offLimit(RIGHT)) {
            (*it)->move(LEFT, MAX_WIDTH);
          }
          // did collide?
          if (frog->isCollision(*it)) {
            frog->toInit();
            level = 1;
          }
        }


  			XFlush( xinfo.display );
        repaint(dList, xinfo);
  			lastRepaint = now(); // remember when the paint happened
  		}

    }
}


// Initialize X and create a window
void initX(int argc, char *argv[], XInfo &xinfo) {
	// Display opening uses the DISPLAY	environment variable.
	// It can go wrong if DISPLAY isn't set, or you don't have permission.
	xinfo.display = XOpenDisplay( "" );
	if ( !xinfo.display )	{
		error( "Can't open display." );
	}

  // set FPS
  if (argc > 1) {
    stringstream ss(argv[1]);
    ss >> FPS;
  }

	// Find out some things about the display you're using.
  int screen = DefaultScreen( xinfo.display );
  unsigned long white = WhitePixel( xinfo.display, screen );
  unsigned long black = BlackPixel( xinfo.display, screen );

  XSizeHints hints;
	hints.x = 100;
	hints.y = 100;
	hints.width = MAX_WIDTH;
	hints.height = MAX_HEIGHT;
	hints.flags = PPosition | PSize;

	xinfo.window = XCreateSimpleWindow(
	                   xinfo.display,				// display where window appears
	                   DefaultRootWindow( xinfo.display ), // window's parent in window tree
	                   hints.x, hints.y,			// upper left corner location
	                   hints.width, hints.height,	// size of the window
	                   5,						// width of window's border
	                   black,						// window border colour
	                   white );					// window background colour

	XSetStandardProperties(
	    xinfo.display,		// display containing the window
	    xinfo.window,		// window whose properties are set
	    "Frog",			// window's title
	    "FG",				// icon's title
	    None,				// pixmap for the icon
	    argv, argc,			// applications command line args
	    &hints );			// size hints for the window

  xinfo.gc = XCreateGC (xinfo.display, xinfo.window, 0, 0 );
  XSetBackground( xinfo.display, xinfo.gc, white );
  XSetForeground( xinfo.display, xinfo.gc, black );

  // Tell the window manager what input events you want.
  // ButtomMotionMask: The client application receives MotionNotify events only when at least one button is pressed.
  XSelectInput( xinfo.display, xinfo.window,
                KeyPressMask );

	// Put the window on the screen.
	XMapRaised( xinfo.display, xinfo.window );

	XFlush(xinfo.display);

    // give server time to setup before sending drawing commands
    sleep(1);
}

int main ( int argc, char* argv[] ) {
    XInfo xinfo;
    initX(argc, argv, xinfo);
    eventloop(xinfo);

    return 0;
}
