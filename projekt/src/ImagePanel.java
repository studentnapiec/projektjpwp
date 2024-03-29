import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImagePanel extends JPanel {

    private static final int basketSocketsY = 500;
    private static final int firstBacketSocketX = 20;
    private static final int basketSocketsXGap = 260;
    private static final int timerDuration = 1;
    private  static final int lossOfTime = 5;

    private static boolean gameOverFlag = false;

    private final static int elipseWidth = 80;
    private final static int elipseHeight = 80;

    private final static int rectangleWidth= 140;
    private final static int rectangleHeight= 200;

    private static int gameScore = 0;
    private final static int scoreAddPortion = 2;


    // domyslna kolejnosc kolorow to blue, green , pink, red tak jak ponizej
    private final ImageIcon blueBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\blue-basket.png")));
    private final ImageIcon greenBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\green-basket.png")));
    private final ImageIcon pinkBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\pink-basket.png")));
    private final ImageIcon redBasketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\red-basket.png")));
    private final ImageIcon emptyBasketSocketIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images\\baskets\\empty-basket-socket.png")));


    private URI blueSound;
    private URI greenSound;
    private URI pinkSound;
    private URI redSound;


    private Point previousPoint;
    private Object previousPressedComponent = null;


    private final static int startRoundTime = 60;
    private static int roundTime = startRoundTime;
    private static int roundRemainingTime=roundTime;

    private static int basketsDocked = 0;
    private static int colorElipsesDocked = 0;
    private static boolean isWin = false;


    private final Random random = new Random();
    private final JLabel timeLabel;
    private final JLabel scoreLabel;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

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

            if (levelChanged || isWin){
                timer.stop();
                repaint();
                return;
            }
            roundRemainingTime -= timerDuration;
            timeLabel.setText("     Time: " + simpleDateFormat.format(new Date(TimeUnit.SECONDS.toMillis(roundRemainingTime))));
            repaint();
        }
        });

    Timer timerSound = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isAfterSoundsRepaintedFrame){
                return;
            }
            else if (getIsSoundsPlayed()){
                repaint();
                isAfterSoundsRepaintedFrame = true;
                return;
            }
        }
    });

    static boolean isSoundsPlayed = false;

    static final String[] choices = {"MENU","Rozpocznij od nowa", "Zakoncz"};
    JPanel panel;
    JLabel levelLabel;
    JLabel user;
    JButton jMenu;
    int level = 1;
    SoundPlayer soundPlayer = new SoundPlayer();

    List<URI> soundUriList;

    boolean isAfterSoundsRepaintedFrame = false;
    List<Basket> basketList;
    List<BasketSocket> basketSocketList;
    List<ColorElipse> colorElipseList;

    //lista indexow wykorzystywanych do uporzadkowania obiektow w listach
    List<Integer> colorsOrderIndexes = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    private static boolean levelChanged = false;
    private final static int maxlevel = 3;

    void initAndShuffleGraphicSoundElements(){
        final int defaultArraySize = 4;

        // przetasowanie kolejnosci kolorow
        Collections.shuffle(colorsOrderIndexes);

        Basket[] basketArray = new Basket[defaultArraySize];
        BasketSocket[] basketSocketArray = new BasketSocket[defaultArraySize];
        colorElipseList = new ArrayList<>();
        URI[] soundsArray = new URI[defaultArraySize];


        basketArray[0] = new Basket(blueBasketIcon, new Rectangle2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), rectangleWidth, rectangleHeight), ColorOrder.BLUE);
        basketArray[1] = new Basket(greenBasketIcon, new Rectangle2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), rectangleWidth, rectangleHeight), ColorOrder.GREEN);
        basketArray[2] = new Basket(pinkBasketIcon, new Rectangle2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), rectangleWidth, rectangleHeight),  ColorOrder.PINK);
        basketArray[3] = new Basket(redBasketIcon, new Rectangle2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), rectangleWidth, rectangleHeight), ColorOrder.RED);

        for (int i = 0; i < level; i++) {
            colorElipseList.add(new ColorElipse(new Ellipse2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), elipseWidth, elipseHeight), new Color(153,217,234), ColorOrder.BLUE));
            colorElipseList.add(new ColorElipse(new Ellipse2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), elipseWidth, elipseHeight), new Color(181,230,29), ColorOrder.GREEN));
            colorElipseList.add(new ColorElipse(new Ellipse2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), elipseWidth, elipseHeight), new Color(255,174,201), ColorOrder.PINK));
            colorElipseList.add(new ColorElipse(new Ellipse2D.Double(getRandomNextInt(50, 800), getRandomNextInt(80, 250), elipseWidth, elipseHeight), new Color(237,28,36), ColorOrder.RED));
        }

        try {
            blueSound = Objects.requireNonNull(getClass().getResource("sounds\\wav\\blue.wav")).toURI();
            greenSound = Objects.requireNonNull(getClass().getResource("sounds\\wav\\green.wav")).toURI();
            pinkSound = Objects.requireNonNull(getClass().getResource("sounds\\wav\\pink.wav")).toURI();
            redSound = Objects.requireNonNull(getClass().getResource("sounds\\wav\\red.wav")).toURI();

        }catch (java.net.URISyntaxException e){
            throw new RuntimeException("Niepoprawny adres uri pliku dzwiekowego");
        }

        Map<ColorOrder, URI> colorSoundMap = new HashMap<>();
        colorSoundMap.put(ColorOrder.BLUE, blueSound);
        colorSoundMap.put(ColorOrder.GREEN, greenSound);
        colorSoundMap.put(ColorOrder.PINK, pinkSound);
        colorSoundMap.put(ColorOrder.RED, redSound);

        for (int i = 0; i < defaultArraySize; i++) {
            basketSocketArray[i] = new BasketSocket(emptyBasketSocketIcon, new Rectangle2D.Double(firstBacketSocketX + ( i * basketSocketsXGap), basketSocketsY, rectangleWidth, rectangleHeight), ColorOrder.values()[colorsOrderIndexes.get(i)]);
            soundsArray[i] = colorSoundMap.get(ColorOrder.values()[colorsOrderIndexes.get(i)]);
        }


        basketList = Arrays.asList(basketArray);
        basketSocketList = Arrays.asList(basketSocketArray);

        soundUriList = Arrays.asList(soundsArray);
        soundPlayer.setSoundList(soundUriList);

    }


    private void win(Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("WIN!", 425, 300);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Select option from menu", 350, 340);
        repaint();

    }
    private void levelUp(Graphics g){
        level++;
        if(level>maxlevel){
            isWin = true;
            return;
        }
        levelLabel.setText("         Level: " + level);
        levelChanged = true;
        nextLevel(g);
        timer.stop();
    }

    ImagePanel() {

        initAndShuffleGraphicSoundElements();


        panel = new JPanel();
        this.addMouseListener(mouseClickListener);
        this.addMouseMotionListener(mouseDragListener);
        this.addKeyListener(keyListener);


        final JPopupMenu popupmenu = new JPopupMenu("Game Menu");
        JMenuItem restartGameMenuItem = new JMenuItem("Restart Game");
        JMenuItem endGameMenuItem = new JMenuItem("End Game");

        restartGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundPlayer.terminate();
                restartGame();
            }
        });

        endGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        popupmenu.add(restartGameMenuItem);
        popupmenu.add(endGameMenuItem);
        panel.add(popupmenu);


        jMenu = new JButton("MENU");


        jMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int popupX = jMenu.getX();
                int popupY = jMenu.getY() + jMenu.getHeight();
                popupmenu.show(jMenu, popupX, popupY);
                repaint();
            }
        });

        timeLabel = new JLabel();

        timeLabel.setVisible(true);

        scoreLabel = new JLabel();
        scoreLabel.setVisible(true);
        levelLabel = new JLabel();

        levelLabel.setVisible(true);

        user = new JLabel();

        user.setVisible(true);

        panel.setBounds(100, 250, 500, 250);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel.add(jMenu);
        panel.add(levelLabel);
        panel.add(timeLabel);
        panel.add(scoreLabel);
        panel.add(user);

        this.add(panel, BorderLayout.NORTH);
        this.setVisible(true);
        initGame();

        timerSound.start();

        if(!getIsSoundsPlayed()){
            soundPlayer.playSoundList();
        }

    }

    public void initGame(){
        timeLabel.setText("     Time: " + simpleDateFormat.format(new Date(TimeUnit.SECONDS.toMillis(roundTime))));
        levelLabel.setText("         Level: 1");
        scoreLabel.setText("     Score: " + "0");
        user.setText("          User: Student");
        timerSound.start();
    }

    public void initSound(){
        if(!getIsSoundsPlayed()){
            soundPlayer.playSoundList();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);

        basketsDocked = 0;
        colorElipsesDocked = 0;

            for (BasketSocket basketSocket : basketSocketList) {
                if (basketSocket.getBasket() != null && basketSocket.getBasket().isDockedInSocket()) {
                    basketsDocked += 1;
                }
                basketSocket.getImageIcon().paintIcon(this, g, (int) basketSocket.getRectangle().getX(), (int) basketSocket.getRectangle().getY());

            }


        for (ColorElipse colorElipse: colorElipseList) {
            if (colorElipse.isDockedInSocket()) {
                colorElipsesDocked += 1;
            }

            if(!colorElipse.isDisabled() && basketsDocked == basketList.size()) {
                g2.setColor(colorElipse.getRgbColorToPaint());
                Ellipse2D.Double elipse2D = colorElipse.getElipse();
                g2.fill(elipse2D);
            }
        }


        if (getIsSoundsPlayed()) {
            for (Basket basket : basketList) {
                if (!basket.isDisabled()) {
                    basket.getImageIcon().paintIcon(this, g, (int) basket.getRectangle().getX(), (int) basket.getRectangle().getY());
                }
            }
            timer.start();
        }


        if(colorElipsesDocked == colorElipseList.size() && !levelChanged){
            levelUp(g);
        }

        if(colorElipsesDocked == colorElipseList.size() && !isWin) {
            nextLevel(g);
        }

        if(gameOverFlag){
            gameOver(g);
        }
        if(isWin){
            win(g);
        }

    }

    private void gameOver(Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Game Over", 300, 300);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Enter to RESTART", 350, 340);
        this.requestFocus();
        repaint();
        timer.stop();

        this.removeMouseListener(mouseClickListener);
        this.removeMouseMotionListener(mouseDragListener);
    }
    private void nextLevel(Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Game Time: " + simpleDateFormat.format(new Date(TimeUnit.SECONDS.toMillis((roundTime - roundRemainingTime)))), 300, 300);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Space to NEXT LEVEL", 425, 340);
        this.requestFocus();
        repaint();
    }

    public int getRandomNextInt(int minInclusive, int maxInclusive) {
        return random.nextInt((maxInclusive - minInclusive) + 1) + minInclusive;
    }
    

    private void restartGame(){
        level = 1;
        initAndShuffleGraphicSoundElements();

        this.addMouseListener(mouseClickListener);
        this.addMouseMotionListener(mouseDragListener);

        for (BasketSocket basketSocket:basketSocketList) {
            basketSocket.setImageIcon(emptyBasketSocketIcon);
            basketSocket.setBasket(null);
        }

        for (Basket basket:basketList) {

            int randXFromScreenRange = getRandomNextInt(50, 800);
            int randYFromScreenRange = getRandomNextInt(80, 250);
            basket.setDockedInSocket(false);
            basket.setDisabled(false);
            basket.setRectangle(new Rectangle2D.Double(randXFromScreenRange, randYFromScreenRange, rectangleWidth, rectangleHeight));
        }

        for (ColorElipse colorElipse:colorElipseList) {
            int randXFromScreenRange = getRandomNextInt(50, 800);
            int randYFromScreenRange = getRandomNextInt(80, 250);
            colorElipse.setDockedInSocket(false);
            colorElipse.setDisabled(false);
            colorElipse.setElipse(new Ellipse2D.Double(randXFromScreenRange, randYFromScreenRange, elipseWidth, elipseHeight));
        }

        gameScore = 0;
        roundRemainingTime = startRoundTime;
        roundTime = startRoundTime;

        isAfterSoundsRepaintedFrame = false;
        levelChanged = false;
        isWin = false;
        gameOverFlag = false;
        setIsSoundsPlayed(false);
        initGame();
        initSound();
        timer.stop();
        timerSound.start();
        repaint();
    }

    private void resetGame(){

        initAndShuffleGraphicSoundElements();

        this.addMouseListener(mouseClickListener);
        this.addMouseMotionListener(mouseDragListener);

        for (BasketSocket basketSocket:basketSocketList) {
            basketSocket.setImageIcon(emptyBasketSocketIcon);
            basketSocket.setBasket(null);
        }

        for (Basket basket:basketList) {

            int randXFromScreenRange = getRandomNextInt(50, 800);
            int randYFromScreenRange = getRandomNextInt(80, 250);
            basket.setDockedInSocket(false);
            basket.setDisabled(false);
            basket.setRectangle(new Rectangle2D.Double(randXFromScreenRange, randYFromScreenRange, rectangleWidth, rectangleHeight));
        }

        for (ColorElipse colorElipse:colorElipseList) {
            int randXFromScreenRange = getRandomNextInt(50, 800);
            int randYFromScreenRange = getRandomNextInt(80, 250);
            colorElipse.setDockedInSocket(false);
            colorElipse.setDisabled(false);
            colorElipse.setElipse(new Ellipse2D.Double(randXFromScreenRange, randYFromScreenRange, elipseWidth, elipseHeight));
        }

        roundRemainingTime = roundTime - lossOfTime;
        roundTime = roundRemainingTime;
        isAfterSoundsRepaintedFrame = false;

        timeLabel.setText("     Time: " + simpleDateFormat.format(new Date(TimeUnit.SECONDS.toMillis(roundRemainingTime))));

        setIsSoundsPlayed(false);
        initSound();
        timer.stop();
        repaint();
    }

    private class MouseClickListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {

            if(basketsDocked == basketList.size()) {
                for (ColorElipse colorElipse : colorElipseList) {
                    if ((e.getButton() == MouseEvent.BUTTON1) && colorElipse.getElipse().contains(e.getX(), e.getY())) {
                        previousPoint = e.getPoint();
                        previousPressedComponent = colorElipse;
                    }
                }
            }

            if(basketsDocked != basketList.size()) {
                for (Basket basket : basketList) {
                    if ((e.getButton() == MouseEvent.BUTTON1) && basket.getRectangle().contains(e.getX(), e.getY())) {
                        previousPoint = e.getPoint();
                        previousPressedComponent = basket;
                    }
                }
            }

        }

        private void addScoreRepaint(){
            gameScore+=scoreAddPortion;
            scoreLabel.setText("     Score: " + gameScore);
            repaint();
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            if(previousPressedComponent instanceof ColorElipse){
                ColorElipse colorElipse = (ColorElipse) previousPressedComponent;
                if(colorElipse.isDisabled())
                {
                    return;
                }
                Ellipse2D.Double elipse2D = colorElipse.getElipse();
                for (BasketSocket basketSocket: basketSocketList) {

                    if(elipse2D.intersects(basketSocket.getRectangle())){

                        if(colorElipse.getColor().equals(basketSocket.getBasket().getColor())){
                            colorElipse.setDockedInSocket(true);
                            addScoreRepaint();
                            colorElipse.setDisabled(true);
                        }else {
                            gameOverFlag=true;
                            timer.stop();
                            repaint();
                            return;
                        }

                    }
                }
            }

            else if(previousPressedComponent instanceof Basket){
                boolean intersectsRightSocketColor = false;
                Basket basket = (Basket) previousPressedComponent;
                if(basket.isDisabled())
                {
                    return;
                }
                Rectangle2D.Double rectangle = basket.getRectangle();
                for (BasketSocket basketSocket: basketSocketList) {
                    if(rectangle.intersects(basketSocket.getRectangle()) && basketSocket.getBasket() == null && !basket.isDockedInSocket()){
                        if(basket.getColor().equals(basketSocket.getColor())){
                            basketSocket.setBasket(basket);
                            basketSocket.setImageIcon(basket.getImageIcon());
                            basket.setDockedInSocket(true);
                            basket.setDisabled(true);
                            addScoreRepaint();
                        }
                        else{
                            gameOverFlag = true;
                            timer.stop();
                            repaint();
                            return;
                        }
                    }

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
                Basket basket = (Basket) previousPressedComponent;
                if(basket.isDisabled())
                {
                    return;
                }
                Rectangle2D.Double rectangle2D = basket.getRectangle();
                rectangle2D.setRect(currentPoint.getX() - (rectangle2D.getWidth()/2.0), currentPoint.getY() - (rectangle2D.getHeight()/2.0), rectangleWidth, rectangleHeight);
                basket.setRectangle(rectangle2D);
            }

            else if(previousPressedComponent instanceof ColorElipse){
                ColorElipse colorElipse = (ColorElipse) previousPressedComponent;
                if(colorElipse.isDisabled())
                {
                    return;
                }
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
            if(e.getKeyCode() == KeyEvent.VK_ENTER && gameOverFlag){
                restartGame();
            }

            if(e.getKeyCode() == KeyEvent.VK_SPACE && levelChanged){
                resetGame();
                levelChanged = false;
            }

            super.keyReleased(e);
        }
    }

    public synchronized static void setIsSoundsPlayed(boolean isSoundsPlayed) {
        ImagePanel.isSoundsPlayed = isSoundsPlayed;
    }
    public static boolean getIsSoundsPlayed(){
        return ImagePanel.isSoundsPlayed;
    }



}
