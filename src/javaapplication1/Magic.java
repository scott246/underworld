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
public class Magic {
    static int phase = 0;
    static int[] x = new int[7];
    static int[] y = new int[7];
    static boolean attackMagicExists = false;
    static boolean defenseMagicExists = false;
    static int size = 10;
    static int phaseDelay = 5;
    
    public static void animateAttackMagic(){
        switch(phase){
            case 1: //on player
                //covers a 1x1 area
                x[0] = Player.x;
                y[0] = Player.y;
                break;
            case 2: //closest; 1 block from player
                //covers a 3x3 area
                for (int a = 0; a < 3; a++){
                    switch(a){
                        case 0:
                            x[a] = Player.x + size * 2; break;
                        case 1:
                            x[a] = Player.x; break;
                        case 2:
                            x[a] = Player.x - size * 2; break;
                    }
                }
                for (int b = 0; b < 3; b++){
                    switch(b){
                        case 0:
                            y[b] = Player.y + size * 2; break;
                        case 1:
                            y[b] = Player.y; break;
                        case 2:
                            y[b] = Player.y - size * 2; break;
                    }
                }
                break;
            case 3: //2 blocks away from player
                //covers a 5x5 area
                for (int a = 0; a < 5; a++){
                    switch(a){
                        case 0:
                            x[a] = Player.x + size * 4; break;
                        case 1:
                            x[a] = Player.x + size * 2; break;
                        case 2:
                            x[a] = Player.x; break;
                        case 3:
                            x[a] = Player.x - size * 2; break;
                        case 4:
                            x[a] = Player.x - size * 4; break;
                    }
                }
                for (int b = 0; b < 5; b++){
                    switch(b){
                        case 0:
                            y[b] = Player.y + size * 4; break;
                        case 1:
                            y[b] = Player.y + size * 2; break;
                        case 2:
                            y[b] = Player.y; break;
                        case 3:
                            y[b] = Player.y - size * 2; break;
                        case 4:
                            y[b] = Player.y - size * 4; break;
                    }
                }
                break;
            case 4: //furthest away; 3 blocks from player
                //covers a 7x7 area
                for (int a = 0; a < 7; a++){
                    switch(a){
                        case 0:
                            x[a] = Player.x + size * 6; break;
                        case 1:
                            x[a] = Player.x + size * 4; break;
                        case 2:
                            x[a] = Player.x + size * 2; break;
                        case 3:
                            x[a] = Player.x; break;
                        case 4:
                            x[a] = Player.x - size * 2; break;
                        case 5:
                            x[a] = Player.x - size * 4; break;
                        case 6:
                            x[a] = Player.x - size * 6; break;
                    }
                }
                for (int b = 0; b < 7; b++){
                    switch(b){
                        case 0:
                            y[b] = Player.y + size * 6; break;
                        case 1:
                            y[b] = Player.y + size * 4; break;
                        case 2:
                            y[b] = Player.y + size * 2; break;
                        case 3:
                            y[b] = Player.y; break;
                        case 4:
                            y[b] = Player.y - size * 2; break;
                        case 5:
                            y[b] = Player.y - size * 4; break;
                        case 6:
                            y[b] = Player.y - size * 6; break;
                    }
                }
                break;
            case 5: //last phase rids attack magic
                attackMagicExists = false;
                for (int a = 0; a < 7; a++){
                    for (int b = 0; b < 7; b++){
                        x[a] = -size;
                        y[b] = -size;
                    }
                }
                break;
        }
        if (phaseDelay == 0) {
            phase++;
            phaseDelay = 5;
        }
        else phaseDelay--;
    }
    
    public static void animateDefenseMagic(){
        switch(phase){
            case 1: //on player
                //covers a 1x1 area
                for (int a = 0; a < 1; a++){
                    for (int b = 0; b < 1; b++){
                        x[a] = Player.x; y[a] = Player.y;
                    }
                }
                break;
            case 2: //closest; 1 block from player
                //covers a 3x3 area
                for (int a = 0; a < 3; a++){
                    switch(a){
                        case 0:
                            x[a] = Player.x + size * 2; break;
                        case 1:
                            x[a] = Player.x; break;
                        case 2:
                            x[a] = Player.x - size * 2; break;
                    }
                }
                for (int b = 0; b < 3; b++){
                    switch(b){
                        case 0:
                            y[b] = Player.y + size * 2; break;
                        case 1:
                            y[b] = Player.y; break;
                        case 2:
                            y[b] = Player.y - size * 2; break;
                    }
                }
                break;
            case 3: //2 blocks away from player
                //covers a 5x5 area
                for (int a = 0; a < 5; a++){
                    switch(a){
                        case 0:
                            x[a] = Player.x + size * 4; break;
                        case 1:
                            x[a] = Player.x + size * 2; break;
                        case 2:
                            x[a] = Player.x; break;
                        case 3:
                            x[a] = Player.x - size * 2; break;
                        case 4:
                            x[a] = Player.x - size * 4; break;
                    }
                }
                for (int b = 0; b < 5; b++){
                    switch(b){
                        case 0:
                            y[b] = Player.y + size * 4; break;
                        case 1:
                            y[b] = Player.y + size * 2; break;
                        case 2:
                            y[b] = Player.y; break;
                        case 3:
                            y[b] = Player.y - size * 2; break;
                        case 4:
                            y[b] = Player.y - size * 4; break;
                    }
                }
                break;
            case 4: //furthest away; 3 blocks from player
                //covers a 7x7 area
                for (int a = 0; a < 7; a++){
                    switch(a){
                        case 0:
                            x[a] = Player.x + size * 6; break;
                        case 1:
                            x[a] = Player.x + size * 4; break;
                        case 2:
                            x[a] = Player.x + size * 2; break;
                        case 3:
                            x[a] = Player.x; break;
                        case 4:
                            x[a] = Player.x - size * 2; break;
                        case 5:
                            x[a] = Player.x - size * 4; break;
                        case 6:
                            x[a] = Player.x - size * 6; break;
                    }
                }
                for (int b = 0; b < 7; b++){
                    switch(b){
                        case 0:
                            y[b] = Player.y + size * 6; break;
                        case 1:
                            y[b] = Player.y + size * 4; break;
                        case 2:
                            y[b] = Player.y + size * 2; break;
                        case 3:
                            y[b] = Player.y; break;
                        case 4:
                            y[b] = Player.y - size * 2; break;
                        case 5:
                            y[b] = Player.y - size * 4; break;
                        case 6:
                            y[b] = Player.y - size * 6; break;
                    }
                }
                break;
            case 5: //last phase rids defense magic
                defenseMagicExists = false;
                for (int a = 0; a < 7; a++){
                    for (int b = 0; b < 7; b++){
                        x[a] = -size;
                        y[b] = -size;
                    }
                }
                break;
        }
        if (phaseDelay == 0) {
            phase++;
            phaseDelay = 5;
        }
        else phaseDelay--;
    }
}
