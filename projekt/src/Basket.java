import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Basket extends ImageRectangle {
    public Basket(ImageIcon image, Rectangle2D.Double rectangle) {
        super(image, rectangle);
    }
    public Basket(ImageIcon image){
        super(image, new Rectangle2D.Double(0,0,140,200));
    }
}
