/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.xframe;
import static game.Game.yframe;
import static game.Game.ROCKS;

/**
 *
 * @author Nathan
 */
public class Player {
    static int size = 20;
    static int hp = 100;
    static int minDamage = 1;
    static int maxDamage = 7;
    static int x = Game.roundLocation((int)xframe/2, size);
    static int y = Game.roundLocation((int)yframe/2, size);
    static int lastDir = 0;
    static int gp = 0;
    static int mana = 0;
    static int attackMagic = 0;
    static int defenseMagic = 0;
    static boolean bow = false;
    static int arrows = 0;
    static boolean moving = false;
    
    public static void movePlayerLeft() {
        moving = true;
        boolean collision = false;
        for (int a = 0; a < ROCKS; a++){
            if (Game.collisionDetect(x-size, y, Game.rockList[a].getX(), Game.rockList[a].getY()))
                collision = true;
        }
        if (!collision) x -= size;
        moving = false;
    }
    public static void movePlayerRight() {
        moving = true;
        boolean collision = false;
        for (int a = 0; a < ROCKS; a++){
            if (Game.collisionDetect(x+size, y, Game.rockList[a].getX(), Game.rockList[a].getY()))
                collision = true;
        }
        if (!collision) x += size;
        moving = false;
    }
    public static void movePlayerUp() {
        moving = true;
        boolean collision = false;
        for (int a = 0; a < ROCKS; a++){
            if (Game.collisionDetect(x, y-size, Game.rockList[a].getX(), Game.rockList[a].getY()))
                collision = true;
        }
        if (!collision) y -= size;
        moving = false;
    }
    public static void movePlayerDown() {
        moving = true;
        boolean collision = false;
        for (int a = 0; a < ROCKS; a++){
            if (Game.collisionDetect(x, y+size, Game.rockList[a].getX(), Game.rockList[a].getY()))
                collision = true;
        }
        if (!collision) y += size;
        moving = false;
    }
    public static void resetPlayer() {
        size = 20;
        hp = 100;
        minDamage = 1;
        maxDamage = 7;
        x = Game.roundLocation((int)xframe/2, size);
        y = Game.roundLocation((int)yframe/2, size);
        lastDir = 0;
        gp = 0;
        mana = 0;
        attackMagic = 0;
        defenseMagic = 0;
        bow = false;
        arrows = 0;
    }
    
    public static void knockbackPlayer() {
        switch(lastDir){
            case 1: //up
                y += size;
                lastDir = 3;
                break;
            case 2: //left
                x += size;
                lastDir = 4;
                break;
            case 3: //down
                y -= size;
                lastDir = 1;
                break;
            case 4: //right
                x -= size;
                lastDir = 2;
                break;
            default:
                break;
        }
    }
}
