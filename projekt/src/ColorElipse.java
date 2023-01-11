import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ColorElipse extends JLabel {
    Ellipse2D.Double elipse;
    Color color;

    public ColorElipse(Ellipse2D.Double elipse, Color color) {
        this.elipse = elipse;
        this.setBounds(elipse.getBounds());

        this.color = color;
    }

    public Ellipse2D.Double getElipse() {
        return elipse;
    }

    public void setElipse(Ellipse2D.Double elipse) {
        this.elipse = elipse;
        this.setBounds(elipse.getBounds());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
