/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import static javaapplication1.Game.xframe;
import static javaapplication1.Game.yframe;

/**
 *
 * @author Nathan
 */
public class Player {
    static int size = 20;
    static int hp = 100;
    static int minDamage = 1;
    static int maxDamage = 2;
    static int x = 400;
    static int y = 300;
    static int lastDir = 0;
    
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
}
