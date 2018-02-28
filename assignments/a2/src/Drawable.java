import org.w3c.dom.css.Rect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by JayP on 2018-02-26.
 */

enum Shape {
    FREEFORM {
        @Override
        public String toString() {
            return "Freeform";
        }
    },
    STRAIGHT_LINE {
        @Override
        public String toString() {
            return "Straight Line";
        }
    },

    RECTANGLE{
        @Override
        public String toString() {
            return "Rectangle";
        }
    },
    ELLIPSE {
        @Override
        public String toString() {
            return "Ellipse";
        }
    }
}

// Stroke width DT
class StrokeWidth {
    int width = 1;

    StrokeWidth(int width) {
        this.width = width;
    }

    // in the form of "XXXpx"
    StrokeWidth(String width) {
        this.width = Integer.parseInt(width.substring(0, width.length()- 2));
    }

    @Override
    public String toString() {
        return width + "px";
    }
}

abstract class Drawable {
    protected Color fillColor;
    protected Color strokeColor;
    protected int width;
    protected final Color TRANSPARENT = new Color(0,0,0,0);

    Drawable(Color fillColor, Color strokeColor, int width) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.width = width;
    }

    abstract void draw(Graphics g);
    abstract boolean hitTest(Point p);
    abstract Rectangle getBoundary();

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }
}

class FreeForm extends Drawable {
    private ArrayList<Point> points;

    FreeForm(Color fillColor, Color strokeColor, int width,
             ArrayList<Point> points) {
        super(fillColor, strokeColor, width);
        this.points = points;
    }

    @Override
    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // outline
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(strokeColor);
        int size = points.size();
        for (int i = 1; i < size; i++) {
            Point p1 = points.get(i-1);
            Point p2 = points.get(i);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    boolean hitTest(Point p) {
        int[] x_arr = new int[points.size()];
        int[] y_arr = new int[points.size()];

        for (int i=0; i < points.size(); i++) {
            x_arr[i] = points.get(i).x;
            y_arr[i] = points.get(i).y;
        }

        return new Polygon(x_arr, y_arr, points.size()).contains(p);
    }

    @Override
    Rectangle getBoundary() {
        int n = points.size();
        ArrayList<Point> xPoints = (ArrayList<Point>) points.clone();
        ArrayList<Point> yPoints = (ArrayList<Point>) points.clone();

        xPoints.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.x - o2.x;
            }
        });

        yPoints.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.y - o2.y;
            }
        });

        int x = xPoints.get(0).x;
        int y = yPoints.get(0).y;
        int w = Math.abs(x - xPoints.get(n-1).x);
        int h = Math.abs(y - yPoints.get(n-1).y);

        return new Rectangle(TRANSPARENT, Color.CYAN, 3, x, y, x+w, y+h);
    }
}


class StraightLine extends Drawable {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    StraightLine(Color fillColor, Color strokeColor, int width,
              int x1, int y1, int x2, int y2) {
        super(fillColor, strokeColor, width);

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // outline
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(strokeColor);
        g2d.drawLine(x1, y1, x2, y2);
    }

    @Override
    boolean hitTest(Point p) {
        return Line2D.ptSegDist(x1, y1, x2, y2, p.x, p.y) < 5;
    }

    @Override
    Rectangle getBoundary() {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);
        int h = Math.abs(y1 - y2);

        return new Rectangle(TRANSPARENT, Color.CYAN, 3, x, y, x+w, y+h);
    }
}

class Rectangle extends Drawable {
    private int w;
    private int h;
    private int x;
    private int y;

    Rectangle(Color fillColor, Color strokeColor, int width,
              int x1, int y1, int x2, int y2) {
        super(fillColor, strokeColor, width);

        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);
        int h = Math.abs(y1 - y2);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // fill
        g2d.setColor(fillColor);
        g2d.fillRect(x, y, w, h);

        // outline
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(strokeColor);
        g2d.drawRect(x, y, w, h);
    }

    @Override
    boolean hitTest(Point p) {
        return (x - 5 < p.x) &&
                (y - 5 < p.y) &&
                (p.x < x + w + 5) &&
                (p.y < y + h + 5);
    }

    @Override
    Rectangle getBoundary() {
        return new Rectangle(TRANSPARENT, Color.CYAN, 3, x, y, x+w, y+h);
    }
}

class Ellipse extends Drawable {
    private int w;
    private int h;
    private int x;
    private int y;

    Ellipse(Color fillColor, Color strokeColor, int width,
              int x1, int y1, int x2, int y2) {
        super(fillColor, strokeColor, width);

        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);
        int h = Math.abs(y1 - y2);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // fill
        g2d.setColor(fillColor);
        g2d.fillOval(x, y, w, h);
        // outline
        g2d.setStroke(new BasicStroke(width));
        g2d.setColor(strokeColor);
        g2d.drawOval(x, y, w, h);
    }

    @Override
    boolean hitTest(Point p) {
        // using an ellipse equation...
        double x_m = x + 0.5 * w;
        double y_m = y + 0.5 * h;

        double res = Math.pow(x_m - p.x, 2) / Math.pow(0.5 * (w + 5), 2) +
                Math.pow(y_m - p.y, 2) / Math.pow(0.5 * (h + 5), 2);

        return res < 1;
    }

    @Override
    Rectangle getBoundary() {
        return new Rectangle(TRANSPARENT, Color.CYAN, 3, x, y, x+w, y+h);
    }
}


