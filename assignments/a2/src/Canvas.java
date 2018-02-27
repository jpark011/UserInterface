import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Created by JayP on 2018-02-26.
 */
// Canvas also acts as a controller for itself!
public class Canvas extends JPanel implements Observer {
    private Model model;

    // Mouse pt
    private Point M = new Point();
    // Click pt
    private Point C = new Point();

    // Freeform path
    private ArrayList<Point> trace;

    private boolean pressed = false;

    Canvas(Model model) {
        this.model = model;
        buildUI();

        model.addObserver(this);
    }

    private void buildUI() {
        // click listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                pressed = true;

                C.x = e.getX();
                C.y = e.getY();

                M.x = C.x;
                M.y = C.y;

                trace = new ArrayList<>();

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Shape shape = model.getShape();
                Color fillColor = model.getFillColor();
                Color strokeColor = model.getStrokeColor();
                int width = model.getWidth();

                pressed = false;

                switch (shape) {
                    case FREEFORM:
                        model.addDrawable(new FreeForm(fillColor, strokeColor, width,
                                trace));
                        break;
                    case STRAIGHT_LINE:
                        model.addDrawable(new StraightLine(fillColor, strokeColor, width,
                                C.x, C.y, M.x, M.y));
                        break;
                    case RECTANGLE:
                        model.addDrawable(new Rectangle(fillColor, strokeColor, width,
                                C.x, C.y, M.x, M.y));
                        break;
                    case ELLIPSE:
                        model.addDrawable(new Ellipse(fillColor, strokeColor, width,
                                C.x, C.y, M.x, M.y));
                        break;
                }
            }
        });

        // move listener
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                M.x = e.getX();
                M.y = e.getY();

                trace.add(new Point(M.x, M.y));

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // this is where model kicks in
        ArrayList<Drawable> drawables = model.getDrawables();
        Shape shape = model.getShape();
        Color fillColor = model.getFillColor();
        Color strokeColor = model.getStrokeColor();
        int width = model.getWidth();

        for (Drawable d : drawables) {
            d.draw(g);
        }

        // draw currently drawn shape
        if (pressed) {
            switch (shape) {
                case FREEFORM:
                    new FreeForm(fillColor, strokeColor, width,
                            trace).draw(g);
                    break;
                case STRAIGHT_LINE:
                    new StraightLine(fillColor, strokeColor, width,
                            C.x, C.y, M.x, M.y).draw(g);
                    break;
                case RECTANGLE:
                   new Rectangle(fillColor, strokeColor, width,
                            C.x, C.y, M.x, M.y).draw(g);
                    break;
                case ELLIPSE:
                    new Ellipse(fillColor, strokeColor, width,
                            C.x, C.y, M.x, M.y).draw(g);
                    break;
            }
        }
    }

    @Override
    public void update(Object observable) {
        repaint();
    }
}
