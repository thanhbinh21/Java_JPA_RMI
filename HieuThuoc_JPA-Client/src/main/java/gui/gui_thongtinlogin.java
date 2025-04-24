
package gui;

import entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

public class gui_thongtinlogin extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel labelMANV;
    private JLabel labelTEN;
    private JLabel labelSDT;
    private JLabel labelNGAYVAOLAM;

    /**
     * Create the panel.
     */
    public gui_thongtinlogin(TaiKhoan taiKhoan) {
        setBackground(SystemColor.textInactiveText);
        setLayout(null);
        
        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN");
        lblTitle.setForeground(new Color(255, 255, 0));
        lblTitle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        lblTitle.setBounds(209, 10, 233, 46);
        add(lblTitle);
        
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setBackground(SystemColor.text);
        lblMaNV.setForeground(SystemColor.text);
        lblMaNV.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblMaNV.setBounds(123, 79, 115, 23);
        add(lblMaNV);
        
        labelMANV = new JLabel(taiKhoan.getNhanVien().getId());
        labelMANV.setFont(new Font("Tahoma", Font.PLAIN, 15));
        labelMANV.setBounds(248, 75, 256, 27);
        labelMANV.setForeground(Color.WHITE);
        labelMANV.setOpaque(true);
        labelMANV.setBackground(new Color(60, 60, 60));
        labelMANV.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(labelMANV);

        JLabel lblHoTen = new JLabel("Họ và tên:");
        lblHoTen.setForeground(SystemColor.text);
        lblHoTen.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblHoTen.setBounds(123, 132, 115, 23);
        add(lblHoTen);
        
        labelTEN = new JLabel(taiKhoan.getNhanVien().getHoTen());
        labelTEN.setFont(new Font("Tahoma", Font.PLAIN, 15));
        labelTEN.setBounds(248, 132, 256, 27);
        labelTEN.setForeground(Color.WHITE);
        labelTEN.setOpaque(true);
        labelTEN.setBackground(new Color(60, 60, 60));
        labelTEN.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(labelTEN);
        
        JLabel lblSdt = new JLabel("Số điện thoại:");
        lblSdt.setForeground(SystemColor.text);
        lblSdt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblSdt.setBounds(123, 190, 115, 23);
        add(lblSdt);
        
        labelSDT = new JLabel(taiKhoan.getNhanVien().getSoDienThoai());
        labelSDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
        labelSDT.setBounds(248, 190, 256, 27);
        labelSDT.setForeground(Color.WHITE);
        labelSDT.setOpaque(true);
        labelSDT.setBackground(new Color(60, 60, 60));
        labelSDT.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(labelSDT);
        
        JLabel lblNgayVaoLam = new JLabel("Ngày vào làm:");
        lblNgayVaoLam.setForeground(SystemColor.text);
        lblNgayVaoLam.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNgayVaoLam.setBounds(123, 248, 115, 23);
        add(lblNgayVaoLam);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String ngayVaoLamString = taiKhoan.getNhanVien().getNgayVaoLam().format(formatter);

        labelNGAYVAOLAM = new JLabel(ngayVaoLamString);
        labelNGAYVAOLAM.setFont(new Font("Tahoma", Font.PLAIN, 15));
        labelNGAYVAOLAM.setBounds(248, 248, 256, 27);
        labelNGAYVAOLAM.setForeground(Color.WHITE);
        labelNGAYVAOLAM.setOpaque(true);
        labelNGAYVAOLAM.setBackground(new Color(60, 60, 60));
        labelNGAYVAOLAM.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(labelNGAYVAOLAM);

        JButton btnExit = new JButton("Đóng");
        btnExit.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setBounds(300, 300, 120, 40);
        
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Find the parent dialog and close it
                JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(gui_thongtinlogin.this);
                if (dialog != null) {
                    dialog.dispose();
                }
            }
        });
        add(btnExit);
    }
}

