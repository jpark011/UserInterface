# CS349 A2
Student: hj8park
Marker: Gustavo Fortes Tondello


Total: 54 / 60 (90.00%)

Code: 
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:   

## REQUIREMENTS

1. [3/3] The main user interface follows the specifications regarding size and look and feel.

2. [8/8] In the drawing (insert) mode, a mouse drag event draws a shape on the canvas.

3. [8/8] In the selection mode, the user can click on an existing shape to select it for modification.

4. [2/2] Only one shape can be selected at once.

5. [2/2] When a shape is selected, the application must provide a visual indication of selection.

6. [2/2] Shape: the toolbar must have a a drop-down menu that allows the user to select the type of shape to draw next (freeform, straight line, rectangle, or ellipse).

7. [2/2] Menu File -> New will allow users to draw on a blank canvas, i.e., it will clear the current contents of the canvas.

8. [2/2] Menu File -> Exit will close the application.

9. [2/2] Menu Edit -> Selection Mode (and similar tool bar button) should change the editor into the Selection mode.

10. [2/2] Menu Edit -> Drawing Mode (and similar tool bar button) should change the editor into the Drawing mode.

11. [2/2] Menu Edit -> Delete should delete the currently selected shape.

12. [4/6] Menu Edit -> Transform will show a dialog window that allows the user to transform the currently selected shape (translation, scaling, and rotation).

-2 Rotation and/or scaling do not happen about the centre of the shape

13. [3/3] Menu Format -> Stroke width (and similar tool bar widget) is used to set/change the stroke of a shape.

14. [3/3] Menu Format -> Fill color (and similar tool bar widget) is used to set/change the fill color of a closed shape (rectangle/ellipse).

15. [3/3] Menu Format -> Stroke color (and similar tool bar widget) is used to set/change the stroke color of a shape.


## MVC REQUIREMENTS [5/5]

## ENHANCEMENT [0/3]

-3 no enhancement at all

## GENERAL [1/2] (can be negative)

-1 When using the menus/toolbar buttons, the drawings in the canvas shift position (possibly missing double buffering)
