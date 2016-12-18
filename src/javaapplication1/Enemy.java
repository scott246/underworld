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
public class Enemy {
    int maxHP = 30;
    int hp = 100;
    int size = 20;
    int x = 0;
    int y = 0;
    int lastDir = 0;
    
    public int getHP() {
        return hp;
    }
    public void setHP(int hp1) {
        hp = hp1;
    }
    public int getMaxHP() {
        return maxHP;
    }
    public int getSize() {
        return size;
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
    public int getLastDir() {
        return lastDir;
    }
    public void setLastDir(int lastDir1) {
        lastDir = lastDir1;
    }

}
