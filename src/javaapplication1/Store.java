/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import static javaapplication1.Game.level;

/**
 *
 * @author Nathan
 */
public class Store {
    static int manaPrice = 1 * (level + 1);
    static int hpPrice = 1 * (level + 1);
    static int minDamagePrice = 12 * (level + 1);
    static int maxDamagePrice = 10 * (level + 1);
    static int attackMagicPrice = 20 * (level + 1);
    static int defenseMagicPrice = 20 * (level + 1);
    static int bowPrice = 50;
    static int arrowPrice = 2 * (level + 1);
    static int x = 0;
    static int y = 0;
    static int size = 20;
    
    void setX(int x1){
        x = x1;
    }
    void setY(int y1){
        y = y1;
    }
}
