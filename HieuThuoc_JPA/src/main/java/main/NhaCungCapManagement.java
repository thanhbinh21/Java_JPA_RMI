package main;

import gui.GUI_NhaCungCap;

import javax.swing.*;

/**
 * Main class to launch the NhaCungCap (Supplier) Management GUI
 */
public class NhaCungCapManagement {
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Optional: Improve font rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            GUI_NhaCungCap gui = new GUI_NhaCungCap();
            gui.setVisible(true);
        });
    }
} 