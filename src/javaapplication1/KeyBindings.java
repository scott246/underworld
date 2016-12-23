/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.awt.event.KeyEvent;
import static javaapplication1.Game.gameOver;
import static javaapplication1.Game.instructionDisplay;
import static javaapplication1.Game.paused;

/**
 * All the key bindings.
 * @author Nathan
 */
public class KeyBindings {

    /**
     * Gets the key bindings.
     * @param key
     */
    public static void bind(int key){
        switch (key) {
                    //w = move up
                    case KeyEvent.VK_W:
                        if (!paused.get() && !gameOver.get()) {
                            Player.lastDir = 1;
                            Player.movePlayerUp();
                        }
                        break;
                    //a = move left
                    case KeyEvent.VK_A:
                        if (!paused.get() && !gameOver.get()) {
                            Player.lastDir = 2;
                            Player.movePlayerLeft();
                        }
                        break;
                    //s = move down
                    case KeyEvent.VK_S:
                        if (!paused.get() && !gameOver.get()) {
                            Player.lastDir = 3;
                            Player.movePlayerDown();
                        }
                        break;
                    //d = move right
                    case KeyEvent.VK_D:
                        if (!paused.get() && !gameOver.get()) {
                            Player.lastDir = 4;
                            Player.movePlayerRight();
                        }
                        break;
                    //i = display instructions
                    case KeyEvent.VK_Q:
                        if (!paused.get()){
                            paused.set(true);
                        }
                        else {
                            paused.set(false);
                            synchronized(Game.thread){
                               Game.thread.notify(); 
                            }
                        }
                        if (!instructionDisplay) instructionDisplay = true;
                        else if (instructionDisplay) instructionDisplay = false;
                        break;
                    //r = restart game
                    case KeyEvent.VK_R:
                        Game.level = 1;
                        Game.generateMap();                        
                        Game.generateEnemies();
                        gameOver.set(false);
                        Game.startTime = System.currentTimeMillis();
                        Game.pauseTime = 0;
                        Player.resetPlayer();
                        
                    //n = use attack magic
                    case KeyEvent.VK_N:
                        if (Player.mana >= 20 && Player.attackMagic >= 1) {
                            if (!Magic.attackMagicExists){
                                Magic.attackMagicExists = true;
                            }
                            Enemy[] enemies = Game.getEnemiesIn3BlockRadius();
                            for (Enemy enemy : enemies){
                                if (enemy != null) {
                                    enemy.setHP(enemy.getHP() - Player.maxDamage);
                                    if (enemy.getHP() <= 0) {
                                        enemy.setX(-enemy.getSize());
                                        enemy.setY(-enemy.getSize());
                                        Player.hp += Math.ceil(Math.random() * (10 * Game.level));
                                        Player.gp += Math.ceil(Math.random() * Game.level);
                                    }
                                }
                            }
                            Player.mana -= 20;
                            Player.attackMagic -= 1;
                        }
                        else if (Player.mana < 20 && Player.attackMagic >= 1) {
                            Error.activeError = Errors.NOMANA;
                            Error.errors = true;
                        }
                        else if (Player.attackMagic < 1) {
                            Error.activeError = Errors.NOMAGIC;
                            Error.errors = true;
                        }
                        break;
                    //m = use defense magic
                    case KeyEvent.VK_M:
                        if (Player.mana >= 10 && Player.defenseMagic >= 1) {
                            if (!Magic.defenseMagicExists){
                                Magic.defenseMagicExists = true;
                            }
                            Player.hp += (30 * Game.level);
                            Player.mana -= 10;
                            Player.defenseMagic -= 1;
                        }
                        else if (Player.mana < 10 && Player.defenseMagic >= 1) {
                            Error.activeError = Errors.NOMANA;
                            Error.errors = true;
                        }
                        else if (Player.defenseMagic < 1) {
                            Error.activeError = Errors.NOMAGIC;
                            Error.errors = true;
                        }
                        break;
                    //i = shoot arrow up
                    case KeyEvent.VK_I:
                        if (Player.bow && Player.arrows >= 1) {
                            if (!Arrow.exists){
                                Arrow.dir = 1; Arrow.exists = true;
                                Arrow.x = Player.x; Arrow.y = Player.y;
                            }
                            Enemy enemy = Game.getEnemyInDirection(1);
                            if (enemy != null) {
                                enemy.setHP(enemy.getHP()-(int)Math.round(Player.minDamage + (Math.random() * Player.maxDamage)));
                                if (enemy.getHP() <= 0) {
                                    enemy.setX(-enemy.getSize());
                                    enemy.setY(-enemy.getSize());
                                    Player.hp += Math.ceil(Math.random() * (10 * Game.level));
                                    Player.gp += Math.ceil(Math.random() * Game.level);
                                }
                            }
                            Player.arrows--;
                        }
                        else if (Player.bow && Player.arrows <= 0) {
                            Error.activeError = Errors.NOARROWS;
                            Error.errors = true;
                        }
                        else if (!Player.bow) {
                            Error.activeError = Errors.NOBOW;
                            Error.errors = true;
                        }
                        break;
                    //j = shoot arrow left
                    case KeyEvent.VK_J:
                        if (Player.bow && Player.arrows >= 1) {
                            if (!Arrow.exists){
                                Arrow.dir = 2; Arrow.exists = true;
                                Arrow.x = Player.x; Arrow.y = Player.y;
                            }
                            Enemy enemy = Game.getEnemyInDirection(2);
                            if (enemy != null) {
                                enemy.setHP(enemy.getHP()-(int)Math.round(Player.minDamage + (Math.random() * Player.maxDamage)));
                                if (enemy.getHP() <= 0) {
                                    enemy.setX(-enemy.getSize());
                                    enemy.setY(-enemy.getSize());
                                    Player.hp += Math.ceil(Math.random() * (10 * Game.level));
                                    Player.gp += Math.ceil(Math.random() * Game.level);
                                }
                            }
                            Player.arrows--;
                        }
                        else if (Player.bow && Player.arrows <= 0) {
                            Error.activeError = Errors.NOARROWS;
                            Error.errors = true;
                        }
                        else if (!Player.bow) {
                            Error.activeError = Errors.NOBOW;
                            Error.errors = true;
                        }
                        break;
                    //k = shoot arrow down
                    case KeyEvent.VK_K:
                        if (Player.bow && Player.arrows >= 1) {
                            if (!Arrow.exists){
                                Arrow.dir = 3; Arrow.exists = true;
                                Arrow.x = Player.x; Arrow.y = Player.y;
                            }
                            Enemy enemy = Game.getEnemyInDirection(3);
                            if (enemy != null) {
                                enemy.setHP(enemy.getHP()-(int)Math.round(Player.minDamage + (Math.random() * Player.maxDamage)));
                                if (enemy.getHP() <= 0) {
                                    enemy.setX(-enemy.getSize());
                                    enemy.setY(-enemy.getSize());
                                    Player.hp += Math.ceil(Math.random() * (10 * Game.level));
                                    Player.gp += Math.ceil(Math.random() * Game.level);
                                }
                            }
                            Player.arrows--;
                        }
                        else if (Player.bow && Player.arrows <= 0) {
                            Error.activeError = Errors.NOARROWS;
                            Error.errors = true;
                        }
                        else if (!Player.bow) {
                            Error.activeError = Errors.NOBOW;
                            Error.errors = true;
                        }
                        break;
                    //l = shoot arrow right
                    case KeyEvent.VK_L:
                        if (Player.bow && Player.arrows >= 1) {
                            if (!Arrow.exists){
                                Arrow.dir = 4; Arrow.exists = true;
                                Arrow.x = Player.x; Arrow.y = Player.y;
                            }
                            Enemy enemy = Game.getEnemyInDirection(4);
                            if (enemy != null) {
                                enemy.setHP(enemy.getHP()-(int)Math.round(Player.minDamage + (Math.random() * Player.maxDamage)));
                                if (enemy.getHP() <= 0) {
                                    enemy.setX(-enemy.getSize());
                                    enemy.setY(-enemy.getSize());
                                    Player.hp += Math.ceil(Math.random() * (10 * Game.level));
                                    Player.gp += Math.ceil(Math.random() * Game.level);
                                }
                            }
                            Player.arrows--;
                        }
                        else if (Player.bow && Player.arrows <= 0) {
                            Error.activeError = Errors.NOARROWS;
                            Error.errors = true;
                        }
                        else if (!Player.bow) {
                            Error.activeError = Errors.NOBOW;
                            Error.errors = true;
                        }
                        break;
                    //buy 1 hp if in store    
                    case KeyEvent.VK_1: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.hpPrice) {
                                Player.gp -= Store.hpPrice;
                                Player.hp += (2*Game.level);
                            }
                            else if (Player.gp < Store.hpPrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //buy 1 mana if in store    
                    case KeyEvent.VK_2: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.manaPrice) {
                                Player.gp -= Store.manaPrice;
                                Player.mana += (2*Game.level);
                            }
                            else if (Player.gp < Store.manaPrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //buy 1 mindamage if in store    
                    case KeyEvent.VK_3: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.minDamagePrice) {
                                Player.gp -= Store.minDamagePrice;
                                if (Player.minDamage++ > Player.maxDamage) Player.maxDamage++;
                            }
                            else if (Player.gp < Store.minDamagePrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //buy 1 maxdamage if in store    
                    case KeyEvent.VK_4: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.maxDamagePrice) {
                                Player.gp -= Store.maxDamagePrice;
                                Player.maxDamage += 1;
                            }
                            else if (Player.gp < Store.maxDamagePrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //buy 1 attackmagic if in store    
                    case KeyEvent.VK_5: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.attackMagicPrice) {
                                Player.gp -= Store.attackMagicPrice;
                                Player.attackMagic += 1;
                            }
                            else if (Player.gp < Store.attackMagicPrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //buy 1 defensemagic if in store    
                    case KeyEvent.VK_6: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.defenseMagicPrice) {
                                Player.gp -= Store.defenseMagicPrice;
                                Player.defenseMagic += 1;
                            }
                            else if (Player.gp < Store.defenseMagicPrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break; 
                    //buy bow if in store
                    case KeyEvent.VK_7:
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.bowPrice) {
                                Player.gp -= Store.bowPrice;
                                Player.bow = true;
                            }
                            else if (Player.gp < Store.bowPrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //buy 1 arrow if in store    
                    case KeyEvent.VK_8: 
                        if (Game.collisionDetect(Player.x, Player.y, Store.x, Store.y)) {
                            if (Player.gp >= Store.arrowPrice) {
                                Player.gp -= Store.arrowPrice;
                                Player.arrows += 1;
                            }
                            else if (Player.gp < Store.arrowPrice){
                                Error.activeError = Errors.NOGOLD;
                                Error.errors = true;
                            }
                        }
                        break;
                    //esc = pause
                    case KeyEvent.VK_ESCAPE:
                        if (!paused.get()){
                            Game.startPauseTime = System.currentTimeMillis();
                            paused.set(true);
                        }
                        else {
                            paused.set(false);
                            Game.pauseTime += System.currentTimeMillis() - Game.startPauseTime;
                            synchronized(Game.thread){
                               Game.thread.notify(); 
                            }
                        }
                    default:
                        break;
                }
        
        
    }
}
