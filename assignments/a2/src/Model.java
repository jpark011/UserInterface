
import java.awt.Color;
import java.util.*;

enum Mode {
    DRAW,
    SELECT
}

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;

    // FOR MENU
    private static final String[] menus = {"File", "Edit", "Format"};
    private static final String[][] menuItems = {
            {"New", "Exit"},
            {"Selection Mode", "Drawing Mode", "Delete", "Transform"},
            {"Stroke Width", "Fill Colour", "Stroke Colour"}
    };

    // FOR TOOLS
    private final static Shape[] shapes = {
            Shape.FREEFORM,
            Shape.STRAIGHT_LINE,
            Shape.RECTANGLE,
            Shape.ELLIPSE
    };

    // FOR MENU & TOOLS
    private static final int NUM_WIDTHS = 10;

    // STATE
    private Mode mode;
    private Shape shape;
    private int width;
    private Color fillColor;
    private Color strokeColor;

    // SHAPES
    private ArrayList<Drawable> drawables;

    private Drawable selected = null;
    /**
     * Create a new model.
     */
    Model() {
        this.observers = new ArrayList<Observer>();

        init();
    }

    private void init() {
        this.mode = Mode.DRAW;
        this.shape = Shape.FREEFORM;
        this.width = 1;
        this.fillColor = Color.WHITE;
        this.strokeColor = Color.BLACK;

        drawables = null;
        this.drawables = new ArrayList<>();
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
        notifyObservers();
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }

        System.out.println(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("mode: " + mode.name());
        sb.append("\nshape: " + shape);
        sb.append("\nwidth: " + width);
        sb.append("\nfillColor: " + fillColor);
        sb.append("\nstrokeColor: " + strokeColor);

        return sb.toString();
    }

    void reset() {
        init();
        notifyObservers();
    }

    public int getNumWidths() {
        return NUM_WIDTHS;
    }

    public  String[] getMenus() {
        return menus;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public  String[][] getMenuItems() {
        return menuItems;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public int getWidth() {
        return width;
    }

    public Mode getMode() {
        return mode;
    }

    public Shape getShape() {
        return shape;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    ArrayList<Drawable> getDrawables() {
        return drawables;
    }

    void addDrawable(Drawable d) {
        drawables.add(d);
    }

    void changeMode(Mode mode) {
        this.mode = mode;
        notifyObservers();
    }

    void changeShape(Shape shape) {
        this.shape = shape;
        notifyObservers();
    }

    void changeWidth(StrokeWidth width) {
        this.width = width.width;
        notifyObservers();
    }

    void changeFillColor(Color color) {
        this.fillColor = color;
        notifyObservers();
    }

    void changeStrokeColor(Color color) {
        this.strokeColor = color;
        notifyObservers();
    }

    boolean hasShapes() {
        return drawables.size() > 0;
    }
}
