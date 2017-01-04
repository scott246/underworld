/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.Stack;

/**
 *
 * @author Nathan
 */
public class Trap {
    static final int MAXTRAPS = 10;
    static int[] x = new int[MAXTRAPS];
    static int[] y = new int[MAXTRAPS];
    static int size = 10;
    
    static void placeTrap() {
        boolean trapPlaced = false;
        for(int i = 0; i < MAXTRAPS; i++) {
            if(x[i] == 0) {
                trapPlaced = true;
                x[i] = Player.x;
                y[i] = Player.y;
                Player.traps--;
                break;
            }
        }
        if (!trapPlaced) Error.displayError(Errors.HITRAPS);
    }

    static void springTrap(Enemy e) {
        //attack enemy
        e.setHP(e.getHP() - Player.maxDamage);
        //delete trap
        for (int a = 0; a < MAXTRAPS; a++){
            if (e.x == x[a] && e.y == y[a]){
                x[a] = 0;
                y[a] = 0;
            }
        }
    }
    
    static void resetTraps() {
        for (int a = 0; a < MAXTRAPS; a++){
            x[a] = 0;
            y[a] = 0;
        }
    }
}
