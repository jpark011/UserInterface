import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

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
    int width;

    StrokeWidth(int width) {
        this.width = width;
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

    Drawable(Color fillColor, Color strokeColor, int width) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.width = width;
    }

    abstract void draw(Graphics g);
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
}


