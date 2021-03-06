import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
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
        this.setBackground(Color.WHITE);

        // click listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                switch(model.getMode()) {
                case DRAW:
                    pressed = true;

                    C.x = e.getX();
                    C.y = e.getY();

                    M.x = C.x;
                    M.y = C.y;

                    trace = new ArrayList<>();

                    break;
                case SELECT:
                    ArrayList<Drawable> drawables = model.getDrawables();
                    Point p = new Point(e.getX(), e.getY());

                    // iterate reverse since i want to select the most recent one!
                    int i = drawables.size() - 1;
                    while (0 <= i) {
                        Drawable target = drawables.get(i);
                        AffineTransform inverse = null;
                        try {
                             inverse = target.getTransform().createInverse();
                        } catch (Exception ex) {
                            System.out.println("Inverse failed!!!!!!!!!!");
                            ex.printStackTrace();
                        }
                        Point transP = new Point();
                        inverse.transform(p, transP);

                        if (target.hitTest(transP)) {
                            // deselect
                            if (target.equals(model.getSelected())) {
                                model.selectShape(-1);
                            // select
                            } else {
                                model.selectShape(i);
                            }
                            break;
                        }
                        i--;
                    }

                    if (i < 0) {
                        model.selectShape(-1);
                    }

                    break;
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                switch (model.getMode()) {
                case DRAW:
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
                    break;
                case SELECT:
                    break;
                }
            }
        });

        // move listener
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                switch (model.getMode()) {
                    case DRAW:
                        M.x = e.getX();
                        M.y = e.getY();

                        trace.add(new Point(M.x, M.y));
                        break;
                    case SELECT:
                        break;
                }

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

        // Draw select outline
        Drawable selected = model.getSelected();

        if (selected != null) {
            selected.getBoundary().draw(g);
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
