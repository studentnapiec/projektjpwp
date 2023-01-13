import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Basket extends ImageRectangle {
    private boolean isDockedInSocket = false;
    private boolean isDisabled = false;

    private ColorOrder colorOrder;

    public ColorOrder getColor() {
        return colorOrder;
    }

    public void setColor(ColorOrder colorOrder) {
        this.colorOrder = colorOrder;
    }

    public boolean isDockedInSocket() {
        return isDockedInSocket;
    }

    public void setDockedInSocket(boolean dockedInSocket) {
        isDockedInSocket = dockedInSocket;
    }

    public Basket(ImageIcon image, Rectangle2D.Double rectangle, ColorOrder colorOrder) {
        super(image, rectangle);
        this.colorOrder = colorOrder;
    }
//    public Basket(ImageIcon image){
//        super(image, new Rectangle2D.Double(0,0,140,200));
//    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
