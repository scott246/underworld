/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * TODO:
 * fix game timer
 * make it possible to shoot more than one arrow at a time
 * animations
 *  magic
 *  movement
 *  arrows
 * tell the player in real time what they can/can't do
 */
package javaapplication1;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author Nathan
 */

@SuppressWarnings("serial")
public class Game extends JPanel {
    //time variables
    static long startTime = 0;
    static long pauseTime = 0;
    static long startPauseTime = 0;
    
    //enemy variables
    static int ENEMIES = (int) 50;//Math.ceil(Math.random() * 50);
    static Enemy[] enemyList = new Enemy[ENEMIES];
    
    //rock variables
    static int ROCKS = (int)2000;
    static Rock[] rockList = new Rock[ROCKS];
    
    //powerup variables
    static int POWERUPS = (int)200;
    static Powerup[] powerupList = new Powerup[POWERUPS];
    
    //store
    static Store store = new Store();
    
    //arrow (one at a time for now)
    static Arrow arrow;
    
    //screen dimensions
    static final int xframe = 800;
    static final int yframe = 600;
    
    //game variables
    static int level = 0;
    static int enemiesKilled = 0;
    static public AtomicBoolean paused;
    static public AtomicBoolean gameOver;
    static public Thread thread;
    static boolean instructionDisplay = false;
    
    //high score variables
    static int highScore = 0;
    static int secondHigh = 0;
    static int thirdHigh = 0;

    public Game() throws IOException {
        paused = new AtomicBoolean(false);
        gameOver = new AtomicBoolean(false);
    }
    
    /**
     * Connect via JDBC to SQLite database for high score keeping
     */
    public static void connectToDB() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");

