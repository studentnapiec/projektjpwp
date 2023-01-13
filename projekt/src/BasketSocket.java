import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BasketSocket extends ImageRectangle {
    private Basket basket = null;

    public BasketSocket(ImageIcon image, Rectangle2D.Double rectangle, Basket basket) {
        super(image, rectangle);
        this.basket = basket;
    }
        public BasketSocket(ImageIcon image, Rectangle2D.Double rectangle) {
        super(image, rectangle);
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
