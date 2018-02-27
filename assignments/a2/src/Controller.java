import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JComboBox;
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

    ActionListener drawToggleButton;
    ActionListener selectToggleButton;

    ActionListener drawMenuItem;
    ActionListener selectMenuItem;

    ActionListener shapeDropDown;
    ActionListener widthDropDown;

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

        drawToggleButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton btn = (JToggleButton) e.getSource();

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
                JToggleButton btn = (JToggleButton) e.getSource();

                // Select
                if (btn.isSelected()) {
                    model.changeMode(Mode.SELECT);
                    // Unselect
                } else {
                    model.changeMode(Mode.DRAW);
                }
            }
        };

        drawMenuItem = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.changeMode(Mode.DRAW);
            }
        };

        selectMenuItem = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.changeMode(Mode.SELECT);
            }
        };

        shapeDropDown = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<Shape> comboBox = (JComboBox<Shape>) e.getSource();
                Shape selected = (Shape) comboBox.getSelectedItem();
                model.changeShape(selected);
            }
        };

        widthDropDown = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<StrokeWidth> comboBox = (JComboBox<StrokeWidth>) e.getSource();
                StrokeWidth selected = (StrokeWidth) comboBox.getSelectedItem();
                model.changeWidth(selected);
            }
        };

    }
}
