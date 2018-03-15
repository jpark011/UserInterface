import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

// Mode(State) for selection
enum SelectMode {
    NONE,
    SHAPE,
    BOX,
    HANDLE
}

public class CanvasView extends JPanel implements Observer {
    DrawingModel model;
    Point2D lastMouse;
    Point2D startMouse;
    Point2D lastDragMouse;

    SelectMode selectMode = SelectMode.NONE;
    boolean isDragged = false;

    public CanvasView(DrawingModel model) {
        super();
        this.model = model;

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastMouse = e.getPoint();
                startMouse = e.getPoint();

                if (!model.hasSelected()) {
                    return;
                }

                // when shape is selected
                // decide mode
                if (model.getRotateHandle().hitTest(startMouse)) {
                    selectMode = SelectMode.HANDLE;
                } else if (model.getScaleBox().hitTest(startMouse)) {
                    selectMode = SelectMode.BOX;
                } else if (model.getSelected().hitTest(startMouse)) {
                    selectMode = SelectMode.SHAPE;
                } else {
                    selectMode = SelectMode.NONE;
                    model.unselectShape();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                isDragged = true;
                lastDragMouse = lastMouse;
                lastMouse = e.getPoint();

                ShapeModel orig = model.getSelected();
                switch(selectMode) {
                    case NONE:
                        break;
                    case SHAPE:
                        int offsetX = (int)(lastMouse.getX() - lastDragMouse.getX());
                        int offsetY = (int)(lastMouse.getY() - lastDragMouse.getY());
                        Point newStart = new Point(orig.startPoint.x + offsetX, orig.startPoint.y + offsetY);
                        Point newEnd = new Point(orig.endPoint.x + offsetX, orig.endPoint.y + offsetY);
                        orig.changeShape(newStart, newEnd);
                        model.selectShape(orig);
                        break;
                    case BOX:
                        orig.changeShape(orig.startPoint, (Point)lastMouse);
                        model.selectShape(orig);
                        break;
                    case HANDLE:
                        Point center = orig.getCenter();
                        // return angle between 2 points in radian
                        double angle = Math.atan2(lastMouse.getY() - center.getY(), lastMouse.getX() - center.getX()) + Math.PI/2;
                        orig.changeShape(angle);
                        model.selectShape(orig);
                        break;
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                // diff behave when dragged
                if (isDragged) {
                    switch(selectMode) {
                        case NONE:
                            ShapeModel shape = new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse);
                            model.addShape(shape);
                            model.selectShape(shape);
                            break;
                        case SHAPE:
                            break;
                        case BOX:
                            break;
                        case HANDLE:
                            break;
                    }

                } else {
                    for (ShapeModel shape : model.getShapes()) {
                        if (shape.hitTest(lastMouse)) {
                            model.selectShape(shape);
                        }
                    }
                }

                isDragged = false;
                startMouse = null;
                lastMouse = null;
            }
        };

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);

        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        setBackground(Color.WHITE);

        drawAllShapes(g2);
        if (!model.hasSelected()) {
            drawCurrentShape(g2);
        }
    }

    // trans & rotate & trans-back
    private void rotate(Graphics2D g2, ShapeModel shape) {
        g2.translate(shape.getCenter().x, shape.getCenter().y);
        g2.rotate(shape.rotation);
        g2.translate(-shape.getCenter().x, -shape.getCenter().y);
    }

    private void drawAllShapes(Graphics2D g2) {
        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        AffineTransform save = g2.getTransform();

        for(ShapeModel shape : model.getShapes()) {
            // rotate
            rotate(g2, shape);
            g2.draw(shape.getShape());
            if (model.isSelected(shape)) {
                Color savedColor = g2.getColor();
                // color for manipulators
                g2.setColor(Color.BLUE);
                g2.fill(model.getScaleBox().getShape());
                g2.fill(model.getRotateHandle().getShape());
                g2.setColor(savedColor);
            }
            g2.setTransform(save);
        }
    }

    private void drawCurrentShape(Graphics2D g2) {
        if (startMouse == null) {
            return;
        }

        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.draw(new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse).getShape());
    }
}
