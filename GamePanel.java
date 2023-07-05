

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

// currently the game does not move because we have not added the key listener yet to add this we need to 
public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600; // width of the screen the final keyword means that the variable cannot be changed
    static final int SCREEN_HEIGHT = 600; // height of the screen
    static final int UNIT_SIZE = 25; // size of the objects in the game
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE; // number of objects that can be placed in the game
    static final int DELAY = 75; // delay of the game, the lower the faster the game

    final int x[] = new int[GAME_UNITS]; // array that will store the x coordinates of the snake
    final int y[] = new int[GAME_UNITS]; // array that will store the y coordinates of the snake

    int bodyParts = 6; // initial number of body parts of the snake
    
    int applesEaten; // number of apples eaten by the snake
    int appleX; // x coordinate of the apple
    int appleY; // y coordinate of the apple

    char direction = 'R'; // initial direction of the snake

    Timer timer; // timer object
    Random random; // random object

    boolean running = false; // boolean variable that will determine if the game is running or not

    GamePanel(){
        random = new Random(); // initialize the random object
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // set the preferred size of the panel
        this.setBackground(Color.black); // set the background color of the panel
        this.setFocusable(true); // set the focusable to true
        this.addKeyListener(new MyKeyAdapter()); // add the key listener to the panel

        startGame(); // start the game
    }

    public void startGame(){
        running = true;
        newApple(); 
        timer = new Timer(DELAY, this); // initialize the timer object, the first parameter is the delay and the second parameter is the action listener
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            // to provide a clearer visualization of the grid, we will draw lines
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                // the first two parameters are the coordinates of the starting point 
                // the last two parameters are the coordinates of the ending point
                g.drawLine(i * UNIT_SIZE, 0,              i * UNIT_SIZE, SCREEN_HEIGHT); // draw vertical lines, 
                g.drawLine(0         , i * UNIT_SIZE,     SCREEN_WIDTH , i * UNIT_SIZE); // draw horizontal lines
            }
            g.setColor(Color.red); // set the color of the apple
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // draw the apple

            // draw the snake
            // as an optimization we can draw the head of the snake first
            g.setColor(new Color(45, 180, 0)); // set the color of the head of the snake
            g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE); // draw the head of the snake
            // then we can draw the body of the snake
            for(int i = 1; i < bodyParts; i++){
                g.setColor(new Color(45, 180, 0)); // set the color of the body of the snake
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // draw the body of the snake
            }

            // Score text
            g.setColor(Color.red); // set the color of the text
            g.setFont(new Font("Ink Free", Font.BOLD, 40)); // set the font of the text
            FontMetrics metrics2 = getFontMetrics(g.getFont()); 
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()); // draw the text
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; // generate a random x coordinate for the apple within the screen by using UNIT_SIZE as the range
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE; // generate a random y coordinate for the apple
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            // By shifting the previous coordinates of the snake to the current coordinates of the snake, we can move the snake
            x[i] = x[i-1]; // shift the x coordinates of the snake
            y[i] = y[i-1]; // shift the y coordinates of the snake
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // move the snake up
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; // move the snake down
                break;
            case 'L':   
                x[0] = x[0] - UNIT_SIZE; // move the snake left
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; // move the snake right
                break;
        }
    }

    public void checkApple(){
        if((x[0]) == appleX && (y[0] == appleY)){ // if the head of the snake collides with the apple
            bodyParts++; // increase the number of body parts of the snake
            applesEaten++; // increase the number of apples eaten by the snake
            newApple(); // generate a new apple
        }
    }
    
    public void checkCollisions(){
        for(int i = bodyParts; i > 0; i--){
            // if the head of the snake collides with the body of the snake, then the game is over
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        // if the head of the snake collides with the left border, then the game is over
        if(x[0] < 0){
            running = false;
        }
        // if the head of the snake collides with the right border, then the game is over
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // if the head of the snake collides with the top border, then the game is over
        if(y[0] < 0){
            running = false;
        }
        // if the head of the snake collides with the bottom border, then the game is over
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop(); // stop the timer
        }
    }

    public void gameOver(Graphics g){
        // Game Over text
        g.setColor(Color.red); // set the color of the text
        g.setFont(new Font("Ink Free", Font.BOLD, 75)); // set the font of the text
        FontMetrics metrics = getFontMetrics(g.getFont()); // helps with the centering of the text
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); // draw the text
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(running){
            move(); // move the snake
            checkApple(); // check if the snake has eaten an apple
            checkCollisions(); // check if the snake has collided with itself or with the borders
        }
        repaint(); // repaint the panel
        
    }

    /**
     * This class is used to handle the keyboard inputs
     */
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L'; // change the direction of the snake to left
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R'; // change the direction of the snake to right
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U'; // change the direction of the snake to up
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D'; // change the direction of the snake to down
                    }
                    break;
                    
            }
        }        
    }

}