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
    int manaPrice = 10 * level;
    int hpPrice = 1 * level;
    int minDamagePrice = 25 * level;
    int maxDamagePrice = 20 * level;
    int attackMagicPrice = 30 * level;
    int defenseMagicPrice = 30 * level;
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
