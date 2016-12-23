/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.ROCKS;
import static game.Game.ENEMIES;

/**
 *
 * @author Nathan
 */
public class Arrow {
    static int dir = 0;
    static boolean exists;
    static int speed = 10;
    static int size = 10;
    static int x = -size;
    static int y = -size;
    
    int getX(){
        return x;
    }
    void setX(int x1){
        x = x1;
    }
    
    int getY(){
        return y;
    }
    void setY(int y1){
        y = y1;
    }
    
    int getDir(){
        return dir;
    }
    void setDir(int d1){
        dir = d1;
    }
    
    /**
     * Allows a basic arrow shooting animation by having the arrow move 
     * according to its speed variable once every game loop iteration.
     */
    public static void animateArrowShot(){
        switch(dir){
            case 1: //up
                y -= speed; break;
            case 2: //left
                x -= speed; break;
            case 3: //down
                y += speed; break;
            case 4: //right
                x += speed; break;
        }
        
        //once the arrow hits a rock, the arrow is gone
        for (int a = 0; a < ROCKS; a++){
            if (Game.rockList[a].getX() == x &&
                Game.rockList[a].getY() == y) {
                exists = false;
                x = -size;
                y = -size;
            }
        }
        
        //once the arrow hits an enemy, the arrow is gone
        for (int a = 0; a < ENEMIES; a++){
            if (Game.enemyList[a].getX() == x &&
                Game.enemyList[a].getY() == y) {
                exists = false;
                x = -size;
                y = -size;
            }
        }
    }
}
