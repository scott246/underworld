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

@SuppressWarnings("serial")
public class Game extends JPanel {
    static long startTime;
    static int playerSize = 20;
    static int enemySize = 20;
    static int rockSize = 20;
    static int playerHP = 100;
    static int playerMinDamage = 1;
    static int playerMaxDamage = 2;
    static final int numEnemies = (int) Math.ceil(Math.random() * 50);
    static final int numPowerups = (int) Math.ceil(Math.random() * 20);
    static final int numRocks = (int) 1000;//(Math.random() * 500);
    static int[] enemyXLocations = new int[numEnemies];
    static int[] enemyYLocations = new int[numEnemies];
    static int[] enemyHP = new int[numEnemies];
    static int[] powerupX = new int[numPowerups];
    static int[] powerupY = new int[numPowerups];
    static int[] rockXLocations = new int[numRocks];
    static int[] rockYLocations = new int[numRocks];
    static int xframe = 805;
    static int yframe = 595;
    static int x = 400;
    static int y = 300;
    static int lastDir = 0; //1 up, 2 left, 3 down, 4 right
    static int[] enemyLastDir = new int[numEnemies];
    private static void moveBallLeft() {
        if (x > 0) x -= playerSize;
    }
    private static void moveBallRight() {
        if (x < xframe - 40) x += playerSize;
    }
    private static void moveBallUp() {
        if (y > 0) y -= playerSize;
    }
    private static void moveBallDown() {
        if (y < yframe - 60) y += playerSize;
    }
    
    public static void generateMap() {
        int densityMultiplier = 6;
        int power = (int)(Math.random() * densityMultiplier);
        int rockCount = 0;
        for (int a = 0; a < xframe; a+=rockSize) {
            for (int b = 0; b < yframe; b+=rockSize) {
                while(power > 0) {
                    //generate border
                    if (a == 0 || a == 1 ||
                        b == 0 || 
                        a == roundRockLocation(xframe)-rockSize * 2 || 
                        b == roundRockLocation(yframe)-rockSize * 3) {
                        rockXLocations[rockCount] = roundRockLocation(a);
                        rockYLocations[rockCount] = roundRockLocation(b); 
                        if (rockCount++ >= numRocks-1) {
                            return;
                        }
                    }
                    b += 20;
                    power--;
                }
                if (a == x && b == y) { //rock would spawn on player
                    b += rockSize;
                }
                rockXLocations[rockCount] = roundRockLocation(a);
                rockYLocations[rockCount] = roundRockLocation(b);
                if (rockCount++ >= numRocks-1) {
                    return;
                }
                power = (int)(Math.random() * densityMultiplier);
            }
        }
    }
    
    final BufferedImage background;

