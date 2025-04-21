package main;

import gui.GUI_PhieuNhapThuoc;

import javax.swing.*;

/**
 * Main class to launch the Medicine Import Receipt (PhieuNhapThuoc) Management GUI
 */
public class PhieuNhapThuocManagement {
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Improve font rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            GUI_PhieuNhapThuoc gui = new GUI_PhieuNhapThuoc();
            gui.setVisible(true);
        });
    }
} 