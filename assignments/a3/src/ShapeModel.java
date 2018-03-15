import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;

public class ShapeModel implements Serializable {
    Shape shape;

    Point startPoint;
    Point endPoint;
    double rotation = 0; // in radian

    final Point SCALE_BOX_OFFSET = new Point(-5, -5);
    final Point ROTATE_HANDLE_OFFSET = new Point(0, -20);
    final int SIZE = 10;
    final int CLONE_OFFSET = 10;

    public ShapeModel(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public ShapeModel clone() {
        ShapeModel ret = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
            ret = (ShapeModel) new ObjectInputStream(bis).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (ret != null) {
            Point newStart = new Point((int)this.startPoint.getX() + CLONE_OFFSET, (int)this.startPoint.getY() + CLONE_OFFSET);
            Point newEnd = new Point((int)this.endPoint.getX() + CLONE_OFFSET, (int)this.endPoint.getY() + CLONE_OFFSET);
            ret.changeShape(newStart, newEnd);
        }

        return ret;
    }

    public Point getCenter() {
        return new Point(
                (startPoint.x + endPoint.x)/2,
                (startPoint.y + endPoint.y)/2
        );
    }

    public Shape getShape() {
        return shape;
    }

    public void changeShape(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void changeShape(double rotation) {
        this.rotation = rotation;
    }

    // You will need to change the hittest to account for transformations.
    public boolean hitTest(Point2D p) {
        // first inverse the mouse location
        AffineTransform inverse = null;
        try {
            AffineTransform orig = new AffineTransform();
            orig.rotate(rotation);
            inverse = orig.createInverse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Point2D transP = new Point();
        inverse.transform(p, transP);

        return this.getShape().contains(transP);
    }

    public ShapeModel getScaleBox() {
        Point startPoint = new Point(
                this.endPoint.x + SCALE_BOX_OFFSET.x,
                this.endPoint.y + SCALE_BOX_OFFSET.y);
        Point endPoint = new Point(
                startPoint.x + SIZE,
                startPoint.y + SIZE);
        return new RectangleModel(startPoint, endPoint);
    }

    public ShapeModel getRotateHandle() {
        double mid = (double)(this.endPoint.x - this.startPoint.x - SIZE) / 2;
        Point startPoint = new Point(
                this.startPoint.x + (int)mid,
                this.startPoint.y + ROTATE_HANDLE_OFFSET.y);
        Point endPoint = new Point(
                startPoint.x + SIZE,
                startPoint.y + SIZE);
        return new EllipseModel(startPoint, endPoint);
    }

    /**
     * Given a ShapeType and the start and end point of the shape, ShapeFactory constructs a new ShapeModel
     * using the class reference in the ShapeType enum and returns it.
     */
    public static class ShapeFactory {
        // For scaleBox & rotateHandle location
        public ShapeModel getShape(ShapeType shapeType, Point startPoint, Point endPoint) {
            try {
                Class<? extends ShapeModel> clazz = shapeType.shape;
                Constructor<? extends ShapeModel> constructor = clazz.getConstructor(Point.class, Point.class);

                return constructor.newInstance(startPoint, endPoint);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public ShapeModel getShape(ShapeModel shape, Point startPoint, Point endPoint) {
            try {
                Class<? extends ShapeModel> clazz = shape.getClass();
                Constructor<? extends ShapeModel> constructor = clazz.getConstructor(Point.class, Point.class);
                System.out.println(clazz);

                return constructor.newInstance(startPoint, endPoint);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public enum ShapeType {
        Ellipse(EllipseModel.class),
        Rectangle(RectangleModel.class),
        Line(LineModel.class);

        public final Class<? extends ShapeModel> shape;
        ShapeType(Class<? extends ShapeModel> shape) {
            this.shape = shape;
        }
    }
}
