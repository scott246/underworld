/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author Nathan
 */

enum powerups {
    GOLD, MINATTACK, MAXATTACK, MANA, HEALTH
};

@SuppressWarnings("serial")
public class Game extends JPanel {
    static long startTime;
    static int powerupSize = 20;
    static int ENEMIES = (int) Math.ceil(Math.random() * 50);
    static Enemy[] enemyList = new Enemy[ENEMIES];
    static int ROCKS = (int)1000;
    static Rock[] rockList = new Rock[ROCKS];
    static final int numPowerups = (int) 20;
    static int[] powerupX = new int[numPowerups];
    static int[] powerupY = new int[numPowerups];
    static int xframe = 805;
    static int yframe = 595;
 
    
    final BufferedImage background;

    public Game() throws IOException {
        this.background = ImageIO.read(new File("C:\\Users\\Nathan\\Documents\\GitHub\\game\\src\\javaapplication1\\stone.jpg"));
    }
    
    //very basic graphics
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
        g.drawImage(background, 0, 0, this);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 40);
        g.setColor(Color.BLUE);
        Graphics2D player1 = (Graphics2D) g;
        player1.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        player1.fillOval(Player.x, Player.y, Player.size, Player.size);
        g.setColor(Color.LIGHT_GRAY);
        player1.drawString(Integer.toString(Player.hp), Player.x, Player.y+Player.size/2);
        for(int a = 0; a < ENEMIES; a++){
                g.setColor(Color.RED);
                Graphics2D enemy = (Graphics2D) g;
                enemy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                enemy.fillRect(enemyList[a].getX(), enemyList[a].getY(), enemyList[a].getSize(), enemyList[a].getSize());
                g.setColor(Color.BLACK);
                enemy.drawString(Integer.toString(enemyList[a].getHP()), enemyList[a].getX(), enemyList[a].getY()+enemyList[a].getSize()/2);
        }
        for(int a = 0; a < numPowerups; a++) {
            g.setColor(Color.YELLOW);
            Graphics2D powerup = (Graphics2D) g;
            powerup.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            powerup.fillRect(powerupX[a], powerupY[a], powerupSize, powerupSize);
        }
        for(int a = 0; a < ROCKS; a++){
            g.setColor(Color.BLACK);
            Graphics2D rock = (Graphics2D) g;
            rock.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            rock.fillRect(rockList[a].getX(), rockList[a].getY(), rockList[a].getSize(), rockList[a].getSize());
        }
        g.setColor(Color.WHITE);
        Graphics2D text = (Graphics2D) g;
        text.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        text.setFont(new Font("Papyrus", Font.BOLD, 20));
        text.drawString("Time Alive: "+ Integer.toString((int)(getTimeAlive()/1000)), 10, 20);
        text.drawString("HP: "+ Player.hp, 380, 20);
        text.drawString("Damage: "+Player.minDamage+"-"+Player.maxDamage, 660, 20);

    }
    
    //helper function to enforce a grid layout
    public static int roundLocation(int n, int size) {
        return (n + size-1) / size * size;
    }
    
    //makes the map and border
    public static void generateMap() {
        int densityMultiplier = 6;
        double powerupFrequency = .01;
        int power = (int)(Math.random() * densityMultiplier);
        int rockCount = 0;
        int powerupCount = 0;
        for (int a = 0; a < xframe; a+=Rock.size) {
            for (int b = 0; b < yframe; b+=Rock.size) {
                while(power > 0) {
                    //generate border
                    if (a == 0 || b == Rock.size ||
                        b == 0 || 
                        a == roundLocation(xframe, Rock.size)-Rock.size * 2 || 
                        b == roundLocation(yframe, Rock.size)-Rock.size * 3) {
                        rockList[rockCount] = new Rock();
                        rockList[rockCount].setX(roundLocation(a, Rock.size));
                        rockList[rockCount].setY(roundLocation(b, Rock.size)); 
                        if (rockCount++ >= ROCKS-1) {
                            return;
                        }
                    }
                    if (Math.random() < powerupFrequency) {
                        if (powerupCount <= numPowerups-1) {
                            powerupX[powerupCount] = roundLocation(a, Rock.size);
                            powerupY[powerupCount] = roundLocation(b, Rock.size);
                            powerupCount++;
                        }
                    }
                    b += 20;
                    power--;
                }
                if (a == Player.x && b == Player.y) { //rock would spawn on Player
                    b += Rock.size;
                }
                rockList[rockCount] = new Rock();
                rockList[rockCount].setX(roundLocation(a, Rock.size));
                rockList[rockCount].setY(roundLocation(b, Rock.size));
                if (rockCount++ >= ROCKS-1) {
                    return;
                }
                power = (int)(Math.random() * densityMultiplier);
            }
        }
        ROCKS = rockCount;
    }
    
    //used to calculate time alive for HUD
    public static double getTimeAlive(){
        return System.currentTimeMillis() - startTime;
    }
    
    //makes bad guys
    public static void generateEnemies() {
        for (int a = 0; a < ENEMIES; a++){
            int tempX = (int) (Math.random() * xframe);
            int tempY = (int) (Math.random() * yframe);
            Enemy e = new Enemy();
            e.setHP((int)Math.ceil((Math.random() * e.getMaxHP())));
            e.setX(roundLocation(tempX, e.getSize()));
            e.setY(roundLocation(tempY, e.getSize()));
            e.setLastDir(1);
            enemyList[a] = e; 
        }

    }
    
    //basic collision detection system
    public static boolean collisionDetect(int x1, int y1, int x2, int y2){
        return (x1 == x2 && y1 == y2);
    }
   
    //allows the bad guys to wander around
    public static void initAI(){
        for (int a = 0; a < ENEMIES; a++){
            double temp = Math.random(); //uses a random number to calculate which way to go
            //go left
            if (temp < .25 && enemyList[a].getX()-enemyList[a].getSize() >= 0) {
                enemyList[a].setX(enemyList[a].getX()-enemyList[a].getSize());
                enemyList[a].setLastDir(2); //left
            }
            //go right
            else if (temp < .5 && temp >= .25 && enemyList[a].getX()+enemyList[a].getSize() < xframe) {
                enemyList[a].setX(enemyList[a].getX()+enemyList[a].getSize());
                enemyList[a].setLastDir(4); //right
            }
            //go up
            if (temp < .75 && temp >= .5 && enemyList[a].getY()-enemyList[a].getSize() >= 0) {
                enemyList[a].setY(enemyList[a].getY()-enemyList[a].getSize());
                enemyList[a].setLastDir(1); //up
            }
            //go down
            else if (temp < 1 && temp >= .75 && enemyList[a].getY()+enemyList[a].getSize() < yframe) {
                enemyList[a].setY(enemyList[a].getY()+enemyList[a].getSize());
                enemyList[a].setLastDir(3); //down
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        startTime = System.currentTimeMillis();
        JFrame frame = new JFrame("Game"); 
        
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        Player.lastDir = 1;
                        Player.movePlayerUp();
                        break;
                    case KeyEvent.VK_A:
                        Player.lastDir = 2;
                        Player.movePlayerLeft();
                        break;
                    case KeyEvent.VK_S:
                        Player.lastDir = 3;
                        Player.movePlayerDown();
                        break;
                    case KeyEvent.VK_D:
                        Player.lastDir = 4;
                        Player.movePlayerRight();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(1);
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        generateEnemies();
        generateMap();
        int aiTimer = 0;
        double enemyFPS = 1;
        int gameFPS = 60;
        Game game = new Game();
        frame.add(game);
        frame.setSize(xframe, yframe);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            if (aiTimer == (int)(100/enemyFPS)) {
                aiTimer = 0;
                initAI();
            }
            for (int a = 0; a < ENEMIES; a++){
                if (collisionDetect(Player.x, Player.y, enemyList[a].getX(), enemyList[a].getY())){
                    Player.hp -= Math.round(Math.random() * enemyList[a].getHP());
                    enemyList[a].setHP(enemyList[a].getHP()-(int)Math.round(Player.minDamage + (Math.random() * Player.maxDamage)));
                    if (Player.hp <= 0) {
                        System.exit(1);
                    }
                    if (enemyList[a].getHP() <= 0) {
                        enemyList[a].setX(-enemyList[a].getSize());
                        enemyList[a].setY(-enemyList[a].getSize());
                        Player.hp += Math.ceil(Math.random() * 10);
                    }
                    switch(Player.lastDir){
                        case 1: //up
                            Player.y += Player.size;
                            break;
                        case 2: //left
                            Player.x += Player.size;
                            break;
                        case 3: //down
                            Player.y -= Player.size;
                            break;
                        case 4: //right
                            Player.x -= Player.size;
                            break;
                        default:
                            break;
                    }
                }
            }
            for (int a = 0; a < ROCKS; a++){
                if (collisionDetect(Player.x, Player.y, rockList[a].getX(), rockList[a].getY())){
                    switch(Player.lastDir){
                        case 1: //up
                            Player.y += Player.size;
                            break;
                        case 2: //left
                            Player.x += Player.size;
                            break;
                        case 3: //down
                            Player.y -= Player.size;
                            break;
                        case 4: //right
                            Player.x -= Player.size;
                            break;
                        default:
                            break;
                    }
                }
                for (int b = 0; b < ENEMIES; b++) {
                    if (collisionDetect(enemyList[b].getX(), enemyList[b].getY(), rockList[a].getX(), rockList[a].getY())){
                        switch(enemyList[b].getLastDir()){
                            case 1: //up
                                enemyList[b].setY(enemyList[b].getY()+enemyList[b].getSize());
                                break;
                            case 2: //left
                                enemyList[b].setX(enemyList[b].getX()+enemyList[b].getSize());
                                break;
                            case 3: //down
                                enemyList[b].setY(enemyList[b].getY()-enemyList[b].getSize());
                                break;
                            case 4: //right
                                enemyList[b].setX(enemyList[b].getX()-enemyList[b].getSize());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            game.repaint();
            Thread.sleep(1000/gameFPS);
            aiTimer++;
        }
    }
}
