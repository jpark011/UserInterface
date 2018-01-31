#include "displayables.h"

#include <sstream>
#include <iostream>

using namespace std;

void Text::paint(XInfo& xinfo) {
  stringstream ss;
  ss << this->s << level;
    XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
                      this->x, this->y, ss.str().c_str(), ss.str().length() );
}

Text::Text(int x, int y, string s): x(x), y(y), s(s)  {}

void Rectangle::paint(XInfo& xinfo) {
  XFillRectangle(xinfo.display, xinfo.window, xinfo.gc, x, y, w, h); // top left
}

Rectangle::Rectangle(int x, int y, int w, int h) {
    this->x = x;
    this->y = y;
    this->w = w;
    this->h = h;
}

void Rectangle::move(Direction dir, int pix) {
  switch(dir) {
  case LEFT:
    this->x -= pix;
    break;
  case RIGHT:
    this->x += pix;
    break;
  case DOWN:
    this->y += pix;
    break;
  case UP:
    this->y -= pix;
    break;
  }
}

bool Rectangle::offLimit(Direction dir) {
  bool ret = false;
  switch(dir) {
  case LEFT:
    if (x + w < 0) {
      ret = true;
    }
    break;
  case RIGHT:
    if (MAX_WIDTH < x) {
      ret = true;
    }
    break;
  case DOWN:
    if (MAX_HEIGHT < y) {
      ret = true;
    }
    break;
  case UP:
    if (y + h < 0) {
      ret = true;
    }
    break;
  }
  return ret;
}

// overlap?
bool Rectangle::isCollision(Rectangle* r) {
  return (this->y == r->y) &&
    ((r->x <= this->x && this->x <= r->x + r->w) ||
      (r->x <= this->x + this->w && this->x + this->w <= r->x + r->w ) ||
      (this->x <= r->x && r->x + r->w <= this->x + this->w));
}

Frog::Frog(int x, int y, int w, int h) : Rectangle(x, y, w, h) {
}

void Frog::move(Direction dir, int pix) {
  if (!offLimit(dir)) {
    Rectangle::move(dir, pix);
  } else {
    cout <<"OFF LIMIT" << endl;
  }
}

bool Frog::offLimit(Direction dir) {
  bool ret = false;
  switch(dir) {
  case LEFT:
    if (x <= 0) {
      ret = true;
    }
    break;
  case RIGHT:
    if (MAX_WIDTH <= x + w) {
      ret = true;
    }
    break;
  case DOWN:
    if (MAX_HEIGHT <= y + h) {
      ret = true;
    }
    break;
  case UP:
    if (y  <= 0) {
      ret = true;
    }
    break;
  }
  return ret;
}

// in Goal area?
bool Frog::isAtTop() {
  return this->y <= 0;
}

void Frog::toInit() {
  x = MAX_WIDTH/2;
  y = MAX_HEIGHT - 50;
}
