import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BasketSocket extends ImageRectangle {
    private Basket basket = null;
    // pole pozwalajace ustalic czy wybrano odpowiednia kolejnosc kolorow, gdyz rysowany jest on zawsze w kolejnosci swoich wewnetrznych wspolrzednych

    private ColorOrder colorOrder;

    public ColorOrder getColor() {
        return colorOrder;
    }

    public void setColor(ColorOrder colorOrder) {
        this.colorOrder = colorOrder;
    }

        public BasketSocket(ImageIcon image, Rectangle2D.Double rectangle, ColorOrder colorOrder) {
        super(image, rectangle);
        this.colorOrder = colorOrder;

    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
