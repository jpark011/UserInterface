import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class LineModel extends ShapeModel {

    Point a;
    Point b;
    public LineModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        this.a = startPoint;
        this.b = endPoint;

        Path2D path = new Path2D.Double();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        this.shape = path;
    }

    public LineModel(Point startPoint, Point endPoint, double rotation) {
        this(startPoint, endPoint);
        this.rotation = rotation;
    }

    @Override
    public boolean hitTest(Point2D p) {
        return pointToLineDistance(a,b,(Point) p) < 10;
    }

    public double pointToLineDistance(Point A, Point B, Point P) {
        double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
        return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
    }

    @Override
    public void changeShape(Point startPoint, Point endPoint) {
        super.changeShape(startPoint, endPoint);

        this.a = startPoint;
        this.b = endPoint;

        Path2D path = new Path2D.Double();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        this.shape = path;
    }

    // very special
    @Override
    public ShapeModel getRotateHandle() {
        double mid = (double)(this.endPoint.x - this.startPoint.x - SIZE) / 2;
        double top = Math.min(startPoint.y, endPoint.y);
        Point startPoint = new Point(
                this.startPoint.x + (int)mid,
                (int)top + ROTATE_HANDLE_OFFSET.y);
        Point endPoint = new Point(
                startPoint.x + SIZE,
                startPoint.y + SIZE);
        return new EllipseModel(startPoint, endPoint);
    }
}