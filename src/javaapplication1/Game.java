/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author Nathan
 */

@SuppressWarnings("serial")
public class Game extends JPanel {
    static int playerSize = 20;
    static int enemySize = 20;
    static int numEnemies = (int) (Math.random() * 20);
    static int[] enemyXLocations = new int[numEnemies];
    static int[] enemyYLocations = new int[numEnemies];
    static int xframe = 800;
    static int yframe = 600;
    static int x = 0;
    static int y = 0;
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
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLUE);
        Graphics2D player = (Graphics2D) g;
        player.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        player.fillOval(x, y, playerSize, playerSize);
        for(int a = 0; a < numEnemies; a++){
            g.setColor(Color.RED);
            Graphics2D enemy = (Graphics2D) g;
            enemy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            enemy.fillRect(enemyXLocations[a], enemyYLocations[a], playerSize, playerSize);
        }
        
    }
    
    public static int roundEnemyLocation(int n) {
        return (n + enemySize-1) / enemySize * enemySize;
    }
    
    public static void generateEnemies() {
        for (int a = 0; a < numEnemies; a++) {
            int tempX = (int) (Math.random() * xframe);
            int tempY = (int) (Math.random() * yframe);
            enemyXLocations[a] = roundEnemyLocation(tempX);
            enemyYLocations[a] = roundEnemyLocation(tempY);
        }
    }
    
    public static boolean collisionDetect(int x1, int y1, int x2, int y2){
        return (x1 == x2 && y1 == y2);
    }
    
    public static void initAI() throws InterruptedException{
        for (int a = 0; a < numEnemies; a++){
            double temp = Math.random();
            if (temp < .25 && enemyXLocations[a]-20 > 0) enemyXLocations[a] -= enemySize;
            else if (temp < .5 && temp >= .25 && enemyXLocations[a]+20 < xframe) enemyXLocations[a] += enemySize;
            if (temp < .75 && temp >= .5 && enemyYLocations[a]-20 > 0) enemyYLocations[a] -= enemySize;
            else if (temp > 1 && temp >= .75 && enemyYLocations[a]+20 < yframe) enemyYLocations[a] += enemySize;
        }
    }

    public static void main(String[] args) throws InterruptedException {

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
                        moveBallUp();
                        break;
                    case KeyEvent.VK_A:
                        moveBallLeft();
                        break;
                    case KeyEvent.VK_S:
                        moveBallDown();
                        break;
                    case KeyEvent.VK_D:
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
        int aiTimer = 0;
        Game game = new Game();
        frame.add(game);
        frame.setSize(xframe, yframe);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            if (aiTimer == 50) {
                aiTimer = 0;
                initAI();
            }
            for (int a = 0; a < numEnemies; a++){
                if (collisionDetect(x, y, enemyXLocations[a], enemyYLocations[a])){
                    System.exit(1);
                }
            }
            game.repaint();
            Thread.sleep(10);
            aiTimer++;
        }
    }
}
