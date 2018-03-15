import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Created by JayP on 2018-03-15.
 */

public class RotateHandleModel extends EllipseModel {
        ShapeModel parent;

    public RotateHandleModel(Point startPoint, Point endPoint, ShapeModel shape) {
        super(startPoint, endPoint);
        this.parent = shape;
        this.rotation = shape.rotation;
    }

    @Override
    public boolean hitTest(Point2D p) {
    // first inverse the mouse location
        AffineTransform inverse = null;
        try {
            AffineTransform orig = new AffineTransform();
            // Move to the center of parent shape first
            orig.translate(0, parent.getCenter().y - this.getCenter().y);
            orig.translate(this.getCenter().x, this.getCenter().y);
            orig.rotate(rotation);
            orig.translate(-this.getCenter().x, -this.getCenter().y);
            orig.translate(0, -(parent.getCenter().y - this.getCenter().y));
            inverse = orig.createInverse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Point2D transP = new Point();
        inverse.transform(p, transP);

        return this.getShape().contains(transP);
    }
}
