import javax.swing.*;

public class MyFrame extends JFrame {
    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024, 768);
        this.setResizable(false);
        this.setTitle("KOLORY");
        this.setLocationRelativeTo(null);

        ImagePanel imagePanel = new ImagePanel();

        this.add(imagePanel);
        this.setVisible(true);
    }
}
