import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ColorElipse {

    private boolean isDisabled = false;
    private Ellipse2D.Double elipse;
    private Color color;


    public ColorElipse(Ellipse2D.Double elipse, Color color) {
        this.elipse = elipse;
        this.color = color;
    }

    public Ellipse2D.Double getElipse() {
        return elipse;
    }

    public void setElipse(Ellipse2D.Double elipse) {
        this.elipse = elipse;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
