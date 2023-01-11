import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

public class ImagePanel extends JPanel {

    ImageIcon blueBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\blue-basket.png")));
    ImageIcon greenBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\green-basket.png")));
    ImageIcon pinkBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\pink-basket.png")));
    ImageIcon redBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\red-basket.png")));

    List<Basket> basketList = List.of(
            new Basket(blueBasketIcon),
            new Basket(greenBasketIcon),
            new Basket(pinkBasketIcon),
            new Basket(redBasketIcon)
    );
    List<ColorElipse> colorElipseList = List.of(
            new ColorElipse(new Ellipse2D.Double(100, 300, elipseWidth, elipseHeight), new Color(135,206,250)),
            new ColorElipse(new Ellipse2D.Double(400, 600, elipseWidth, elipseHeight), new Color(235,206,100))
    );

    Point previousPoint;

    final static int elipseWidth = 80;
    final static int elipseHeight = 80;

    final static int rectangleWidth= 140;
    final static int rectangleHeight= 200;

    Object previousPressedComponent = null;


    ImagePanel() {
        JPanel panel = new JPanel();
        this.addMouseListener(new PressListener());
        this.addMouseMotionListener(new DragListener());

        JButton jButton = new JButton("MENU");


        JLabel timeLabel = new JLabel();
        timeLabel.setText("     Time: " + "00:00");
        timeLabel.setVisible(true);

        JLabel level = new JLabel();
        level.setText("         Level: 1");
        level.setVisible(true);

        JLabel user = new JLabel();
        user.setText("          user: X");
        user.setVisible(true);

        panel.setBounds(100, 250, 500, 250);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel.add(jButton);
        panel.add(level);
        panel.add(timeLabel);
        panel.add(user);

        this.add(panel, BorderLayout.NORTH);
        this.setVisible(true);

//        for (Basket basket: basketList) {
//            basket.addMouseListener(new PressListener());
//            basket.addMouseMotionListener(new DragListener());
//        }
//        PressListener pressListener = new PressListener();
//        DragListener dragListener = new DragListener();
//        for (ColorElipse colorElipse: colorElipseList) {
//            colorElipse.addMouseListener(new PressListener());
//            colorElipse.addMouseMotionListener(new DragListener());
//        }
        // ustawianie polozenia kolorow

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g);
        for (ColorElipse colorElipse: colorElipseList) {
            g2.setColor(colorElipse.getColor());
            Ellipse2D.Double elipse2D = colorElipse.getElipse();
            g2.fill(elipse2D);
//            g.drawOval((int)elipse2D.getX(), (int)elipse2D.getY(), colorElipse.getWidth(), colorElipse.getHeight());
//            g.fillOval((int)elipse2D.getX(), (int)elipse2D.getY(), colorElipse.getWidth(), colorElipse.getHeight());
        }

    }

    private class PressListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {


                for (ColorElipse colorElipse: colorElipseList) {
                    if ((e.getButton() == 1) && colorElipse.getElipse().contains(e.getX(), e.getY())) {
                        previousPoint = e.getPoint();
                        previousPressedComponent = colorElipse;
                    }
                }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            previousPressedComponent = null;
        }
    }
    private class DragListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            if(previousPressedComponent == null)
                return;

            Point currentPoint = e.getPoint();

            if(previousPressedComponent instanceof Basket || previousPressedComponent instanceof BasketSocket){
                ImageRectangle imageRectangle = (Basket) previousPressedComponent;
                Rectangle2D.Double rectangle2D = imageRectangle.getRectangle();
                rectangle2D.setRect(currentPoint.getX(), currentPoint.getY(), rectangleWidth, rectangleHeight);
                imageRectangle.setRectangle(rectangle2D);

            }
            else if(previousPressedComponent instanceof ColorElipse){
                ColorElipse colorElipse = (ColorElipse) previousPressedComponent;
                Ellipse2D.Double elipse2D = colorElipse.getElipse();
                elipse2D.setFrame(currentPoint.getX() - ((double)elipseWidth/2.0), currentPoint.getY() - ((double)elipseHeight/2.0), elipseWidth, elipseHeight);
                colorElipse.setElipse(elipse2D);
            }

            previousPoint = currentPoint;
            repaint();
        }
    }
}
