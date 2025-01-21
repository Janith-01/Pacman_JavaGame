import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class Pacman extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'W';// W S A D
        int velocityX = 0;
        int velocityY = 0;

        block(Image image, intx, int y,int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
        void updateDirection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall:){
                if(collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }
    }
    void updateVelocity (){
        if(this.direction == 'W'){
            this.velocityX = 0;
            this.velocityY = -tileSize/4;
    }
    else if(this.direction == 'S'){
        this.velocityX = 0;
        this.velocityY = tileSize/4;
    }
    else if(this.direction == 'A'){
        this.velocityX = -tileSize/4;
        this.velocityY = 0;
    }
    else if(this.direction == 'D'){
        this.velocityX = tileSize/4;
        this.velocityY = 0;
        }
    }
    void reset(){
        this.x = this.startX;
        this.y = this.startY;
    }
}

private int rowCount = 21;
private int colCount = 19;
private int tileSize = 32;
private int boardWidth = colCount * tileSize;
private int boardHeight = rowCount * tileSize;

private Image wallImage;
private Image blueGhostImage;
private Image orangeGhostImage;
private Image pinkGhostImage;
private Image redGhostImage;

private Image pacmanUpImage;
private Image pacmanDownImage;
private Image pacmanLeftImage;
private Image pacmanRightImage;

//X = wall, O = skip, P = pacman , ' ' = food
//Ghoust : B = blue, O = orange, P = pink, R = red
private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
};

    hashSet<Block> walls;
    HashSet<Block> food;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'W', 'S', 'A', 'D'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();
        for(Block ghost : ghosts){
            char newDiection = directions[random.nextInt(4)];
            ghost.updateDirection(newDiection); 
        }

        gameLoop = new Timer(1000/20, this);// 20fps
        gameLoop.start();

    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < colCount; c++){
                String row = titleMap[r];
                char titleMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if(titleMapChar == 'X'){//Block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if(titleMapChar == ' '){//Block food
                    Block foodBlock = new Block(null, x+14, y+14, 4, 4);
                    foods.add(foodBlock);
                }
                else if(titleMapChar == 'P'){//Block pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if(titleMapChar == 'b'){//Block blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(titleMapChar == 'o'){//Block orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(titleMapChar == 'p'){//Block pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(titleMapChar == 'r'){//Block red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
            }
        }
    }
    public void paintcomponent(Graphics g){
        super.paintComponts(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image,pacman.x, pacman.y, pacman.width, pacman.heigth, null);

        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for(Block wall : wals){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if(gameOver){
            g.drawString("Game Over: "+String.valueOf(score), titleSize/2, titleSize/2);
        }
        else{
            g.drawString("X "+String.valueOf(lives),+"Score: "+String.valueOf(score), titleSize/2, titleSize/2);
        }
    }
    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        for(Block wall : walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        for(Block ghost : ghosts){
            if (collision(pacman, ghost)){
                lives-=1;
                if(lives == 0){
                    gameOver = true;
                    eturn;
                }
                resetPositions();
            }
            if (ghost.y ==titleSize*9 && ghost.direction != 'W' && ghost.direction != 'S'){
                ghost.updateDirection('W');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for(Block wall : walls){
                if(collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth){
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }

        }
        //check food collision
        for(Block food : foods){
            if(collision(pacman, food)){
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if(foods.isEmpty()){
            loadMap();
            resetPositions();
        }
    }
    public boolean collision(Block b1, Block b2){
        return b1.x < b2.x + b2.width && b1.x + b1.width > b2.x && b1.y < b2.y + b2.height && b1.y + b1.height > b2.y;
    }
    public `void resetPositions(){
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for(Block ghost : ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(!gameOver){
            move();
        }
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }
    public void keyTyped(keyEvent e){}
    public void keyPressed(keyEvent e){}
    public void keyReleased(keyEvent e){
        if(gameOver){
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('W');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('S');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('A');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('D');
        }

        if (pacman.direction == 'W') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'S') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'A') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanRightImage;
        }
    }