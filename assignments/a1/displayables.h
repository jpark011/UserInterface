#ifndef __DISPLAYABLES_H__
#define __DISPLAYABLES_H__

#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <string>
#include "configs.h"

using namespace std;

// An abstract class representing displayable things.
class Displayable {
public:
    virtual void paint(XInfo& xinfo) = 0;
};

// A text displayable (lvl specific)
class Text : public Displayable {
public:
    virtual void paint(XInfo& xinfo);
    Text(int x, int y, string s);

private:
    int x;
    int y;
    string s; // string to show
};

// A displayable Rectangle
class Rectangle : public Displayable {
public:
    virtual void paint(XInfo& xinfo);

    Rectangle(int x, int y, int w, int h);

    virtual void move(Direction dir, int pix);

    virtual bool offLimit(Direction dir);

    // overlap?
    bool isCollision(Rectangle* r);

protected:
    int x;
    int y;
    int w;
    int h;
};

// A displayable Frog
class Frog : public Rectangle {
public:
    Frog(int x, int y, int w, int h);

    void move(Direction dir, int pix);

    bool offLimit(Direction dir);

    // in Goal area?
    bool isAtTop();

    void toInit();
};

#endif
