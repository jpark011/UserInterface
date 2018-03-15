import java.util.*;
import java.util.List;

import javax.swing.undo.UndoManager;

public class DrawingModel extends Observable {

    private List<ShapeModel> shapes = new ArrayList<>();

    // UNDO
    UndoManager undoManager = new UndoManager();

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
        this.shapes.add(shape);
        this.setChanged();
        this.notifyObservers();
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
}
