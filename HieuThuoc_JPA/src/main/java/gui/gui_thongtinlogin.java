
package gui;

import entity.TaiKhoan;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.SimpleDateFormat;

import javax.swing.JTextField;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

public class gui_thongtinlogin extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldMANV;
    private JTextField textFieldTEN;
    private JTextField textFieldSDT;
    private JTextField textFieldNGAYVAOLAM;

    /**
     * Create the panel.
     */
    public gui_thongtinlogin(TaiKhoan taiKhoan) {
        setBackground(SystemColor.textInactiveText);
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel("THÔNG TIN CÁ NHÂN");
        lblNewLabel.setForeground(new Color(255, 255, 0));
        lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel.setBounds(209, 10, 233, 46);
        add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("MANV");
        lblNewLabel_1.setBackground(SystemColor.text);
        lblNewLabel_1.setForeground(SystemColor.text);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(123, 79, 57, 23);
        add(lblNewLabel_1);
        
        textFieldMANV = new JTextField(taiKhoan.getNhanVien().getHoTen());
        textFieldMANV.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textFieldMANV.setBounds(248, 75, 256, 27);
        add(textFieldMANV);
        textFieldMANV.setColumns(10);

        JLabel lblNewLabel_1_1 = new JLabel("TÊN");
        lblNewLabel_1_1.setForeground(SystemColor.text);
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1.setBounds(123, 132, 57, 23);
        add(lblNewLabel_1_1);
        
        textFieldTEN = new JTextField(taiKhoan.getNhanVien().getHoTen());
        textFieldTEN.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textFieldTEN.setColumns(10);
        textFieldTEN.setBounds(248, 132, 256, 27);
        add(textFieldTEN);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("SDT");
        lblNewLabel_1_1_1.setForeground(SystemColor.text);
        lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1_1.setBounds(123, 190, 57, 23);
        add(lblNewLabel_1_1_1);
        
        textFieldSDT = new JTextField(taiKhoan.getNhanVien().getSoDienThoai());
        textFieldSDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textFieldSDT.setColumns(10);
        textFieldSDT.setBounds(248, 190, 256, 27);
        add(textFieldSDT);
        
        JLabel lblNewLabel_1_1_1_1 = new JLabel("NGÀY VÀO LÀM");
        lblNewLabel_1_1_1_1.setForeground(SystemColor.text);
        lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1_1_1.setBounds(123, 248, 115, 23);
        add(lblNewLabel_1_1_1_1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String ngayVaoLamString = taiKhoan.getNhanVien().getNgayVaoLam().format(formatter);

		textFieldNGAYVAOLAM = new JTextField(ngayVaoLamString);
		textFieldNGAYVAOLAM.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldNGAYVAOLAM.setBounds(248, 248, 256, 27);
		add(textFieldNGAYVAOLAM);
    }
}

