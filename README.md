# User Interface
UI practices using C++ X11, Java Swing/AWT, and Android

### X11
Using X11 library of C++, __Frog Game__ is created.
- It's real-time graphics (_double buffer_ is supported)

### Swing/AWT
Using Swing(lightweight)/AWT(heavyweight) library of Java, __Paint App__ is created.
- __v1:__ 
  - can draw circles, rectangles, lines
  - supports __hit test__
  - can change color/thickness/size 
  - supports rotate/scale/translate using __Affine Transformation Matrix__
  
- __v2:__
  - same as _v1_
  - supports on screen scale/rotate (__direct manipulation__)
  - supports undo/redo using __UndoManager__ (forward stack based)
