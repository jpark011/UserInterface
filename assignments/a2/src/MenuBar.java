import java.awt.CheckboxMenuItem;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Created by JayP on 2018-02-26.
 */

public class MenuBar extends JMenuBar implements Observer {
    private Model model;
    private String[] menus;
    private String[][] menuItems;
    private int numWidths;


    MenuBar(Model model, Controller controller) {
        this.model = model;

        menus = model.getMenus();
        menuItems = model.getMenuItems();
        numWidths = model.getNumWidths();

        buildUI(controller);

        model.addObserver(this);
    }

    private void buildUI(Controller controller) {
        // Build menuBar
        for (int i=0; i < menus.length; i++) {
            JMenu menu = new JMenu(menus[i]);
            for (int j=0; j < menuItems[i].length; j++) {
                JMenuItem menuItem;
                // Stroke Width has children
                if (menuItems[i][j].equals("Stroke Width")) {
                    menuItem = new JMenu(menuItems[i][j]);
                    for (int k=1; k <= model.getNumWidths(); k++) {
                        JCheckBoxMenuItem subMenuItem = new JCheckBoxMenuItem(k+"px");
                        menuItem.add(subMenuItem);
                    }
                    // regular menuItems
                } else {
                    menuItem = new JMenuItem(menuItems[i][j]);

                    switch (menuItems[i][j]) {
                        case "New":
                            menuItem.addActionListener(controller.newFile);
                            break;
                        case "Exit":
                            menuItem.addActionListener(controller.exitFile);
                            break;
                        case "Selection Mode":
                            menuItem.addActionListener(controller.selectMenuItem);
                            break;
                        case "Drawing Mode":
                            menuItem.addActionListener(controller.drawMenuItem);
                            break;
                        case "Delete":
                            break;
                        case "Transform":
                            break;
                        case "Fill Colour":
                            break;
                        case "Stroke Colour":
                            break;
                    }
                }
                menu.add(menuItem);
            }
            this.add(menu);
        }
    }

    @Override
    public void update(Object observable) {

    }
}
