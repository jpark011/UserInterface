import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * Created by JayP on 2018-02-26.
 */

public class ToolBar extends JToolBar implements Observer {
    private Model model;
    private Shape[] shapes;
    private int numWidths;

    private JToggleButton selectButton;
    private JToggleButton drawButton;

    private JComboBox<Shape> shapeDropDown;
    private JComboBox<StrokeWidth> widthDropDown;

    private JButton fillColorButton;
    private JButton strokeColorButton;

    private ColorIcon fillColorIcon;
    private ColorIcon strokeColorIcon;


    ToolBar(Model model, Controller controller) {
        this.model = model;

        shapes = model.getShapes();
        numWidths = model.getNumWidths();

        buildUI(controller);

        model.addObserver(this);
    }

    private void buildUI(Controller controller) {
        // Select Mode button
        selectButton = new JToggleButton("Select");
        selectButton.addActionListener(controller.selectToggleButton);
        this.add(selectButton);

        // Draw Mode button
        drawButton = new JToggleButton("Draw");
        drawButton.addActionListener(controller.drawToggleButton);
        this.add(drawButton);

        // Share dropdown
        shapeDropDown = new JComboBox<Shape>(shapes);
        shapeDropDown.addActionListener(controller.shapeDropDown);
        this.add(shapeDropDown);

        // Stroke Width dropdown
        StrokeWidth[] widths = new StrokeWidth[numWidths];
        for (int i = 1; i <= numWidths; i++) {
            widths[i-1] = new StrokeWidth(i);
        }
        widthDropDown = new JComboBox<>(widths);
        widthDropDown.addActionListener(controller.widthDropDown);
        this.add(widthDropDown);

        // Fill color button
        fillColorIcon = new ColorIcon(model.getFillColor(), 20, 20);
        fillColorButton = new JButton("Fill Color", fillColorIcon);
        fillColorButton.addActionListener(controller.fillColorChanger);
        this.add(fillColorButton);

        // Stroke color button
        strokeColorIcon = new ColorIcon(model.getStrokeColor(), 20, 20);
        strokeColorButton = new JButton("Stroke Color", strokeColorIcon);
        strokeColorButton.addActionListener(controller.strokeColorChanger);
        this.add(strokeColorButton);
    }

    @Override
    public void update(Object observable) {

        // update toggle buttons
        Mode mode = model.getMode();
        selectButton.setSelected(mode == Mode.SELECT);
        drawButton.setSelected(mode == Mode.DRAW);

        // update drop downs
        shapeDropDown.setSelectedItem(model.getShape());
        widthDropDown.setSelectedIndex(model.getWidth() -1);

        // update colors
        fillColorIcon.setColor(model.getFillColor());
        strokeColorIcon.setColor(model.getStrokeColor());
    }
}
