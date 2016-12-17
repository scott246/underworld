/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author Nathan
 */

public class Powerup {
    static int size = 20;
    int x = 0;
    int y = 0;
    Powerups type;
    String typeString;
    
    public int getSize() {
        return size;
    }
    public void setSize(int size1) {
        size = size1;
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x1) {
        x = x1;
    }
    
    public int getY() {
        return y;
    }
    public void setY(int y1) {
        y = y1;
    }
    
    public Powerups getType() {
        return type;
    }
    public String getTypeString() {
        return typeString;
    }
    public void setType(double temp){
        if (temp < .2 && temp >= 0) {
            type = Powerups.GOLD;
            typeString = "G";
        } else if (temp < .4 && temp >= .2) {
            type = Powerups.MANA;
            typeString = "M";
        } else if (temp < .6 && temp >= .4) {
            type = Powerups.MINATTACK;
            typeString = "-";
        } else if (temp < .8 && temp >= .6) {
            type = Powerups.MAXATTACK;
            typeString = "+";
        } else if (temp < 1 && temp >= .8) {
            type = Powerups.HEALTH;
            typeString = "H";
        }
    }
}
