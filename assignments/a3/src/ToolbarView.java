import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class ToolbarView extends JToolBar implements Observer {
    private JButton undo = new JButton("Undo");
    private JButton redo = new JButton("Redo");
    private JButton duplicate = new JButton("Duplicate");

    private DrawingModel model;

    ToolbarView(DrawingModel model) {
        super();
        this.model = model;
        model.addObserver(this);

        setFloatable(false);
        add(undo);
        add(redo);
        add(duplicate);

        ActionListener drawingActionListener = e -> model.setShape(ShapeModel.ShapeType.valueOf(((JButton) e.getSource()).getText()));

        for(ShapeModel.ShapeType mode : ShapeModel.ShapeType.values()) {
            JButton button = new JButton(mode.toString());
            button.addActionListener(drawingActionListener);
            add(button);
        }
        undo.addActionListener(new UndoListener());
        redo.addActionListener(new RedoListener());
        duplicate.addActionListener(new DuplicateListener());

        // just for shortcuts (NOT added to UI)
        JMenuItem undoShortCut = new JMenuItem();
        JMenuItem redoShortCut = new JMenuItem();

        undoShortCut.setMnemonic(KeyEvent.VK_Z);
        undoShortCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        undoShortCut.addActionListener(new UndoListener());

        redoShortCut.setMnemonic(KeyEvent.VK_Y);
        redoShortCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        redoShortCut.addActionListener(new RedoListener());

        this.update(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        duplicate.setEnabled(model.hasSelected());
        undo.setEnabled(model.canUndo());
        redo.setEnabled(model.canRedo());
    }

    private class DuplicateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.duplicateShape();

        }
    }

    private class UndoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.undo();
        }
    }

    private class RedoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.redo();
        }
    }
}
