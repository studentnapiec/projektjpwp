import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ImagePanel extends JPanel {



    public static final int basketSocketsY = 500;
    public static final int firstBacketSocketX = 20;
    public static final int basketSocketsXGap = 260;
    public static final int timerDuration = 1;

    public static boolean gameOverFlag = false;

    // -------------------------------
    final static int elipseWidth = 80;
    final static int elipseHeight = 80;

    final static int rectangleWidth= 140;
    final static int rectangleHeight= 200;


    ImageIcon blueBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\blue-basket.png")));
    ImageIcon greenBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\green-basket.png")));
    ImageIcon pinkBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\pink-basket.png")));
    ImageIcon redBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\red-basket.png")));
    ImageIcon emptyBasketSocketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\empty-basket-socket.png")));

//    ImageIcon emptyBasketSocketIcon = new ImageIcon(((new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\empty-basket-socket.png")))).getImage()).getScaledInstance(70, 100, java.awt.Image.SCALE_SMOOTH));

    Point previousPoint;
    Object previousPressedComponent = null;


//    public static final double rectangleDiagonal = Math.sqrt(rectangleHeight * rectangleHeight + rectangleWidth * rectangleWidth);
//    Rectangle2D.Double defaultBasketSocketRect = new Rectangle2D.Double(firstBacketSocketX, basketSocketsY, rectangleWidth, rectangleHeight);



    static int roundTime = 6;
    static int roundRemainingTime=roundTime;

    static int basketsDocked = 0;

    Random random = new Random();
    JLabel timeLabel;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    MouseDragListener mouseDragListener = new MouseDragListener();
    MouseClickListener mouseClickListener = new MouseClickListener();
    KeyListener keyListener = new KeyListener();


    Timer timer = new Timer((int)TimeUnit.SECONDS.toMillis(timerDuration), new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {

            if (roundRemainingTime <= 0){
                gameOverFlag = true;
                timer.stop();
                repaint();
                return;
            }
            roundRemainingTime -= timerDuration;
            timeLabel.setText("     Time: " + simpleDateFormat.format(new Date(TimeUnit.SECONDS.toMillis(roundRemainingTime))));
            repaint();
        }
        });


    List<Basket> basketList = List.of(
            new Basket(blueBasketIcon),
            new Basket(greenBasketIcon),
            new Basket(pinkBasketIcon),
            new Basket(redBasketIcon)
    );

    List<BasketSocket> basketSocketList = List.of(
            new BasketSocket(emptyBasketSocketIcon, new Rectangle2D.Double(firstBacketSocketX, basketSocketsY, rectangleWidth, rectangleHeight)),
            new BasketSocket(emptyBasketSocketIcon, new Rectangle2D.Double(firstBacketSocketX + basketSocketsXGap, basketSocketsY, rectangleWidth, rectangleHeight)),
            new BasketSocket(emptyBasketSocketIcon, new Rectangle2D.Double(firstBacketSocketX + 2 * basketSocketsXGap, basketSocketsY, rectangleWidth, rectangleHeight)),
            new BasketSocket(emptyBasketSocketIcon, new Rectangle2D.Double(firstBacketSocketX + 3* basketSocketsXGap, basketSocketsY, rectangleWidth, rectangleHeight))
    );
    List<ColorElipse> colorElipseList = List.of(
            new ColorElipse(new Ellipse2D.Double(100, 300, elipseWidth, elipseHeight), new Color(153,217,234)),
            new ColorElipse(new Ellipse2D.Double(400, 600, elipseWidth, elipseHeight), new Color(181,230,29)),

            new ColorElipse(new Ellipse2D.Double(400, 600, elipseWidth, elipseHeight), new Color(255,174,201)),
            new ColorElipse(new Ellipse2D.Double(400, 600, elipseWidth, elipseHeight), new Color(237,28,36))
    );



    public static final String[] choices = {"MENU","Rozpocznij od nowa", "Zakoncz"};
    JPanel panel;
    JLabel level;
    JLabel user;


    ImagePanel() {
        panel = new JPanel();
        this.addMouseListener(mouseClickListener);
        this.addMouseMotionListener(mouseDragListener);
        this.addKeyListener(keyListener);

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setMaximumSize(cb.getPreferredSize()); // added code
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);// added code
        panel.add(cb);


        JButton jMenu = new JButton("MENU");

        timeLabel = new JLabel();

//        timeLabel.setText("     Time: " + "00:00");
        timeLabel.setVisible(true);

        level = new JLabel();

        level.setVisible(true);

        user = new JLabel();

        user.setVisible(true);

        panel.setBounds(100, 250, 500, 250);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel.add(jMenu);
        panel.add(level);
        panel.add(timeLabel);
        panel.add(user);

        this.add(panel, BorderLayout.NORTH);
        this.setVisible(true);
        initGame();

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

    public void initGame(){
        timeLabel.setText("     Time: " + simpleDateFormat.format(new Date(TimeUnit.SECONDS.toMillis(roundTime))));
        level.setText("         Level: 1");
        user.setText("          user: Student");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);

        basketsDocked = 0;
        for (BasketSocket basketSocket: basketSocketList) {
            basketSocket.getImageIcon().paintIcon(this, g, (int) basketSocket.getRectangle().getX(), (int) basketSocket.getRectangle().getY());
            if(basketSocket.getBasket() != null && basketSocket.getBasket().isDockedInSocket()){
                basketsDocked+=1;
            }
        }

        for (Basket basket: basketList) {
            if(!basket.isDisabled()){
                basket.getImageIcon().paintIcon(this, g, (int) basket.getRectangle().getX(), (int) basket.getRectangle().getY());
            }
        }



        for (ColorElipse colorElipse: colorElipseList) {
            if(!colorElipse.isDisabled() && basketsDocked == basketList.size()) {
                g2.setColor(colorElipse.getColor());
                Ellipse2D.Double elipse2D = colorElipse.getElipse();
                g2.fill(elipse2D);
//              g.drawOval((int)elipse2D.getX(), (int)elipse2D.getY(), colorElipse.getWidth(), colorElipse.getHeight());
//              g.fillOval((int)elipse2D.getX(), (int)elipse2D.getY(), colorElipse.getWidth(), colorElipse.getHeight());
            }
        }

        if(gameOverFlag){
            gameOver(g);
        }
