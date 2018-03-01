import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

/**
 * Created by JayP on 2018-02-28.
 */

public class TransformDialog extends JDialog {

    SpinnerModel translateX = new SpinnerNumberModel();
    SpinnerModel translateY = new SpinnerNumberModel();
    SpinnerModel rotate = new SpinnerNumberModel();
    SpinnerModel scaleX = new SpinnerNumberModel();
    SpinnerModel scaleY = new SpinnerNumberModel();

    ActionListener okButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changed = true;
            dispose();
        }
    };
    ActionListener closeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    boolean changed;

    TransformDialog(AffineTransform transform) {
        super();
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setTitle("Transform Shape");
        this.setSize(350, 250);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
//        this.setLocation((int)dim.getWidth()/2, (int)dim.getHeight()/2);
        this.setModal(true);

        this.changed = false;
        translateX.setValue(transform.getTranslateX());
        translateY.setValue(transform.getTranslateY());
        rotate.setValue(Math.toDegrees(Math.atan2(transform.getShearY(), transform.getScaleY())));
        scaleX.setValue(transform.getScaleX());
        scaleY.setValue(transform.getScaleY());

        this.setContentPane(buildUI());

    }

    JPanel buildUI() {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

        JPanel innerPanel;
        JLabel label;
        JButton button;
        JSpinner spinner;
        Dimension spinnerSize = new Dimension(86, 24);

        // Translate Row
        innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Translate (px):");
        innerPanel.add(label);
        // x
        label = new JLabel("x:");
        innerPanel.add(label);
        spinner = new JSpinner(translateX);
        spinner.setPreferredSize(spinnerSize);
        innerPanel.add(spinner);
        // y
        label = new JLabel("y:");
        innerPanel.add(label);
        spinner = new JSpinner(translateY);
        spinner.setPreferredSize(spinnerSize);
        innerPanel.add(spinner);

        outerPanel.add(innerPanel);

        // Rotation Row
        innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Rotation (degrees):");
        innerPanel.add(label);

        spinner = new JSpinner(rotate);
        spinner.setPreferredSize(spinnerSize);
        innerPanel.add(spinner);

        outerPanel.add(innerPanel);

        // Scale Row
        innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("Scale (times):");
        innerPanel.add(label);
        // x
        label = new JLabel("x:");
        innerPanel.add(label);
        spinner = new JSpinner(scaleX);
        spinner.setPreferredSize(spinnerSize);
        innerPanel.add(spinner);
        // y
        label = new JLabel("y:");
        innerPanel.add(label);
        spinner = new JSpinner(scaleY);
        spinner.setPreferredSize(spinnerSize);
        innerPanel.add(spinner);

        outerPanel.add(innerPanel);

        // Button Row
        innerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        button = new JButton("OK");
        button.addActionListener(okButton);
        innerPanel.add(button);
        button = new JButton("Close");
        button.addActionListener(closeButton);
        innerPanel.add(button);

        outerPanel.add(innerPanel);

        return outerPanel;
    }

    AffineTransform getTransform() {
        AffineTransform ret = null;
        if (changed) {
            ret = new AffineTransform();
            double sX = ( (Number)scaleX.getValue() ).doubleValue();
            double sY = ( (Number)scaleY.getValue() ).doubleValue();
            double r = ( (Number)rotate.getValue() ).doubleValue();
            double tX = ( (Number)translateX.getValue() ).doubleValue();
            double tY = ( (Number)translateY.getValue() ).doubleValue();

            ret.scale(sX, sY);
            ret.rotate(Math.toRadians(r));
            ret.translate(tX, tY);
        }

        return ret;
    }
}
