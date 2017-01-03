/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.xframe;
import static game.Game.yframe;
import static game.Game.p;
import static game.Game.powerupList;
import static game.Game.enemyList;
import static game.Game.rockList;
import static game.Game.ENEMIES;
import static game.Game.POWERUPS;
import static game.Game.ROCKS;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Nathan
 */
public class Paint {
    public static void paint(Graphics g) throws IOException, URISyntaxException {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        //draw background
        g.setColor(Color.GRAY);
        graphics.fillRect(0, 0, xframe, yframe);
        
        //draw enemies
        for(int a = 0; a < ENEMIES; a++){
                g.setColor(Color.RED);
                graphics.fillRect(
                        enemyList[a].getX(),
                        enemyList[a].getY(),
                        enemyList[a].getSize(),
                        enemyList[a].getSize());
                g.setColor(Color.BLACK);
                graphics.drawString(
                        Integer.toString(enemyList[a].getHP()), 
                        enemyList[a].getX(), 
                        enemyList[a].getY()+enemyList[a].getSize()/2);
        }
        
        //draw powerups
        for(int a = 0; a < POWERUPS; a++) {
            g.setColor(Color.YELLOW);
            graphics.fillRect(
                    powerupList[a].getX(), 
                    powerupList[a].getY(), 
                    powerupList[a].getSize(), 
                    powerupList[a].getSize());
            g.setColor(Color.BLACK);
            graphics.drawString(
                    powerupList[a].getTypeString(), 
                    powerupList[a].getX(), 
                    powerupList[a].getY()+Powerup.size/2);
        }
        
        //draw store
        g.setColor(Color.GREEN);
        graphics.fillRect(Store.x, Store.y, Store.size, Store.size);
        
        //draw arrow
        g.setColor(Color.BLACK);
        graphics.fillOval(Arrow.x + 5, Arrow.y + 5, Arrow.size, Arrow.size);
        
        //draw attack magic
        if (Magic.attackMagicExists){
            g.setColor(Color.RED);
            for (int x : Magic.x){
                for (int y : Magic.y){
                    graphics.fillOval(x + 5, y + 5, Magic.size, Magic.size);
                }
            }
        }
        
        //draw defense magic
        if (Magic.defenseMagicExists){
            g.setColor(Color.BLUE);
            for (int x : Magic.x){
                for (int y : Magic.y){
                    graphics.fillOval(x + 5, y + 5, Magic.size, Magic.size);
                }
            }
        }
        
        //draw player
        g.setColor(Color.BLUE);
        graphics.fillOval(p.x, p.y, p.size, p.size);
        g.setColor(Color.LIGHT_GRAY);
        graphics.drawString(
                Integer.toString(p.hp), p.x, p.y+p.size/2);
        
        //draw rocks
        for(int b = 0; b < ROCKS; b++){
            g.setColor(Color.BLACK);
            graphics.fillRect(
                    rockList[b].getX(), 
                    rockList[b].getY(), 
                    rockList[b].getSize(), 
                    rockList[b].getSize());
        }
        
        //draw fog of war
        if (Game.fog){
            Area fog = new Area(new Rectangle(0, 0, xframe, yframe));
            //Area center = new Area(new Rectangle(p.x - 20, p.y - 20, p.size + 40, p.size + 40));
            Area middle1 = new Area(new Rectangle(p.x - 40, p.y - 60, p.size + 80, p.size + 120));
            Area middle2 = new Area(new Rectangle(p.x - 60, p.y - 40, p.size + 120, p.size + 80));
            Area light1 = new Area(new Rectangle(p.x - 20, p.y - 40, p.size + 40, p.size + 80));
            Area light2 = new Area(new Rectangle(p.x - 40, p.y - 20, p.size + 80, p.size + 40));
            //outer.subtract(new Area(inner1));
            fog.subtract(middle1);
            fog.subtract(middle2);
            light1.subtract(new Area(light2));
            middle1.subtract(middle2);
            middle1.subtract(light1);
            middle2.subtract(light1);
            middle1.subtract(light2);
            middle2.subtract(light2);

            graphics.setColor(new Color(0,0,0,255));
            graphics.fill(fog);
            graphics.setColor(new Color(0,0,0,128));
            graphics.fill(middle1);
            graphics.fill(middle2);
            graphics.setColor(new Color(0,0,0,0));
            graphics.fill(light1);
            graphics.fill(light2);
        }
        
        
        //draw all text
        
        //level
        graphics.setFont(new Font("Courier New", Font.BOLD, 16));
        if (!Game.instructionDisplay){
            g.setColor(Color.WHITE);
            FontMetrics m = g.getFontMetrics(g.getFont());
            String ltext = "==Level " +Game.level+"==";
            graphics.drawString(ltext, (xframe - m.stringWidth(ltext))/2, 30);
        }
        if (Game.instructionDisplay){
            g.setColor(Color.WHITE);
            FontMetrics m = g.getFontMetrics(g.getFont());
            String ltext = "==PRESS [q] TO RETURN TO GAME==";
            graphics.drawString(ltext, (xframe - m.stringWidth(ltext))/2, 45);
        }
      
        //HP
        g.setColor(Color.RED);
        graphics.fillRect(10, 10, p.hp*2, 20);
        g.setColor(Color.DARK_GRAY);
        graphics.drawRect(10, 10, 200, 20);
        g.setColor(Color.WHITE);
        String htext = p.hp+" HP";
        graphics.drawString(htext, 15, 25);
       
        //mana
        g.setColor(Color.BLUE);
        graphics.fillRect(10, 30, p.mana*2, 20);
        g.setColor(Color.DARK_GRAY);
        graphics.drawRect(10, 30, 200, 20);
        g.setColor(Color.WHITE);
        String mtext = p.mana+" Mana";
        graphics.drawString(mtext, 15, 45);
        
        //damage
        g.setColor(Color.WHITE);
        String datext = p.minDamage+"-"+p.maxDamage+" Damage";
        graphics.drawString(datext, 10, 80);        
        
        //gold
        g.setColor(Color.YELLOW);
        graphics.drawString(p.gp + " Gold", 10, 100);
        
        //arrows
        g.setColor(Color.WHITE);
        String artext = p.arrows + " Arrows";
        graphics.drawString(artext, 10, 120);
        
        //logo
        BufferedImage underworld = ImageIO.read(new File(Paint.class.getResource("/images/underworld.png").toURI()));
        //g.drawImage(underworld, 10, yframe/2, null);
        g.drawImage(underworld, 10, yframe/2 - 50, 100, 100, null);

        //alive time
        g.setColor(Color.WHITE);
        String aliveTime = Integer.toString(
                (int)(Game.getTimeAlive()/1000));
        graphics.drawString(aliveTime, 10, yframe - graphics.getFontMetrics().getHeight() * 3);

        //attack magic
        g.setColor(Color.WHITE);
        String atext = p.attackMagic + " Attack";
        graphics.drawString(atext, 10, yframe - graphics.getFontMetrics().getHeight() * 5);
        
        //defense magic
        g.setColor(Color.WHITE);
        String dtext = p.defenseMagic + " Defense";
        graphics.drawString(dtext, 10, yframe - graphics.getFontMetrics().getHeight() * 4);

        //enemies killed
        g.setColor(Color.LIGHT_GRAY);
        FontMetrics m7 = g.getFontMetrics(g.getFont());
        String etext = Game.enemiesKilled + " Enemies Killed";
        int etextx = xframe - Rock.size - m7.stringWidth(etext);
        int etexty = yframe-m7.getHeight() * 2;
        graphics.drawString(etext, etextx, etexty);
        
        //enemies remaining
        g.setColor(Color.RED);
        FontMetrics m8 = g.getFontMetrics(g.getFont());
        String qtext = Game.enemiesRemaining + " Enemies Remaining";
        int qtextx = xframe - Rock.size - m8.stringWidth(qtext);
        int qtexty = m8.getHeight() * 2;
        graphics.drawString(qtext, qtextx, qtexty);
        
        
        //screens
        
        //pause screen
        g.setColor(Color.WHITE);
        if (Game.paused.get()){
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String text1 = "PAUSED";
            int ptextx = (xframe - metrics.stringWidth(text1))/2;
            int ptexty = ((yframe - metrics.getHeight())/2)
                    + metrics.getAscent();
            graphics.drawString(text1, ptextx, ptexty);
        }
        //game over screen
        if (Game.gameOver.get()){
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String text1 = "GAME OVER\n"
                    + "==PRESS [r] TO RESTART==\n"
                    + "\n"
                    + "==High Scores==\n"
                    + ""+Game.highScore+"\n"
                    + ""+Game.secondHigh+"\n"
                    + ""+Game.thirdHigh+"\n";
            int ptexty = yframe/3 + graphics.getFontMetrics().getHeight();
            for (String line : text1.split("\n")) {
                graphics.drawString(
                        line, 
                        (xframe - metrics.stringWidth(line))/2, 
                        ptexty+=(graphics.getFontMetrics().getHeight()));
            }
        }
        //store screen
        if (Game.collisionDetect(p.x, p.y, Store.x, Store.y)) {        
            graphics.setFont(new Font("Courier New", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            String storeMenu = "==STORE==\n"
                    + "[1] Buy HP: "+Store.hpPrice+" Gold\n"
                    + "[2] Buy Mana: "+Store.manaPrice+" Gold\n"
                    + "[3] Buy Minimum Damage: "+Store.minDamagePrice+
                    " Gold\n"
                    + "[4] Buy Maximum Damage: "+Store.maxDamagePrice+
                    " Gold\n"
                    + "[5] Buy +1 Attack Magic: "+Store.attackMagicPrice+
                    " Gold\n"
                    + "[6] Buy +1 Defense Magic: "+Store.defenseMagicPrice+
                    " Gold\n"
                    + "[7] Buy a Bow: "+Store.bowPrice+" Gold\n"
                    + "[8] Buy +1 Arrows: "+Store.arrowPrice+" Gold\n";
            int xtextx = xframe/2;
            int ytexty = yframe/2 + graphics.getFontMetrics().getHeight();
            for (String line : storeMenu.split("\n")) {
                graphics.drawString(
                        line, 
                        xtextx, 
                        ytexty+=graphics.getFontMetrics().getHeight());
            }
        }
        //information display       
        graphics.setFont(new Font("Courier New", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        int xtextx = p.x+p.size;
        int ytexty = p.y+p.size;
        graphics.drawString(
                Game.information, 
                xtextx, 
                ytexty);
        //instructions screen
        String instructions = null;
        if (Game.instructionDisplay) {
            g.setColor(Color.BLACK);
            if (xframe == 640 && yframe == 480)
                graphics.setFont(new Font("Courier New", Font.BOLD, 10));
            else
                graphics.setFont(new Font("Courier New", Font.BOLD, 14));
            graphics.fillRect(0, Rock.size * 3, xframe, yframe);
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
                graphics.drawString(line, 10, y+=graphics.getFontMetrics().getHeight());
            }
        }
        
        else if (!Game.instructionDisplay) {
            graphics.setFont(new Font("Courier New", Font.BOLD, 16));
            instructions = "Press [q] to display instructions";
            long yinst = yframe - graphics.getFontMetrics().getHeight() * 2l;
            graphics.drawString(instructions, 10, yinst);
        }
    }
}
