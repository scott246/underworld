/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Nathan
 */
public class Error {
    static boolean errors = false;
    static int errorDelay = 60;
    static Errors activeError = Errors.NOERROR;
    
    public static void displayError(Errors e) {
        switch(e){
            case NOMANA:
                errors = true;
                activeError = Errors.NOMANA;
                Game.information = "Not enough mana";
                break;
            case NOBOW:
                errors = true;
                activeError = Errors.NOBOW;
                Game.information = "Need a bow to shoot arrows";
                break;
            case NOARROWS:
                errors = true;
                activeError = Errors.NOARROWS;
                Game.information = "You don't have any arrows";
                break;
            case NOGOLD:
                errors = true;
                activeError = Errors.NOGOLD;
                Game.information = "Not enough gold";
                break;
            case NOMAGIC:
                errors = true;
                activeError = Errors.NOMAGIC;
                Game.information = "You don't have this magic";
                break;
            case NOTRAPS:
                errors = true;
                activeError = Errors.NOTRAPS;
                Game.information = "You don't have any traps";
                break;
            case HIHP:
                errors = true;
                activeError = Errors.HIHP;
                Game.information = "You can't have more than 100 HP";
                break;
            case HIMANA:
                errors = true;
                activeError = Errors.HIMANA;
                Game.information = "You can't have more than 100 Mana";
                break;
            case HITRAPS:
                errors = true;
                activeError = Errors.HITRAPS;
                Game.information = "You can't have more than 10 traps set";
                break;
            case HIBOW:
                errors = true;
                activeError = Errors.HIBOW;
                Game.information = "You already have a bow!";
                break;
            case NOERROR:
                errors = false;
                activeError = Errors.NOERROR;
                Game.information = "";
                break;
        }
    }
    
}
