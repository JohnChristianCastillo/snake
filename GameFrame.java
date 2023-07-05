import javax.swing.JFrame;

public class GameFrame extends JFrame {
    // create constructor
    GameFrame(){
        GamePanel panel = new GamePanel();

        this.add(panel);
        this.setTitle("Snake");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // this will close the program when we click the exit button
        this.setResizable(false); // this will prevent the frame from being resized
        this.pack(); // this method will resize the frame to fit the panel
        this.setVisible(true); // this will make the frame visible
        this.setLocationRelativeTo(null); // this will make the frame appear at the center of the screen
    }
}
