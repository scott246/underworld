/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.level;

/**
 *
 * @author Nathan
 */
public class Store {
    static int manaPrice = 1 * (level + 1);
    static int hpPrice = 1 * (level + 1);
    static int minDamagePrice = 25 * (level + 1);
    static int maxDamagePrice = 20 * (level + 1);
    static int attackMagicPrice = 20 * (level + 1);
    static int defenseMagicPrice = 20 * (level + 1);
    static int bowPrice = 50;
    static int arrowPrice = 2 * (level + 1);
    static int x = Player.x;
    static int y = Player.y;
    static int size = 20;
    
    void setX(int x1){
        x = x1;
    }
    void setY(int y1){
        y = y1;
    }
}