//        if (basketsDocked == basketList.size()){
//            gameOver(g);
//        }
//        timer.setInitialDelay(0);
        timer.start();

    }

    private void gameOver(Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Game Over", 300, 300);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Space to RESTART", 350, 340);
        this.requestFocus();
        repaint();
        gameOverFlag = true;

        this.removeMouseListener(mouseClickListener);
        this.removeMouseMotionListener(mouseDragListener);
    }

    public int getRandomNextInt(int minInclusive, int maxInclusive) {
        return random.nextInt((maxInclusive - minInclusive) + 1) + minInclusive;
    }
    

    private void restartGame(){
        for (Basket basket:basketList) {

            for (BasketSocket basketSocket:basketSocketList) {
                basketSocket.setImageIcon(emptyBasketSocketIcon);
                basketSocket.setBasket(null);
            }

            int randXFromScreenRange = getRandomNextInt(50, 800);
            int randYFromScreenRange = getRandomNextInt(80, 250);
            basket.setDockedInSocket(false);
            basket.setDisabled(false);
            basket.setRectangle(new Rectangle2D.Double(randXFromScreenRange, randYFromScreenRange, rectangleWidth, rectangleHeight));
        }
        this.addMouseListener(mouseClickListener);
        this.addMouseMotionListener(mouseDragListener);

        roundRemainingTime = roundTime - 2;
        roundTime = roundRemainingTime;
        initGame();
        gameOverFlag = false;
        repaint();
    }

    private class MouseClickListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {


//                Object source = e.getSource();
//                if (source instanceof Basket){
//                    previousPoint = e.getPoint();
//                    previousPressedComponent = (Basket) source;
//                }

            for (Basket basket: basketList) {
                if ((e.getButton() == MouseEvent.BUTTON1) && basket.getRectangle().contains(e.getX(), e.getY())) {
                    previousPoint = e.getPoint();
                    previousPressedComponent = basket;
                }
            }
                for (ColorElipse colorElipse: colorElipseList) {
                    if ((e.getButton() == MouseEvent.BUTTON1) && colorElipse.getElipse().contains(e.getX(), e.getY())) {
                        previousPoint = e.getPoint();
                        previousPressedComponent = colorElipse;
                    }
                }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            if(previousPressedComponent instanceof ColorElipse){
                ColorElipse colorElipse = (ColorElipse) previousPressedComponent;
                Ellipse2D.Double elipse2D = colorElipse.getElipse();
                for (BasketSocket basketSocket: basketSocketList) {

//                    Do sprawdzenia czy elipsa znajduje sie w prostokacie
//                    basketSocket.getRectangle().contains(elipse2D.getBounds2D());

                    if(elipse2D.intersects(basketSocket.getRectangle())){
                        System.out.println("kolo przecina socket");
                    }
                }
            }

            if(previousPressedComponent instanceof Basket){
                Basket basket = (Basket) previousPressedComponent;
                Rectangle2D.Double rectangle = basket.getRectangle();
                for (BasketSocket basketSocket: basketSocketList) {
                    if(rectangle.intersects(basketSocket.getRectangle()) && basketSocket.getBasket() == null && !basket.isDockedInSocket()){
                        basketSocket.setBasket(basket);
                        basketSocket.setImageIcon(basket.getImageIcon());
                        basket.setDockedInSocket(true);
                        basket.setDisabled(true);
                        System.out.println("kosz przecina socket");
                    }
//                    if(rectangle.intersects(basketSocket.getRectangle()) && basketSocket.getBasket() == null && !basket.isDockedInSocket){
//                        basketSocket.setBasket(basket);
//                        basketSocket.setImageIcon(basket.getImageIcon());
//                        basket.setDockedInSocket(true);
//                        System.out.println("kosz przecina socket");
//                    }
                }
                repaint();
            }

            previousPressedComponent = null;
        }
    }
    private class MouseDragListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            if(previousPressedComponent == null)
                return;


            Point currentPoint = e.getPoint();

            if(previousPressedComponent instanceof Basket){
                Basket imageRectangle = (Basket) previousPressedComponent;
                Rectangle2D.Double rectangle2D = imageRectangle.getRectangle();
                rectangle2D.setRect(currentPoint.getX() - (rectangle2D.getWidth()/2.0), currentPoint.getY() - (rectangle2D.getHeight()/2.0), rectangleWidth, rectangleHeight);
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

    private class KeyListener extends KeyAdapter{
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE && gameOverFlag == true){
                restartGame();
            }
            super.keyReleased(e);
        }
    }
}
