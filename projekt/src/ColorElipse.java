import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ColorElipse {

    private boolean isDisabled = false;
    private Ellipse2D.Double elipse;
    private Color rgbColorToPaint;

    private ColorOrder color;

    public ColorOrder getColor() {
        return color;
    }

    public void setColor(ColorOrder color) {
        this.color = color;
    }


    public ColorElipse(Ellipse2D.Double elipse, Color rgbColorToPaint, ColorOrder color) {
        this.elipse = elipse;
        this.rgbColorToPaint = rgbColorToPaint;
        this.color = color;
    }

    public Ellipse2D.Double getElipse() {
        return elipse;
    }

    public void setElipse(Ellipse2D.Double elipse) {
        this.elipse = elipse;
    }

    public Color getRgbColorToPaint() {
        return rgbColorToPaint;
    }

    public void setRgbColorToPaint(Color rgbColorToPaint) {
        this.rgbColorToPaint = rgbColorToPaint;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
