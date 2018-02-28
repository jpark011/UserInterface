import java.awt.CheckboxMenuItem;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Created by JayP on 2018-02-26.
 */

public class MenuBar extends JMenuBar implements Observer {
    private Model model;
    private String[] menus;
    private String[][] menuItems;
    private int numWidths;

    private JCheckBoxMenuItem selectCheckBox;
    private JCheckBoxMenuItem drawCheckBox;

    private JMenuItem deleteButton;
    private JMenuItem transformButton;

    private ColorIcon fillColorIcon;
    private ColorIcon strokeColorIcon;
    private JCheckBoxMenuItem[] widthMenuItem;


    MenuBar(Model model, Controller controller) {
        this.model = model;

        menus = model.getMenus();
        menuItems = model.getMenuItems();
        numWidths = model.getNumWidths();

        buildUI(controller);

        model.addObserver(this);
    }

    private void buildUI(Controller controller) {
        JMenu menu, subMenu;
        JMenuItem menuItem;

        // File Menu
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        menuItem = new JMenuItem("New");
        menuItem.addActionListener(controller.newFile);
        menu.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        menu.add(menuItem);

        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(controller.exitFile);
        menu.setMnemonic(KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        menu.add(menuItem);

        this.add(menu);

        // Edit Menu
        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);

        selectCheckBox = new JCheckBoxMenuItem("Selection Mode");
        selectCheckBox.addActionListener(controller.selectToggleButton);
        selectCheckBox.setMnemonic(KeyEvent.VK_S);
        selectCheckBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menu.add(selectCheckBox);

        drawCheckBox = new JCheckBoxMenuItem("Drawing Mode");
        drawCheckBox.addActionListener(controller.drawToggleButton);
        drawCheckBox.setMnemonic(KeyEvent.VK_R);
        drawCheckBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        menu.add(drawCheckBox);

        deleteButton = new JMenuItem("Delete");
        deleteButton.addActionListener(controller.deleteButton);
        deleteButton.setMnemonic(KeyEvent.VK_D);
        deleteButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_DOWN_MASK));
        menu.add(deleteButton);

        transformButton = new JMenuItem("Transform");
        transformButton.addActionListener(controller.transformButton);
        transformButton.setMnemonic(KeyEvent.VK_T);
        transformButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
        menu.add(transformButton);

        this.add(menu);

        // Format Menu
        menu = new JMenu("Format");
        menu.setMnemonic(KeyEvent.VK_F);

        subMenu = new JMenu("Stroke Width");
        subMenu.setMnemonic(KeyEvent.VK_S);
        int numWidths = model.getNumWidths();
        widthMenuItem = new JCheckBoxMenuItem[numWidths];
        // create width menuitems
        for (int k=1; k <= numWidths; k++) {
            widthMenuItem[k-1] = new JCheckBoxMenuItem(new StrokeWidth(k).toString());
            widthMenuItem[k-1].addActionListener(controller.widthCheckBox);
            subMenu.add(widthMenuItem[k-1]);
        }
        menu.add(subMenu);

        fillColorIcon = new ColorIcon(model.getFillColor(), 10, 10);
        menuItem = new JMenuItem("Fill Colour", fillColorIcon);
        menuItem.addActionListener(controller.fillColorChanger);
        menuItem.setMnemonic(KeyEvent.VK_F);
        menu.add(menuItem);

        strokeColorIcon = new ColorIcon(model.getStrokeColor(), 10, 10);
        menuItem = new JMenuItem("Stroke Colour", strokeColorIcon);
        menuItem.addActionListener(controller.strokeColorChanger);
        menuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(menuItem);

        this.add(menu);

    }

    @Override
    public void update(Object observable) {
        // Select & Draw mode
        selectCheckBox.setState(model.getMode() == Mode.SELECT);
        drawCheckBox.setState(model.getMode() == Mode.DRAW);

        // Stroke width checkboxes
        for (int i = 0; i < widthMenuItem.length; i++) {
            widthMenuItem[i].setState(model.getWidth() == i+1);
        }

        // update colors
        fillColorIcon.setColor(model.getFillColor());
        strokeColorIcon.setColor(model.getStrokeColor());

        // Delete & Transform
        if (model.hasShapes()) {
            deleteButton.setEnabled(true);
            transformButton.setEnabled(true);
        } else {
            deleteButton.setEnabled(false);
            transformButton.setEnabled(false);
        }

    }
}
