/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.xframe;
import static game.Game.yframe;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author nate
 */
public class Database {
    /**
     * Connect via JDBC to SQLite database for high score keeping
     * @throws java.io.IOException
     */
    public static void dbConnect() throws IOException {
        System.out.println(xframe);
        System.out.println(yframe);
        Connection conn = null;
        try {
            //make a directory to store the database in
            File dir = new File(System.getProperty("user.home")+"/UnderworldDBs");
            if (!dir.exists()){
                dir.mkdir();
                byte data[] = new byte[0];
                Path file = Paths.get(System.getProperty("user.home")+"/UnderworldDBs/game.db");
                Files.write(file, data);
            }
            
            // db parameters
            String url = "jdbc:sqlite:"+System.getProperty("user.home")+"/UnderworldDBs/game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");

            String u = "CREATE TABLE IF NOT EXISTS highscores "
                + "(date DATETIME, "
                + "enemiesKilled INTEGER);";
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(u);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * Deletes all entries that are less than the third highest score in order
     * to keep the database small.
     * @throws java.io.IOException
     */
    public static void dbClean() throws IOException {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:"+System.getProperty("user.home")+"/UnderworldDBs/game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            String u = "DELETE FROM highscores "
                    + "WHERE enemiesKilled < "+getHighScore(3)+";";
            Statement st = conn.createStatement();
            st.executeUpdate(u);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Add score to the database
     * @param score
     */
    public static void addScore(int score) {
        Connection conn = null;
        try {
            //get current date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now)); //2016/11/16 12:08:43
            // db parameters
            String url = "jdbc:sqlite:"+System.getProperty("user.home")+"/UnderworldDBs/game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            String u = "INSERT INTO highscores "
                    + "VALUES ('"+dtf.format(now)+"', "+score+");";
            Statement st = conn.createStatement();
            st.executeUpdate(u);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get nth highest score from database
     * @param n
     * @return nth highest score
     */
    public static int getHighScore(int n){
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:"+System.getProperty("user.home")+"/UnderworldDBs/game.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            String q = "SELECT DISTINCT enemiesKilled " +
                "FROM highscores " +
                "ORDER BY 1 DESC " +
                "LIMIT 1 OFFSET "+(n-1)+";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(q);
            int max = 0;
            if (rs.next()){
                max = rs.getInt(1);
            }
            conn.close();
            return max;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
