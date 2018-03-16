import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class DrawingModel extends Observable {

    private List<ShapeModel> shapes = new ArrayList<>();

    // UNDO
    UndoManager undoManager = new UndoManager();
    private ShapeModel saved = null;

    // need when selected
    private ShapeModel selected = null;
    private ShapeModel scaleBox = null;
    private ShapeModel rotateHandle = null;

    ShapeModel.ShapeType shapeType = ShapeModel.ShapeType.Rectangle;

    public ShapeModel.ShapeType getShape() {
        return shapeType;
    }

    public void setShape(ShapeModel.ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public DrawingModel() { }

    public List<ShapeModel> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    public void addShape(ShapeModel shape) {
        UndoableEdit edit = new AddShapeEdit(shape);
        undoManager.addEdit(edit);

        this.shapes.add(shape);
        this.selectShape(shape);
        this.setChanged();
        this.notifyObservers();
    }

    public void changeShape(Point startPoint, Point endPoint) {
        selected.changeShape(startPoint, endPoint);
        this.selectShape(selected);
        this.setChanged();
        this.notifyObservers();
    }

    public void changeShape(double rotation) {
        selected.changeShape(rotation);
        this.selectShape(selected);
        this.setChanged();
        this.notifyObservers();
    }

    public void saveShape() {
        this.saved =selected.clone();
    }

    public boolean isSelected(ShapeModel shape) {
        return shape == selected;
    }

    public boolean hasSelected() {
        return selected != null;
    }

    public ShapeModel getSelected() {
        return selected;
    }

    public void selectShape(ShapeModel shape) {
        selected = shape;
        scaleBox = shape.getScaleBox();
        rotateHandle = shape.getRotateHandle();
        this.setChanged();
        this.notifyObservers();
    }

    public void unselectShape() {
        selected = null;
        scaleBox = null;
        rotateHandle = null;
        this.setChanged();
        this.notifyObservers();
    }

    public ShapeModel getScaleBox() {
        return scaleBox;
    }

    public ShapeModel getRotateHandle() {
        return rotateHandle;
    }

    public void duplicateShape() {
        ShapeModel duplicate = selected.clone();
        addShape(duplicate);
        selectShape(duplicate);
    }

    public void undo() {
        if (canUndo()) {
            undoManager.undo();
        }
    }

    public void redo() {
        if (canRedo()) {
            undoManager.redo();
        }
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void makeTransUndoable() {
        UndoableEdit edit = new ChangeShapeEdit(
                selected,
                saved.startPoint, saved.endPoint,
                selected.startPoint, selected.endPoint);
        undoManager.addEdit(edit);
    }

    public void makeRotationUndoable() {
        UndoableEdit edit = new ChangeShapeEdit(
                selected,
                saved.rotation,
                selected.rotation);
        undoManager.addEdit(edit);
    }

    private class AddShapeEdit extends AbstractUndoableEdit {
        ShapeModel added;

        public AddShapeEdit(ShapeModel shape) {
            this.added = shape;
        }
        @Override
        public void undo() throws CannotUndoException {
            super.undo();

            // ops reversed
            unselectShape();
            shapes.remove(added);
            setChanged();
            notifyObservers();
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();

            // ops restored
            shapes.add(added);
            selectShape(added);

            setChanged();
            notifyObservers();
        }
    };

    private class ChangeShapeEdit extends AbstractUndoableEdit {
        ShapeModel target;
        // current values
        Point curStartPoint;
        Point curEndPoint;
        double curRotation;

        // previous values
        Point preStartPoint;
        Point preEndPoint;
        double preRotation;

        public ChangeShapeEdit(
                ShapeModel shape,
                Point preStartPoint, Point preEndPoint,
                Point curStartPoint, Point curEndPoint) {
            this.target = shape;
            this.preStartPoint = preStartPoint;
            this.preEndPoint = preEndPoint;
            this.curStartPoint = curStartPoint;
            this.curEndPoint = curEndPoint;
            this.preRotation = 0;
            this.curRotation = 0;
        }

        public ChangeShapeEdit(
                ShapeModel shape,
                double preRotation, double curRotation) {
            this.target = shape;
            this.preStartPoint = null;
            this.preEndPoint = null;
            this.curStartPoint = null;
            this.curEndPoint = null;
            this.preRotation = preRotation;
            this.curRotation = curRotation;
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();

            // ops reversed
            // when rotate edit
            if (curStartPoint == null || curEndPoint == null) {
                target.changeShape(preRotation);
            // when translate & scale edit
            } else {
                target.changeShape(preStartPoint, preEndPoint);
            }
            selectShape(target);

            setChanged();
            notifyObservers();
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();

            // ops reversed
            // when rotate edit
            if (curStartPoint == null || curEndPoint == null) {
                target.changeShape(curRotation);
            // when translate & scale edit
            } else {
                target.changeShape(curStartPoint, curEndPoint);
            }
            selectShape(target);


            setChanged();
            notifyObservers();
        }
    };
}
