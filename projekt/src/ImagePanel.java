import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class ImagePanel extends JPanel {

    ImageIcon blue = new ImageIcon("C:\\Users\\Miloszka\\IdeaProjects\\projektjpwp\\projekt\\src\\images\\blue.png");
    ImageIcon red = new ImageIcon("C:\\Users\\Miloszka\\IdeaProjects\\projektjpwp\\projekt\\src\\images\\red.png");
    ImageIcon orange = new ImageIcon("C:\\Users\\Miloszka\\IdeaProjects\\projektjpwp\\projekt\\src\\images\\orange.png");
    ImageIcon black = new ImageIcon("C:\\Users\\Miloszka\\IdeaProjects\\projektjpwp\\projekt\\src\\images\\black.png");
    final int BLUE_WITH = blue.getIconWidth();
    final int BLUE_HEIGTH = blue.getIconHeight();
    final int RED_WITH = red.getIconWidth();
    final int RED_HEIGTH = red.getIconHeight();
    final int ORANGE_WITH = orange.getIconWidth();
    final int ORANGE_HEIGTH = orange.getIconHeight();
    final int BLACK_WITH = black.getIconWidth();
    final int BLACK_HEIGTH = black.getIconHeight();

    Point image_corner_blue;
    Point image_corner_red;
    Point image_corner_orange;
    Point image_corner_black;
    Point point1;
    Point point2;
    Point point3;
    Point point4;

    int second, minute;
    String ddSecond, ddMinute;
    DecimalFormat dFormat = new DecimalFormat("00");
    JLabel timeLabel;
    Timer timer;

    ImagePanel() {

        image_corner_blue = new Point(0, 100);
        image_corner_red = new Point(300, 100);
        image_corner_orange = new Point(600, 100);
        image_corner_black = new Point(900, 100);
        ClickListen clickListen = new ClickListen();
        this.addMouseListener(clickListen);

        DragListen dragListen = new DragListen();
        this.addMouseMotionListener(dragListen);


        JPanel panel = new JPanel();

        JButton jButton = new JButton("MENU");

        JLabel timeLabel = new JLabel();
        timeLabel.setText("     Time: " + "00:00");
        timeLabel.setVisible(true);

        JLabel level = new JLabel();
        level.setText("         Level: 1");
        level.setVisible(true);

        JLabel user = new JLabel();
        user.setText("          User: X");
        user.setVisible(true);

        panel.setBounds(100, 250, 500, 250);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel.add(jButton);
        panel.add(level);
        panel.add(timeLabel);
        panel.add(user);

        this.add(panel, BorderLayout.NORTH);
        this.setVisible(true);

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        blue.paintIcon(this, g, (int) image_corner_blue.getX(), (int) image_corner_blue.getY());
        red.paintIcon(this, g, (int) image_corner_red.getX(), (int) image_corner_red.getY());
        orange.paintIcon(this, g, (int) image_corner_orange.getX(), (int) image_corner_orange.getY());
        black.paintIcon(this, g, (int) image_corner_black.getX(), (int) image_corner_black.getY());
    }

    private class ClickListen extends MouseAdapter {

        public void mousePressed(MouseEvent event) {
            point1 = event.getPoint();
            point2 = event.getPoint();
            point3 = event.getPoint();
            point4 = event.getPoint();

        }

    }

    private class DragListen extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent event) {
            normalTimer();
            Point currentPoint = event.getPoint();
            image_corner_blue.translate(
                    (int) (currentPoint.getX() - point1.getX()),
                    (int) (currentPoint.getY() - point1.getY())
            );

            image_corner_red.translate(
                    (int) (currentPoint.getX() - point2.getX()),
                    (int) (currentPoint.getY() - point2.getY())
            );

            image_corner_orange.translate(
                    (int) (currentPoint.getX() - point3.getX()),
                    (int) (currentPoint.getY() - point3.getY())
            );

            image_corner_black.translate(
                    (int) (currentPoint.getX() - point4.getX()),
                    (int) (currentPoint.getY() - point4.getY())
            );

            point1 = currentPoint;
            repaint();
        }

    }

    public void normalTimer() {
        second = 0;
        minute = 0;
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                second++;
                ddSecond = dFormat.format(second);
                ddMinute = dFormat.format(minute);
                timeLabel.setText(ddMinute + ":" + ddSecond);

                if (second == 60) {
                    second = 0;
                    minute++;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timeLabel.setText(ddMinute + ":" + ddSecond);
                }
            }
        });
    }
}
