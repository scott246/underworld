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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nathan
 */

@SuppressWarnings("serial")
public class Game extends JPanel {
    static long startTime;
    static int ENEMIES = (int) Math.ceil(Math.random() * 50);
    static Enemy[] enemyList = new Enemy[ENEMIES];
    static int ROCKS = (int)1000;
    static Rock[] rockList = new Rock[ROCKS];
    static int POWERUPS = (int)200;
    static Powerup[] powerupList = new Powerup[POWERUPS];
    static int xframe = 805;
    static int yframe = 595;
    static int level = 0;
    static private AtomicBoolean paused;
    static private AtomicBoolean gameOver;
    static private Thread thread;
    
    final BufferedImage background;

    public Game() throws IOException {
        this.background = ImageIO.read(new File("C:\\Users\\Nathan\\Documents\\GitHub\\game\\src\\javaapplication1\\stone.jpg"));
        paused = new AtomicBoolean(false);
        gameOver = new AtomicBoolean(false);
    }
    
    //very basic graphics
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
        g.drawImage(background, 0, 0, this);
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
        for(int a = 0; a < POWERUPS; a++) {
            g.setColor(Color.YELLOW);
            Graphics2D powerup = (Graphics2D) g;
            powerup.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            powerup.fillRect(powerupList[a].getX(), powerupList[a].getY(), powerupList[a].getSize(), powerupList[a].getSize());
            g.setColor(Color.BLACK);
            powerup.drawString(powerupList[a].getTypeString(), powerupList[a].getX(), powerupList[a].getY()+Powerup.size/2);
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
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        text.drawString("Time Alive: "+ Integer.toString((int)(getTimeAlive()/1000)), 10, 15);
        g.setColor(Color.YELLOW);
        text.drawString("Gold: "+Player.gp, 10, 30);
        g.setColor(Color.WHITE);
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        text.drawString("Level: "+ level, 340, 15);
        g.setColor(Color.RED);
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        text.drawString("HP: "+ Player.hp, 340, 30);
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        text.drawString("Damage: "+Player.minDamage+"-"+Player.maxDamage, 660, 15);
        g.setColor(Color.BLUE);
        text.drawString("Mana: "+Player.mana, 660, 30);
        g.setColor(Color.WHITE);
        if (paused.get()){
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String text1 = "PAUSED";
            int textx = (xframe - metrics.stringWidth(text1))/2;
            int texty = ((yframe - metrics.getHeight())/2) + metrics.getAscent();
            text.drawString(text1, textx, texty);
        }
        if (gameOver.get()){
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String text1 = "GAME OVER";
            int textx = (xframe - metrics.stringWidth(text1))/2;
            int texty = ((yframe - metrics.getHeight())/2) + metrics.getAscent();
            text.drawString(text1, textx, texty);
        }
            
        

    }
    
    //helper function to enforce a grid layout
    public static int roundLocation(int n, int size) {
        return (n + size-1) / size * size;
    }
    
