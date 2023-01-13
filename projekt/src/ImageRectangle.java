import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ImageRectangle {

    private ImageIcon imageIcon;

    private Rectangle2D.Double rectangle;

    public ImageRectangle(ImageIcon image, Rectangle2D.Double rectangle) {
        this.imageIcon = image;
        this.rectangle = rectangle;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
    public void setImageIcon(ImageIcon imageIcon){
        this.imageIcon = imageIcon;
    }
    public Rectangle2D.Double getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle2D.Double rectangle) {
        this.rectangle = rectangle;
    }
}