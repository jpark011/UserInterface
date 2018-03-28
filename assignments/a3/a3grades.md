# CS349 A3
Student: hj8park
Marker: Bahar Sarrafzadeh


Total: 22 / 28 (78.57%)

Code: 
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:   

## REQUIREMENTS

1. [2/2] A user can select any shape in the canvas by clicking on it. Show the selected shape with an oriented bounding box as well as handles for scaling and rotation. The user clicks upon a blank area of the canvas to deselect the shape. Upon creation of a new shape, whether by a tool or duplication, the shape is selected. Lines can be selected by clicking within 10 pixels of the line. Selections continue to work on transformed shapes.

2. [2/2] The rotation handle appears 10 pixels above the shape's oriented bounding box, and rotates with the shape. The scale handle appears on the bottom right corner of the oriented bounding box and matches the shape's rotation.

3. [2/2] A user can directly translate a shape by first selecting it and then dragging it. The shape is translated with the mouse during the drag.

4. [0/2] A shape can be scaled by dragging the scale handle. The shape scales according to where the handle is dragged.

-1 scale doesn't work after rotation: it jumps when scaling is attempted after rotation
-1 cannot scale negatively to flip the shape: shape disappears

5. [2/2] A shape can be rotated by pressing the mouse button down on the rotate handle and dragging. The shape is rotated about its center and the rotation handle stays under the mouse cursor as the shape rotates.

6. [2/2] All transformations can be performed on the same shape and the same type of transformation be applied more than one. For example, you could first rotate the shape, and then scale this rotated shape and finally rotate the rotated and translated shape again.

7. [2/2] When a shape is selected, a user can click "duplicate", which will copy the currently selected shape and place it 10px down and to the right of the duplicated shape. After creation the shape will be selected. Duplicated shapes keep the applied transformations.

8. [2/2] If it is possible to redo or undo the buttons will be enabled. If it is impossible, then the buttons will be disabled.

9. [2/2] Pressing "undo" undoes the last command, unless it was a selection, which is covered in the previous requirement. The user can undo multiple commands until no commands are left.

10. [2/2] The user can perform multiple redos after a series of undos to reapply the undone commands. A user can do this until all commands have been redone.

11. [2/2] Actions will be properly chunked upon performing an undo or redo. Undoing a translation undoes from the mouse-down to the mouse-up, and not the intermediate translations.

12. [2/2] The last transformed object has the focus after performing an undo. For example, you create a rectangle, translate it, create an ellipse, select the rectangle and scale it. If you first undo scaling the rectangle, the rectangle remains selected. Pressing undo again undoes creation of the ellipse and leaves the rectangle nothing selected. Pressing undoes a next time undoes the rectangle translation and leaves the rectangle selected again. Pressing final undo undoes the creating of the rectangle and leaves nothing on the screen. This example is seen in the video (47 seconds -> end).

## ENHANCEMENT [0/2]

no enhancement

## GENERAL [0/2] (can be negative)

-2 runs enough to mark functionality, but crashes sometimes, details:

At some point the following error occurs and after that no shape can be drawn.

Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
	at CanvasView$1.mouseDragged(CanvasView.java:74)
	at java.awt.Component.processMouseMotionEvent(Component.java:6573)
	at javax.swing.JComponent.processMouseMotionEvent(JComponent.java:3342)
	at java.awt.Component.processEvent(Component.java:6294)
	at java.awt.Container.processEvent(Container.java:2234)
	at java.awt.Component.dispatchEventImpl(Component.java:4881)
	at java.awt.Container.dispatchEventImpl(Container.java:2292)
	at java.awt.Component.dispatchEvent(Component.java:4703)
	at java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4898)

