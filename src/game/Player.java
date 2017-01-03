/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.xframe;
import static game.Game.yframe;

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
    
    public static void movePlayerLeft() {
        if (x > 0) x -= size;
    }
    public static void movePlayerRight() {
        if (x < xframe - size * 2) x += size;
    }
    public static void movePlayerUp() {
        if (y > 0) y -= size;
    }
    public static void movePlayerDown() {
        if (y < yframe - size * 3) y += size;
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
