
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class MainView extends JFrame implements Observer {

    private Model model;

    /**
     * Create a new View.
     */
    public MainView(Model model, Controller controller) {
        // Set up the window.
        this.setTitle("CS 349 W18 A2");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Relative location set
        this.setLocationRelativeTo(null);

        // menu bar (Another view)
        MenuBar menuBar = new MenuBar(model, controller);
        this.setJMenuBar(menuBar);

        // tool bar (Another view)
        ToolBar toolBar = new ToolBar(model, controller);
        this.add(toolBar, BorderLayout.NORTH);

        // canvas (it is controller itself)
        Canvas canvas = new Canvas(model);
        this.add(canvas, BorderLayout.CENTER);


        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
    }

}
