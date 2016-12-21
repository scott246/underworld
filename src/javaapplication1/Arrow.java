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
    
}
