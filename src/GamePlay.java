import java.awt.Graphics;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.Timer;


public class GamePlay extends JPanel implements ActionListener,KeyListener {

    private boolean play = false;
    private int score = 0;
    Timer timer;
    InputStream is = getClass().getResourceAsStream("BG.jpg");
    BufferedImage bgImage;
    {
        try {
            bgImage = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private int delay = 1;
    private int playerX = 310;
    private int ballposistionX = 120;
    private int ballposistionY = 350;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;
    private int brickRows = 3;
    private int brickCols = 7;
    private int totalBricks = brickRows * brickCols;
    private int level = 1;
    private Bricks brick;

    Music music = new Music();
    public GamePlay(){
        playMusic(0);
        brick = new Bricks(brickRows,brickCols);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }
    private void initializeLevel(int level) {
        switch (level) {
            case 1:
                // Initial level parameters
                break;
            case 2:
                delay = 5;
                timer.setDelay(delay);
                brickRows = 3;
                brickCols = 8;
                totalBricks = brickRows * brickCols;
                brick = new Bricks(brickRows, brickCols);
                break;
            case 3:
                delay = 3;
                timer.setDelay(delay);
                brickRows = 4;
                brickCols = 9;
                totalBricks = brickRows * brickCols;
                brick = new Bricks(brickRows, brickCols);
                break;
            case 4:
                delay = 1;
                timer.setDelay(delay);
                brickRows = 4;
                brickCols = 8;
                totalBricks = brickRows * brickCols;
                brick = new Bricks(brickRows, brickCols);
                break;

        }
    }

    public void paint(Graphics g){

        //for setting background
        g.setColor(Color.BLACK);
        g.drawImage(bgImage , 0 ,0 ,692,592,null);

        //drawing bricks
        brick.draw((Graphics2D)g);

        //setting scores
        g.setColor(Color.white);
        g.setFont(new Font("mv Boli",Font.BOLD,25));
        g.drawString("Score : "+score,549,30);

        //creating the borders
        g.setColor(Color.YELLOW);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(680,0,3,592);

        //creating the paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX,550,100,8);

        //creating the ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballposistionX,ballposistionY,20,20);

        if (!play && totalBricks <= 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You Won!", 250, 300);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to proceed to the next level.", 170, 350);
        }

        //logic for gameOver
        if (!play && ballposistionY > 570) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", 250, 300);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to restart.", 220, 350);
        }

        g.dispose();

    }

    public void actionPerformed(ActionEvent ae){



        if(play){
//            code for detection of ball with paddle
            if(new Rectangle(ballposistionX,ballposistionY,20,20).intersects(new Rectangle(playerX,550,100,8))){
                ballDirectionY = -ballDirectionY;;
        }

            //code for detecting bricks
            A: for(int i=0;i<brick.map.length;i++){
                for(int j=0;j<brick.map[0].length;j++){
                    if(brick.map[i][j]>0){

                        int brickX = j*brick.brickWidth+80;
                        int brickY = i*brick.brickHeight+50;
                        int brickWidth = brick.brickWidth;
                        int brickHeight = brick.brickHeight;

                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballrect = new Rectangle(ballposistionX,ballposistionY,20,20);
                        Rectangle brickRect = rect;

                        if(ballrect.intersects(brickRect)){
                            brick.setBrickValue(0,i,j);
                            totalBricks--;
                            score += 5;

                            //code for detecting left and right direction
                            if(ballposistionX+19<=brickRect.x || ballposistionX+1>= brickRect.x+brickRect.width){
                                ballDirectionX = -ballDirectionX;
                            }else{
                                ballDirectionY = -ballDirectionY;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposistionX += ballDirectionX;
            ballposistionY += ballDirectionY;
           //this is for the left border
            if(ballposistionX<0){
                ballDirectionX = -ballDirectionX;
            }
            //this is for the top border
            if(ballposistionY<0){
                ballDirectionY = -ballDirectionY;
            }
            //this is for the right
            if(ballposistionX>670){
                ballDirectionX = -ballDirectionX;
            }
            if (totalBricks <= 0) {
                play = false;
                repaint();
            }
            if (ballposistionY > 570) {
                play = false;
                stopMusic(0);
                repaint();
            }

        }

        repaint();
    }
    public void keyTyped(KeyEvent keyEvent){}
    public void keyReleased(KeyEvent keyEvent){}

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            if(playerX>=600){
                playerX = 600;
            }
            else{
                moveRight();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            if(playerX<10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }

        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            playMusic(0);
            if(!play){
                if (totalBricks <= 0) {
                    level++;
                    initializeLevel(level);
                    play = true;
                    ballposistionX = 120;
                    ballposistionY = 350;
                    ballDirectionX = -2;
                    ballDirectionY = -4;
                    playerX = 310;
                    totalBricks = brickRows * brickCols;
                    brick = new Bricks(brickRows, brickCols);
                    repaint();
                } else if (ballposistionY > 570) {
                    level=1;
                    play = true;
                    ballposistionX = 120;
                    ballposistionY = 350;
                    ballDirectionX = -1;
                    ballDirectionY = -2;
                    playerX = 310;
                    score = 0;
                    totalBricks = brickRows * brickCols;
                    brick = new Bricks(brickRows, brickCols);
                    repaint();
                }
            }
        }
    }

    //method for moverRight for the paddle
    public void moveRight(){
        play = true;
        playerX += 30;
    }

    //method for moving left
    public void moveLeft(){
        play = true;
        playerX -= 30;

    }

    //for sound first open a file and start music and loop
    public void playMusic(int i){

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(int i){
        music.stop();
    }

    public void playSoundEffect(int i){
        music.setFile(i);
        music.play();
    }


}
