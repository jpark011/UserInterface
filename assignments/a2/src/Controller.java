import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

/**
 * Created by JayP on 2018-02-27.
 */

class Controller {
    private Model model;

    ActionListener newFile;
    ActionListener exitFile;

    ActionListener widthCheckBox;

    ActionListener drawToggleButton;
    ActionListener selectToggleButton;

    ActionListener shapeDropDown;
    ActionListener widthDropDown;

    ActionListener fillColorChanger;
    ActionListener strokeColorChanger;

    ActionListener deleteButton;
    ActionListener transformButton;

    Controller(Model model) {
        this.model = model;

        newFile = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.reset();
            }
        };

        exitFile = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        widthCheckBox = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();

                model.changeWidth(new StrokeWidth(checkBox.getText()));
            }
        };

        drawToggleButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton btn = (AbstractButton) e.getSource();

                // Select
                if (btn.isSelected()) {
                    model.changeMode(Mode.DRAW);
                    // Unselect
                } else {
                    model.changeMode(Mode.SELECT);
                }
            }
        };

        selectToggleButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton btn = (AbstractButton) e.getSource();
                // Select
                if (btn.isSelected()) {
                    model.changeMode(Mode.SELECT);
                    // Unselect
                } else {
                    model.changeMode(Mode.DRAW);
                }
            }
        };

        shapeDropDown = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<Shape> comboBox = (JComboBox<Shape>) e.getSource();
                Shape selected = (Shape) comboBox.getSelectedItem();
                if (selected != null) {
                    model.changeShape(selected);
                }
            }
        };

        widthDropDown = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<StrokeWidth> comboBox = (JComboBox<StrokeWidth>) e.getSource();
                StrokeWidth selected = (StrokeWidth) comboBox.getSelectedItem();
                if (selected != null) {
                    model.changeWidth(selected);
                }
            }
        };

        fillColorChanger = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog((JComponent)e.getSource(), "Choose Fill Color", model.getFillColor());
                model.changeFillColor(color);
            }
        };

        strokeColorChanger = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog((JComponent)e.getSource(), "Choose Stroke Color", model.getStrokeColor());
                model.changeStrokeColor(color);
            }
        };


        deleteButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.deleteSelected();
            }
        };

        transformButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
    }
}