    public Game() throws IOException {
        this.background = ImageIO.read(new File("C:\\Users\\Nathan\\Documents\\GitHub\\game\\src\\javaapplication1\\stone.jpg"));
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
        g.drawImage(background, 0, 0, this);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 40);
        g.setColor(Color.BLUE);
        Graphics2D player = (Graphics2D) g;
        player.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        player.fillOval(x, y, playerSize, playerSize);
        g.setColor(Color.LIGHT_GRAY);
        player.drawString(Integer.toString(playerHP), x, y+playerSize/2);
        for(int a = 0; a < numEnemies; a++){
            if (enemyXLocations[a] >= 0) {
                g.setColor(Color.RED);
                Graphics2D enemy = (Graphics2D) g;
                enemy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                enemy.fillRect(enemyXLocations[a], enemyYLocations[a], enemySize, enemySize);
                g.setColor(Color.BLACK);
                enemy.drawString(Integer.toString(enemyHP[a]), enemyXLocations[a], enemyYLocations[a]+enemySize/2);
            }
        }
        for(int a = 0; a < numRocks; a++){
            g.setColor(Color.BLACK);
            Graphics2D rock = (Graphics2D) g;
            rock.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            rock.fillRect(rockXLocations[a], rockYLocations[a], rockSize, rockSize);
        }
        g.setColor(Color.WHITE);
        Graphics2D text = (Graphics2D) g;
        text.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        text.setFont(new Font("Papyrus", Font.BOLD, 20));
        text.drawString("Time Alive: "+ Integer.toString((int)(getTimeAlive()/1000)), 10, 20);
        text.drawString("HP: "+ playerHP, 380, 20);
        text.drawString("Damage: "+playerMinDamage+"-"+playerMaxDamage, 660, 20);

    }
    
    public static int roundEnemyLocation(int n) {
        return (n + enemySize-1) / enemySize * enemySize;
    }
    
    public static int roundRockLocation(int n) {
        return (n + rockSize-1) / rockSize * rockSize;
    }
    
    public static double getTimeAlive(){
        return System.currentTimeMillis() - startTime;
    }
    
    public static void generateEnemies() {
        for (int a = 0; a < numEnemies; a++) {
            int tempX = (int) (Math.random() * xframe);
            int tempY = (int) (Math.random() * yframe);
            enemyXLocations[a] = roundEnemyLocation(tempX);
            enemyYLocations[a] = roundEnemyLocation(tempY);
            enemyHP[a] = 10;
        }
    }
    
    public static boolean collisionDetect(int x1, int y1, int x2, int y2){
        return (x1 == x2 && y1 == y2);
    }
   
    
    public static void initAI(){
        for (int a = 0; a < numEnemies; a++){
            double temp = Math.random();
            if (temp < .25 && enemyXLocations[a]-enemySize >= 0) {
                enemyXLocations[a] -= enemySize;
                enemyLastDir[a] = 2;
            }
            else if (temp < .5 && temp >= .25 && enemyXLocations[a]+enemySize < xframe) {
                enemyXLocations[a] += enemySize;
                enemyLastDir[a] = 4;
            }
            if (temp < .75 && temp >= .5 && enemyYLocations[a]-enemySize >= 0) {
                enemyYLocations[a] -= enemySize;
                enemyLastDir[a] = 1;
            }
            else if (temp < 1 && temp >= .75 && enemyYLocations[a]+enemySize < yframe) {
                enemyYLocations[a] += enemySize;
                enemyLastDir[a] = 3;
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
                System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
                System.out.println("Current location (x:"+x+", y:"+y+")");
                for (int a = 0; a < numEnemies; a++) {
                    System.out.println("Enemy location (x:"+enemyXLocations[a]+", y:"+enemyYLocations[a]+")");
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        lastDir = 1;
                        moveBallUp();
                        break;
                    case KeyEvent.VK_A:
                        lastDir = 2;
                        moveBallLeft();
                        break;
                    case KeyEvent.VK_S:
                        lastDir = 3;
                        moveBallDown();
                        break;
                    case KeyEvent.VK_D:
                        lastDir = 4;
                        moveBallRight();
                        break;
                    default:
                        break;
                }
                System.out.println("New location (x:"+x+", y:"+y+")");
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        generateEnemies();
        generateMap();
        int aiTimer = 0;
        Game game = new Game();
        frame.add(game);
        frame.setSize(xframe, yframe);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            if (aiTimer == 25) {
                aiTimer = 0;
                initAI();
            }
            for (int a = 0; a < numEnemies; a++){
                if (collisionDetect(x, y, enemyXLocations[a], enemyYLocations[a])){
                    playerHP -= Math.round(Math.random() * 10);
                    enemyHP[a] -= Math.round(Math.random() * playerMaxDamage);
                    if (playerHP <= 0) {
                        System.exit(1);
                    }
                    if (enemyHP[a] <= 0) {
                        enemyXLocations[a] = -enemySize;
                        enemyYLocations[a] = -enemySize;
                        playerMaxDamage++;
                        playerHP += Math.round(Math.random() * 20);
                    }
                    switch(lastDir){
                        case 1: //up
                            y += playerSize;
                            break;
                        case 2: //left
                            x += playerSize;
                            break;
                        case 3: //down
                            y -= playerSize;
                            break;
                        case 4: //right
                            x -= playerSize;
                            break;
                        default:
                            break;
                    }
                }
            }
            for (int a = 0; a < numRocks; a++){
                if (collisionDetect(x, y, rockXLocations[a], rockYLocations[a])){
                    switch(lastDir){
                        case 1: //up
                            y += playerSize;
                            break;
                        case 2: //left
                            x += playerSize;
                            break;
                        case 3: //down
                            y -= playerSize;
                            break;
                        case 4: //right
                            x -= playerSize;
                            break;
                        default:
                            break;
                    }
                }
                for (int b = 0; b < numEnemies; b++) {
                    if (collisionDetect(enemyXLocations[b], enemyYLocations[b], rockXLocations[a], rockYLocations[a])){
                        switch(enemyLastDir[b]){
                            case 1: //up
                                enemyYLocations[b] += enemySize;
                                break;
                            case 2: //left
                                enemyXLocations[b] += enemySize;
                                break;
                            case 3: //down
                                enemyYLocations[b] -= enemySize;
                                break;
                            case 4: //right
                                enemyXLocations[b] -= enemySize;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            game.repaint();
            Thread.sleep(10);
            aiTimer++;
        }
    }
}
