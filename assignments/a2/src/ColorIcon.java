import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * Created by JayP on 2018-02-27.
 */

public class ColorIcon implements Icon {
    private int width ;
    private int height;
    Color color = Color.WHITE;

    ColorIcon(Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
