/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Nathan
 */

public class Launcher extends JFrame {
    CardLayout cards = new CardLayout();    
    JPanel p = new JPanel(cards); //panel with play button
    JPanel i = new JPanel(cards); //panel with image
    JPanel r = new JPanel(); //resolution selector
    final static String D1 = "640x480";
    final static String D2 = "800x600";
    final static String D3 = "1280x720";
    final static String D4 = "1920x1080";
    final static String D5 = "Detect Screen Resolution";
    JButton playButton = new JButton("Play!");
    String resolutions[] = { D1, D2, D3, D4, D5 };
    JComboBox resBox = new JComboBox(resolutions);
     
    public void addComponentToPane(Container pane) throws MalformedURLException {
        //add resolution selector to panel
        r.add(resBox);
        
        //action listener for play button
        playButton.addActionListener((ActionEvent ae) -> {
            switch(resBox.getSelectedItem().toString()){
                case D1:
                {
                    try {
                        Game.xframe = 640;
                        Game.yframe = 480;
                        Game.gameLoop();
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case D2:
                {
                    try {
                        Game.xframe = 800;
                        Game.yframe = 600;
                        Game.gameLoop();
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case D3:
                {
                    try {
                        Game.xframe = 1280;
                        Game.yframe = 720;
                        Game.gameLoop();
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case D4:
                {
                    try {
                        Game.xframe = 1920;
                        Game.yframe = 1080;
                        Game.gameLoop();
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case D5:
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    {
                        try {
                        Game.xframe = screen.width;
                        Game.yframe = screen.height;
                        Game.gameLoop();
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    break;
            }
        });
        
        //add play button to panel
        p.add(playButton);
        
        //add logo to panel
        ImageIcon underworld = new ImageIcon(getClass().getResource("/images/underworld.png"));
        JLabel image = new JLabel(underworld);
        i.add(image);
        
        //add panels to container
        pane.add(r, BorderLayout.CENTER);
        pane.add(i, BorderLayout.PAGE_START);
        pane.add(p, BorderLayout.SOUTH);
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void showLauncher() throws MalformedURLException {
        //Create and set up the window.
        JFrame frame = new JFrame("Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
         
        //Create and set up the content pane.
        Launcher l = new Launcher();
        l.addComponentToPane(frame.getContentPane());
        
        //change icon
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(
                        JFrame.class.getResource("/images/logo.png")));
         
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
         
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                showLauncher();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}