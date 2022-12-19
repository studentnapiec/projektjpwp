import javax.swing.*;

public class MyFrame extends JFrame {
    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        this.setTitle("DEMO");
        this.setLocationRelativeTo(null);

        ImagePanel imagePanel = new ImagePanel();

        this.add(imagePanel);
        this.setVisible(true);
    }
}