            String u = "CREATE TABLE IF NOT EXISTS highscores "
                + "(date DATETIME, "
                + "enemiesKilled INTEGER);";
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(u);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * Add score to the database
     * @param score
     */
    public static void addScore(int score) {
        Connection conn = null;
        try {
            //get current date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now)); //2016/11/16 12:08:43
            // db parameters
            String url = "jdbc:sqlite:game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            String u = "INSERT INTO highscores "
                    + "VALUES ('"+dtf.format(now)+"', "+score+");";
            Statement st = conn.createStatement();
            st.executeUpdate(u);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get nth highest score from database
     * @param n
     * @return nth highest score
     */
    public static int getHighScore(int n){
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            String q = "SELECT DISTINCT enemiesKilled " +
                "FROM highscores " +
                "ORDER BY 1 DESC " +
                "LIMIT 1 OFFSET "+(n-1)+";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(q);
            int max = 0;
            if (rs.next()){
                max = rs.getInt(1);
            }
            conn.close();
            return max;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Helper function to enforce a grid layout. Takes n and rounds it to the 
     * nearest size
     * @param n
     * @param size
     * @return
     */
    public static int roundLocation(int n, int size) {
        return (n + size-1) / size * size;
    }

    /**
     * Generates a map, border, and powerups
     * Algorithm: 
     *      generates a random power based on density
     *      takes that power and goes down the y axis until that power is reached
     *      places a rock, generates new power
     *      continue until you reach the bottom of the page, then increment x
     *      continue until you reach the bottom right edge of the screen
     */
    public static void generateMap() {
        //density of rocks (lower = more dense)
        double densityMultiplier = 3;
        
        //density of powerups (lower = less dense)
        double powerupFrequency = .03;
        int power = (int)Math.ceil((1 + Math.random()) * densityMultiplier);
        int rockCount = 0;
        int powerupCount = 0;
        for (int a = 0; a < xframe; a+=Rock.size) {
            for (int b = 0; b < yframe; b+=Rock.size) {
                while(power > 0) {
                    
                    //use the rocks to generate a border
                    if (a == 0 || b == Rock.size || b == Rock.size * 2 ||
                        b == 0 || 
                        a == roundLocation(xframe, Rock.size)-Rock.size ||
                        a == roundLocation(xframe, Rock.size) ||
                        b == roundLocation(yframe, Rock.size)-Rock.size * 2 ||
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
                            powerupList[powerupCount].setX(roundLocation(
                                    a, Powerup.size));
                            powerupList[powerupCount].setY(roundLocation(
                                    b, Powerup.size));
                            powerupList[powerupCount].setType(Math.random());
//                            //ensure powerups don't spawn in unreachable spots
//                            //  (i.e. spots with rocks surrounding all sides)
//                            boolean up = false;
//                            boolean left = false;
//                            boolean down = false;
//                            boolean right = false;
//                            for (int c = 0; c < ROCKS; b++){
//                                if (powerupList[powerupCount].getX() + 
//                                        powerupList[powerupCount].getSize() == 
//                                        rockList[c].getX()) 
//                                    right = true;
//                                if (powerupList[powerupCount].getX() - 
//                                        powerupList[powerupCount].getSize() == 
//                                        rockList[c].getX())  
//                                    left = true;
//                                if (powerupList[powerupCount].getY() + 
//                                        powerupList[powerupCount].getSize() == 
//                                        rockList[c].getY())  
//                                    up = true;
//                                if (powerupList[powerupCount].getY() - 
//                                        powerupList[powerupCount].getSize() == 
//                                        rockList[c].getY())  
//                                    down = true;
//                            }
//                            if (up && left && down && right) {
//                                powerupList[powerupCount].setY(
//                                        powerupList[powerupCount].getY() + 
//                                        powerupList[powerupCount].getSize()*2);
//                            }
                            if (powerupCount++ >= POWERUPS-1) {
                                powerupCount--;
                            }
                        }
                    }
                    b += 20;
                    power--;
                }
                
                //avoid having rocks spawn on player
                if (roundLocation(a, Rock.size) == Player.x &&
                        roundLocation(b, Rock.size) == Player.y) {
                    b -= Rock.size;
                }
                
                //make the rock
                rockList[rockCount] = new Rock();
                rockList[rockCount].setX(roundLocation(a, Rock.size));
                rockList[rockCount].setY(roundLocation(b, Rock.size));
                if (rockCount++ >= ROCKS-1) {
                    return;
                }
                
                //generate new power and start over
                power = (int)Math.ceil(
                        (Math.random() + Math.random()) * densityMultiplier);
            }
        }
        ROCKS = rockCount;
        POWERUPS = powerupCount;
    }
    
    /**
     * Used to calculate time alive for the HUD
     * @return time alive
     */
    public static double getTimeAlive(){
        return System.currentTimeMillis() - startTime - pauseTime;
    }

    /**
     * Spawns bad guys in the map
     */
    public static void generateEnemies() {
        for (int a = 0; a < ENEMIES; a++){            
            Enemy e = new Enemy();
            
            //generate a random x and y variable
            int tempX = roundLocation(
                    (int) (Math.random() * xframe - e.getSize() * 3),
                    e.getSize());
            int tempY = roundLocation(
                    (int) (Math.random() * yframe - e.getSize() * 4),
                    e.getSize());
            
            //ensure enemies don't spawn on player
            if (tempX == Player.x && tempY == Player.y) {
                tempX += e.getSize();
                tempY += e.getSize();
            }
            
//            //ensure enemies don't spawn in unreachable spots
//            //  (i.e. spots with rocks surrounding all sides)
//            boolean up = false;
//            boolean left = false;
//            boolean down = false;
//            boolean right = false;
//            for (int b = 0; b < ROCKS; b++){
//                if (tempX + e.getSize() == rockList[b].getX()) right = true;
//                if (tempX - e.getSize() == rockList[b].getX()) left = true;
//                if (tempY + e.getSize() == rockList[b].getY()) up = true;
//                if (tempY - e.getSize() == rockList[b].getY()) down = true;
//            }
//            if (up && left && down && right) {
//                tempX = Player.x + e.getSize() * 2;
//                tempY = Player.y + e.getSize() * 2;
//            }
            
            
            //set enemy properties
            e.setHP((int)Math.ceil((Math.random() * e.getMaxHP() * (level))));
            e.setX(tempX);
            e.setY(tempY);
            e.setLastDir(0);
            enemyList[a] = e; 
            System.out.println("Enemy "+a+" created at ("+e.getX()+","+e.getY()+")");
        }
    }

    /**
     * Basic collision detection system.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean collisionDetect(int x1, int y1, int x2, int y2){
        return (x1 == x2 && y1 == y2);
    }

    /**
     * Initiates the basic AI system
     */
    public static void initAI(){
        if (!gameOver.get()){
            for (int a = 0; a < ENEMIES; a++){
                
                //uses a random number to decide which way the enemy goes
                double temp = Math.random();
                
                //go left
                if (temp < .25 && 
                        enemyList[a].getX()-enemyList[a].getSize() >= 0 &&
                        enemyList[a].getX()-enemyList[a].getSize() != Store.x) {
                    enemyList[a].setX(
                            enemyList[a].getX()-enemyList[a].getSize());
                    enemyList[a].setLastDir(2); //left
                }
                
                //go right
                else if (temp < .5 && temp >= .25 && 
                        enemyList[a].getX()+enemyList[a].getSize() < xframe &&
                        enemyList[a].getX()+enemyList[a].getSize() != Store.x) {
                    enemyList[a].setX(
                            enemyList[a].getX()+enemyList[a].getSize());
                    enemyList[a].setLastDir(4); //right
                }
                
                //go up
                if (temp < .75 && temp >= .5 && 
                        enemyList[a].getY()-enemyList[a].getSize() >= 0 &&
                        enemyList[a].getY()-enemyList[a].getSize() != Store.y) {
                    enemyList[a].setY(
                            enemyList[a].getY()-enemyList[a].getSize());
                    enemyList[a].setLastDir(1); //up
                }
                
                //go down
                else if (temp < 1 && temp >= .75 && 
                        enemyList[a].getY()+enemyList[a].getSize() < yframe &&
                        enemyList[a].getY()+enemyList[a].getSize() != Store.y) {
                    enemyList[a].setY(
                            enemyList[a].getY()+enemyList[a].getSize());
                    enemyList[a].setLastDir(3); //down
                }
            }
        }
    }
    
    /**
     * Allows a basic arrow shooting animation by having the arrow move 
     * according to its speed variable once every game loop iteration.
     */
    public static void animateArrowShot(){
        switch(Arrow.dir){
            case 1: //up
                Arrow.y = Arrow.y-Arrow.speed; break;
            case 2: //left
                Arrow.x = Arrow.x-Arrow.speed; break;
            case 3: //down
                Arrow.y = Arrow.y+Arrow.speed; break;
            case 4: //right
                Arrow.x = Arrow.x+Arrow.speed; break;
        }
        
        //once the arrow hits a rock, the arrow is gone
        for (int a = 0; a < ROCKS; a++){
            if (rockList[a].getX() == Arrow.x &&
                rockList[a].getY() == Arrow.y) {
                Arrow.exists = false;
                Arrow.x = -Arrow.size;
                Arrow.y = -Arrow.size;
            }
        }
        
        //once the arrow hits an enemy, the arrow is gone
        for (int a = 0; a < ENEMIES; a++){
            if (enemyList[a].getX() == Arrow.x &&
                enemyList[a].getY() == Arrow.y) {
                Arrow.exists = false;
                Arrow.x = -Arrow.size;
                Arrow.y = -Arrow.size;
            }
        }
    }
    
    /**
     * Changes the entire game screen to move on to the next level.
     * @param g
     * @param f
     * @throws IOException
     */
    public static void nextLevel(Game g, JFrame f) throws IOException {
        f.add(g);
        f.setSize(xframe, yframe);
        f.setResizable(false);
        f.setVisible(true);
        f.setPreferredSize(new Dimension(xframe, Rock.size));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level++;
    }
    
    /**
     * Used to calculate which enemies will be damaged when attack magic
     * (destroying everything in a 3 block radius) is used.
     * @return enemies to be damaged
     */
    public static Enemy[] getEnemiesIn3BlockRadius() {
        Enemy[] enemies = new Enemy[50];
        int enemyCount = 0;
        for (int a = 0; a < ENEMIES; a++) {
            if (enemyList[a].getX() >= Player.x - 3*Player.size && 
                enemyList[a].getX() <= Player.x + 3*Player.size &&
                enemyList[a].getY() >= Player.y - 3*Player.size &&
                enemyList[a].getY() <= Player.y + 3*Player.size){
                enemies[enemyCount] = enemyList[a];
                enemyCount++;
            }
        }
        return enemies;
    }
    
    //used to calculate what the arrow will hit

    /**
     * Used to calculate which enemy will be damaged when an arrow is shot at 
     * it.
     * @param dir
     * @return enemy to be damaged
     */
    public static Enemy getEnemyInDirection(int dir) {
        int arrowx = Player.x;
        int arrowy = Player.y;
        boolean hitRock = false;
        while (!hitRock) {
            for (int a = 0; a < ROCKS; a++){
                if (rockList[a].getX() == arrowx &&
                    rockList[a].getY() == arrowy) {
                    hitRock = true;
                }
            }
            for (int b = 0; b < ENEMIES; b++) {
                if (enemyList[b].getX() == arrowx &&
                    enemyList[b].getY() == arrowy){
                    return enemyList[b];
                }
            }
            switch (dir) {
                case 1://up
                    arrowy-=Player.size; break;
                case 2://left
                    arrowx-=Player.size; break;
                case 3://down
                    arrowy+=Player.size; break;
                case 4://right
                    arrowx+=Player.size; break;
                default:
                    return null; //invalid input
            }
        }
        return null; //hit a rock or the end of the screen
    }

    /**
     * All the graphics for the game are 2D Graphics for now. This is the method
     * that draws them.
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
        
        //draw background
        g.setColor(Color.GRAY);
        Graphics2D background = (Graphics2D) g;
        background.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        background.fillRect(0, 0, xframe, yframe);
        
        //draw enemies
        for(int a = 0; a < ENEMIES; a++){
                g.setColor(Color.RED);
                Graphics2D enemy = (Graphics2D) g;
                enemy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                System.out.println("Making enemy "+a+"...");
                enemy.fillRect(
                        enemyList[a].getX(),
                        enemyList[a].getY(),
                        enemyList[a].getSize(),
                        enemyList[a].getSize());
                g.setColor(Color.BLACK);
                enemy.drawString(
                        Integer.toString(enemyList[a].getHP()), 
                        enemyList[a].getX(), 
                        enemyList[a].getY()+enemyList[a].getSize()/2);
        }
        
        //draw powerups
        for(int a = 0; a < POWERUPS; a++) {
            g.setColor(Color.YELLOW);
            Graphics2D powerup = (Graphics2D) g;
            powerup.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            powerup.fillRect(
                    powerupList[a].getX(), 
                    powerupList[a].getY(), 
                    powerupList[a].getSize(), 
                    powerupList[a].getSize());
            g.setColor(Color.BLACK);
            powerup.drawString(
                    powerupList[a].getTypeString(), 
                    powerupList[a].getX(), 
                    powerupList[a].getY()+Powerup.size/2);
        }
        
        //draw store
        g.setColor(Color.GREEN);
        Graphics2D shop = (Graphics2D) g;
        shop.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        shop.fillRect(Store.x, Store.y, Store.size, Store.size);
        
        //draw arrow
        g.setColor(Color.BLACK);
        Graphics2D a = (Graphics2D) g;
        a.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        a.fillOval(Arrow.x + 5, Arrow.y + 5, Arrow.size, Arrow.size);
        
        //draw player
        g.setColor(Color.BLUE);
        Graphics2D player1 = (Graphics2D) g;
        player1.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        player1.fillOval(Player.x, Player.y, Player.size, Player.size);
        g.setColor(Color.LIGHT_GRAY);
        player1.drawString(
                Integer.toString(Player.hp), Player.x, Player.y+Player.size/2);
        
        //draw rocks
        for(int b = 0; b < ROCKS; b++){
            g.setColor(Color.BLACK);
            Graphics2D rock = (Graphics2D) g;
            rock.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            rock.fillRect(
                    rockList[b].getX(), 
                    rockList[b].getY(), 
                    rockList[b].getSize(), 
                    rockList[b].getSize());
        }
        
        //draw all text
        g.setColor(Color.WHITE);
        Graphics2D text = (Graphics2D) g;
        text.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        //alive time
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        String aliveTime = "Time Alive: "+ Integer.toString(
                (int)(getTimeAlive()/1000));
        text.drawString(aliveTime, 10, 15);
        //gold
        g.setColor(Color.YELLOW);
        text.drawString("Gold: "+Player.gp, 10, 30);
        //level
        g.setColor(Color.WHITE);
        FontMetrics m = g.getFontMetrics(g.getFont());
        String ltext = "==Level " +level+"==";
        text.drawString(ltext, (xframe - m.stringWidth(ltext))/2, 45);
        //arrows
        g.setColor(Color.WHITE);
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        FontMetrics m1 = g.getFontMetrics(g.getFont());
        String artext = "Arrows: " + Player.arrows;
        int textx = (xframe - m1.stringWidth(ltext))/3;
        int texty = 15;
        text.drawString(artext, textx, texty);
        //HP
        g.setColor(Color.RED);
        FontMetrics m2 = g.getFontMetrics(g.getFont());
        String htext = "HP: " + Player.hp;
        int htextx = (xframe - m2.stringWidth(htext))/3;
        int htexty = 30;
        text.drawString(htext, htextx, htexty);
        //attack magic
        g.setColor(Color.RED);
        FontMetrics m3 = g.getFontMetrics(g.getFont());
        String atext = "Attack Magic: " + Player.attackMagic;
        int atextx = (xframe - m3.stringWidth(atext))*2/3;
        int atexty = 15;
        text.drawString(atext, atextx, atexty);
        //defense magic
        g.setColor(Color.BLUE);
        FontMetrics m4 = g.getFontMetrics(g.getFont());
        String dtext = "Defense Magic: " + Player.defenseMagic;
        int dtextx = (xframe - m4.stringWidth(dtext))*2/3;
        int dtexty = 30;
        text.drawString(dtext, dtextx, dtexty);
        //damage
        text.setFont(new Font("Courier New", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        FontMetrics m5 = g.getFontMetrics(g.getFont());
        String datext = "Damage: "+Player.minDamage+"-"+Player.maxDamage;
        int datextx = xframe - Rock.size - m5.stringWidth(datext);
        int datexty = 15;
        text.drawString(datext, datextx, datexty);
        //mana
        g.setColor(Color.BLUE);
        FontMetrics m6 = g.getFontMetrics(g.getFont());
        String mtext = "Mana: "+Player.mana;
        int mtextx = xframe - Rock.size - m6.stringWidth(mtext);
        int mtexty = 30;
        text.drawString(mtext, mtextx, mtexty);
        //enemies killed
        g.setColor(Color.LIGHT_GRAY);
        FontMetrics m7 = g.getFontMetrics(g.getFont());
        String etext = "Enemies Killed: " + enemiesKilled;
        int etextx = xframe - Rock.size - m7.stringWidth(etext);
        int etexty = yframe-m7.getHeight() * 2;
        text.drawString(etext, etextx, etexty);
        //pause screen
        g.setColor(Color.WHITE);
        if (paused.get()){
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String text1 = "PAUSED";
            int ptextx = (xframe - metrics.stringWidth(text1))/2;
            int ptexty = ((yframe - metrics.getHeight())/2)
                    + metrics.getAscent();
            text.drawString(text1, ptextx, ptexty);
        }
        //game over screen
        if (gameOver.get()){
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String text1 = "GAME OVER";
            String text2 = "==PRESS [r] TO RESTART==";
            String text3 = "==High Scores==";
            String text4 = ""+highScore;
            String text5 = ""+secondHigh;
            String text6 = ""+thirdHigh;
            int ptextx = (xframe - metrics.stringWidth(text1))/2;
            int ptextx2 = (xframe - metrics.stringWidth(text2))/2;
            int ptextx3 = (xframe - metrics.stringWidth(text3))/2;
            int ptextx4 = (xframe - metrics.stringWidth(text4))/2;
            int ptextx5 = (xframe - metrics.stringWidth(text5))/2;
            int ptextx6 = (xframe - metrics.stringWidth(text6))/2;
            int ptexty = (
                    (yframe - metrics.getHeight())/2) + metrics.getAscent();
            int ptexty2 = (
                    (yframe - metrics.getHeight())/2) + metrics.getAscent() * 2;
            int ptexty3 = (
                    (yframe - metrics.getHeight())/2) + metrics.getAscent() * 4;
            int ptexty4 = (
                    (yframe - metrics.getHeight())/2) + metrics.getAscent() * 5;
            int ptexty5 = (
                    (yframe - metrics.getHeight())/2) + metrics.getAscent() * 6;
            int ptexty6 = (
                    (yframe - metrics.getHeight())/2) + metrics.getAscent() * 7;
            text.drawString(text1, ptextx, ptexty);
            text.drawString(text2, ptextx2, ptexty2);
            text.drawString(text3, ptextx3, ptexty3);
            text.drawString(text4, ptextx4, ptexty4);
            text.drawString(text5, ptextx5, ptexty5);
            text.drawString(text6, ptextx6, ptexty6);
        }
        //store screen
        if (collisionDetect(Player.x, Player.y, Store.x, Store.y)) {        
            text.setFont(new Font("Courier New", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            String storeMenu = "==STORE==\n"
                    + "[1] Buy HP: "+Store.hpPrice+" Gold\n"
                    + "[2] Buy Mana: "+Store.manaPrice+" Gold\n"
                    + "[3] Buy +1 Minimum Damage: "+Store.minDamagePrice+
                    " Gold\n"
                    + "[4] Buy +1 Maximum Damage: "+Store.maxDamagePrice+
                    " Gold\n"
                    + "[5] Buy +1 Attack Magic: "+Store.attackMagicPrice+
                    " Gold\n"
                    + "[6] Buy +1 Defense Magic: "+Store.defenseMagicPrice+
                    " Gold\n"
                    + "[7] Buy a Bow: "+Store.bowPrice+" Gold\n"
                    + "[8] Buy +1 Arrows: "+Store.arrowPrice+" Gold\n";
            int xtextx = xframe/2;
            int ytexty = yframe/2 + text.getFontMetrics().getHeight();
            for (String line : storeMenu.split("\n")) {
                text.drawString(
                        line, 
                        xtextx, 
                        ytexty+=text.getFontMetrics().getHeight());
            }
        }
        //instructions screen
        String instructions = null;
        if (instructionDisplay) {
            g.setColor(Color.DARK_GRAY);
            text.setFont(new Font("Courier New", Font.BOLD, 14));
            text.fillRect(0, Rock.size * 3, xframe, yframe);
            g.setColor(Color.WHITE);
            instructions = "==INSTRUCTIONS==\n"
                    + "[wasd]  move (up, left, down, right)\n"
                    + "[ijkl]  shoot arrows (up, left, down, right)\n"
                    + "        requires you to buy a bow to use\n"
                    + "[1-8]   buy item (while in the store)\n"
                    + "[q]     toggle instructions\n"
                    + "        don't worry, the game is paused\n"
                    + "[n]     use attack magic\n"
                    + "        every enemy in a 3 block radius takes your max "
                    + "damage\n"
                    + "        requires 20 mana to use\n"
                    + "[m]     use defense magic\n"
                    + "        increases your health by (30*your_level)\n"
                    + "        requires 10 mana to use\n"
                    + "[esc]   pause\n"
                    + "[r]     restart game\n"
                    + "==ENTITIES==\n"
                    + "Red     enemy\n"
                    + "        attack by bumping into them\n"
                    + "        both will take a random amount damage based on "
                    + "level and enemy's health\n"
                    + "Blue    your player model\n"
                    + "Green   store\n"
                    + "        purchase items that increase your stats\n"
                    + "        enemies can't attack you while you're in the "
                    + "store\n"
                    + "Yellow  powerup which increases various stats\n"
                    + "        increases stats based on player level\n"
                    + "        M = mana, H = health, - = minimum attack, + = "
                    + "maximum attack, G = gold\n"
                    + "==GAMEPLAY==\n"
                    + "Navigate through the dungeon, kill all the enemies to get"
                    + " to the next level,\n"
                    + "get through as many levels as possible before you "
                    + "inevitably die.";
            int y = Rock.size * 3;
            for (String line : instructions.split("\n")) {
                text.drawString(line, 10, y+=text.getFontMetrics().getHeight());
            }
            String returntext = "==PRESS [q] TO RETURN TO THE GAME==";
            text.drawString(
                    returntext, 
                    (xframe - text.getFontMetrics().stringWidth(returntext))/2, 
                    y+=text.getFontMetrics().getHeight());
        }
        
        else if (!instructionDisplay) {
            text.setFont(new Font("Courier New", Font.BOLD, 16));
            instructions = "Press [q] to display instructions";
            long yinst = yframe - text.getFontMetrics().getHeight() * 2l;
            text.drawString(instructions, 10, yinst);
        }
    }
    
    public static void main(String[] args) 
            throws InterruptedException, IOException {
        startTime = System.currentTimeMillis();
        connectToDB();
        JFrame frame = new JFrame("Dungeon Game"); 
        
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            //KEY BINDINGS
            public void keyPressed(KeyEvent e) {
                //go find the key binding
                KeyBindings.bind(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        Game game = new Game();
        nextLevel(game, frame);        
        generateMap();        
        generateEnemies();
        double enemyFPS = 2;
        final int gameFPS = 60;


        //MAIN GAME LOOP
        Runnable runnable = () -> {
            int aiTimer = 0;
            while (true) {
                
                //handle game pausing
                if (paused.get()) {
                    synchronized(thread) {
                        try {
                            frame.repaint();
                            thread.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Game.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }
                }
                
                //animate stuff once per game loop
                animateArrowShot();
                
                //calculate enemies killed
                int deadEnemies = 0;
                for (int a = 0; a < ENEMIES; a++) { 
                    if (enemyList[a].getHP() <= 0) deadEnemies++;
                }
                enemiesKilled = deadEnemies;
                
                //if all enemies are killed, make a new map
                if (ENEMIES == deadEnemies) {
                    frame.remove(frame);
                    Game g = null;
                    try {
                        g = new Game();
                    } catch (IOException ex) {
                        Logger.getLogger(Game.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                    try {
                        nextLevel(g, frame);
                    } catch (IOException ex) {
                        Logger.getLogger(Game.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                    generateMap();                    
                    generateEnemies();
                }
                
                //move ai
                if (aiTimer == (int)((100/enemyFPS)) * (level)) {
                    aiTimer = 0;
                    initAI();
                }
                
                /* ==HANDLING COLLISIONS== */
                
                //if player collides with enemy, knock back player and calculate
                //  hp's/remove dead entities
                for (int a = 0; a < ENEMIES; a++){
                    if (collisionDetect(
                            Player.x,
                            Player.y,
                            enemyList[a].getX(),
                            enemyList[a].getY())){
                        Player.hp -= Math.round(
                                Math.random() * enemyList[a].getHP());
                        enemyList[a].setHP(enemyList[a].getHP()-(int)Math.round(
                                Player.minDamage + (
                                        Math.random() * Player.maxDamage)));
                        if (enemyList[a].getHP() <= 0) {
                            enemyList[a].setX(-enemyList[a].getSize());
                            enemyList[a].setY(-enemyList[a].getSize());
                            Player.hp += Math.ceil(Math.random() * (10 * level));
                            Player.gp += Math.ceil(Math.random() * level);
                        }
                        if (Player.hp <= 0) {
                            addScore(enemiesKilled);
                            highScore = getHighScore(1);
                            secondHigh = getHighScore(2);
                            thirdHigh = getHighScore(3);
                            gameOver.set(true);
                        }
                        Player.knockbackPlayer();
                    }
                }
                
                //if player collides with powerup, remove the powerup and give 
                //  the player superpowers
                for (int a = 0; a < POWERUPS; a++) {
                    if (collisionDetect(
                            Player.x, 
                            Player.y, 
                            powerupList[a].getX(), 
                            powerupList[a].getY())) {
                        powerupList[a].setX(-Powerup.size);
                        powerupList[a].setY(-Powerup.size);
                        switch(powerupList[a].getType()) {
                            case GOLD:
                                Player.gp += (int) Math.ceil(
                                        Math.random() * level * 10);
                                break;
                            case HEALTH:
                                Player.hp += (int) Math.ceil(
                                        Math.random() * level * 10);
                                break;
                            case MINATTACK:
                                Player.minDamage += (int) Math.ceil(
                                        Math.random() * level * 5);
                                if (Player.minDamage > Player.maxDamage){
                                    Player.maxDamage = Player.minDamage;
                                }
                                break;
                            case MAXATTACK:
                                Player.maxDamage += (int) Math.ceil(
                                        Math.random() * level * 5);
                                break;
                            case MANA:
                                Player.mana += (int) Math.ceil(
                                        Math.random() * level * 10);
                                break;
                        }
                    }
                }
                
                //if player collides with rock, knock them back
                for (int a = 0; a < ROCKS; a++){
                    if (collisionDetect(
                            Player.x, 
                            Player.y, 
                            rockList[a].getX(), 
                            rockList[a].getY())){
                        Player.knockbackPlayer();
                    }
                    
                    for (int b = 0; b < ENEMIES; b++){
                        //if enemy collides with rock, knock them back
                        if (collisionDetect(
                                enemyList[b].getX(), 
                                enemyList[b].getY(), 
                                rockList[a].getX(), 
                                rockList[a].getY())){
                            enemyList[b].knockbackEnemy();
                        }
                    }
                }
                
                for (int b = 0; b < ENEMIES; b++) {
                    //if enemy collides with enemy, knock an enemy back
                    for (int c = 0; c < ENEMIES; c++) {
                        if (collisionDetect(
                                enemyList[b].getX(), 
                                enemyList[b].getY(), 
                                enemyList[c].getX(), 
                                enemyList[c].getY()) && c != b){
                            enemyList[b].knockbackEnemy();
                        }
                    }

                    //enemy collides with powerup, knock enemy back
                    for (int d = 0; d < POWERUPS; d++) {
                        if (collisionDetect(
                                enemyList[b].getX(), 
                                enemyList[b].getY(), 
                                powerupList[d].getX(), 
                                powerupList[d].getY())){
                            enemyList[b].knockbackEnemy();
                        }
                    }
                    
                    //enemy collides with store, knock enemy back
                    if (collisionDetect(
                            enemyList[b].getX(), 
                            enemyList[b].getY(), 
                            store.x, 
                            store.y)) {
                       enemyList[b].knockbackEnemy();
                    }
                }
                frame.repaint();
                try {
                    thread.sleep(1000/gameFPS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
                aiTimer++; 
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }
}