    //makes the map, border, and powerups
    public static void generateMap() {
        int densityMultiplier = 6;
        double powerupFrequency = .03;
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
                        a == roundLocation(xframe, Rock.size) ||
                        b == roundLocation(yframe, Rock.size)-Rock.size * 3 ||
                        b == roundLocation(yframe, Rock.size)) {
                        rockList[rockCount] = new Rock();
                        rockList[rockCount].setX(roundLocation(a, Rock.size));
                        rockList[rockCount].setY(roundLocation(b, Rock.size)); 
                        if (rockCount++ >= ROCKS-1) {
                            return;
                        }
                    }
                    //generate powerups
                    if (Math.random() < powerupFrequency) {
                        if (powerupCount <= POWERUPS-1) {
                            powerupList[powerupCount] = new Powerup();
                            powerupList[powerupCount].setX(roundLocation(a, Powerup.size));
                            powerupList[powerupCount].setY(roundLocation(b, Powerup.size));
                            powerupList[powerupCount].setType(Math.random());
                            if (powerupCount++ >= POWERUPS-1) {
                                powerupCount--;
                            }
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
        POWERUPS = powerupCount;
    }
    
    //used to calculate time alive for HUD
    public static double getTimeAlive(){
        return System.currentTimeMillis() - startTime;
    }
    
    //makes bad guys
    public static void generateEnemies() {
        for (int a = 0; a < ENEMIES; a++){            
            Enemy e = new Enemy();
            int tempX = (int) (Math.random() * xframe - e.getSize() * 2);
            int tempY = (int) (Math.random() * yframe - e.getSize() * 3);
            if (tempX == Player.x && tempY == Player.y) {
                tempX += e.getSize();
                tempY += e.getSize();
            }
            e.setHP((int)Math.ceil((Math.random() * e.getMaxHP() * (level + 1))));
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
    
    public static void knockback(int lastDir, int size, int x, int y) {
        switch(lastDir){
            case 1: //up
                y += size;
                break;
            case 2: //left
                x += size;
                break;
            case 3: //down
                y -= size;
                break;
            case 4: //right
                x -= size;
                break;
            default:
                break;
        }
    }
    
    public static void nextLevel(Game g, JFrame f) throws IOException {
        f.add(g);
        f.setSize(xframe, yframe);
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level++;
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
                        if (!paused.get()){
                            paused.set(true);
                        }
                        else {
                            paused.set(false);
                            synchronized(thread){
                               thread.notify(); 
                            }
                        }
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
        final double enemyFPS = 2;
        final int gameFPS = 60;
        Game game = new Game();
        nextLevel(game, frame);

        //game loop
        Runnable runnable = () -> {
            int aiTimer = 0;
            while (true) {
                if (paused.get() || gameOver.get()) {
                    synchronized(thread) {
                        try {
                            frame.repaint();
                            thread.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                int deadEnemies = 0;
                for (int a = 0; a < ENEMIES; a++) { 
                    if (enemyList[a].getX() < 0 ||
                            enemyList[a].getY() < 0 ||
                            enemyList[a].getX() > xframe ||
                            enemyList[a].getY() > yframe) deadEnemies++;
                }
                if (ENEMIES == deadEnemies) {
                    frame.remove(frame);
                    Game g = null;
                    try {
                        g = new Game();
                    } catch (IOException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        nextLevel(g, frame);
                    } catch (IOException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    generateEnemies();
                    generateMap();
                }
                
                if (aiTimer == (int)(100/enemyFPS)) {
                    aiTimer = 0;
                    initAI();
                }
                //player collides with enemy
                for (int a = 0; a < ENEMIES; a++){
                    if (collisionDetect(Player.x, Player.y, enemyList[a].getX(), enemyList[a].getY())){
                        Player.hp -= Math.round(Math.random() * enemyList[a].getHP());
                        enemyList[a].setHP(enemyList[a].getHP()-(int)Math.round(Player.minDamage + (Math.random() * Player.maxDamage)));
                        if (Player.hp <= 0) {
                            gameOver.set(true);
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
                //player collides with powerup
                for (int a = 0; a < POWERUPS; a++) {
                    if (collisionDetect(Player.x, Player.y, powerupList[a].getX(), powerupList[a].getY())) {
                        powerupList[a].setX(-Powerup.size);
                        powerupList[a].setY(-Powerup.size);
                        switch(powerupList[a].getType()) {
                            case GOLD:
                                Player.gp += (int) (Math.random() * level * 10);
                                break;
                            case HEALTH:
                                Player.hp += (int) (Math.random() * level * 10);
                                break;
                            case MINATTACK:
                                Player.minDamage += (int) (Math.random() * level * 5);
                                if (Player.minDamage > Player.maxDamage){
                                    Player.maxDamage = Player.minDamage;
                                }
                                break;
                            case MAXATTACK:
                                Player.maxDamage += (int) (Math.random() * level * 5);
                                break;
                            case MANA:
                                Player.mana += (int) (Math.random() * level * 10);
                                break;
                        }
                    }
                }
                //player collides with rock
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
                    //enemy collides with rock
                    for (int b = 0; b < ENEMIES; b++) {
                        for (int c = 0; c < ENEMIES; c++) {
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
                            //enemy collides with enemy
                            if (collisionDetect(enemyList[b].getX(), enemyList[b].getY(), enemyList[c].getX(), enemyList[c].getY()) && c != b){
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
                        //enemy collides with powerup
                        for (int d = 0; d < POWERUPS; d++) {
                            if (collisionDetect(enemyList[b].getX(), enemyList[b].getY(), powerupList[d].getX(), powerupList[d].getY())){
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
                }
                frame.repaint();
                try {
                    thread.sleep(1000/gameFPS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                aiTimer++; 
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }
}
