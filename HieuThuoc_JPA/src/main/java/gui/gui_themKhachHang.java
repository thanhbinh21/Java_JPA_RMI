package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.KhachHangDAO;
import dao.impl.KhachHangDAOImpl;

import entity.KhachHang;
import other.MessageDialog;
import other.RandomMa;
import other.Validation;
import service.KhachHangService;
import service.impl.KhachHangServiceImpl;
import javax.swing.UIManager;
import javax.swing.ImageIcon;

public class gui_themKhachHang extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txt_sdt;
    private JTextField txt_ten;
    JComboBox cbb_gioiTinh;


	private KhachHang kh;
    private KhachHangService KHACH_HANG_SERVICE;

    /**
     * Create the panel.
     */
    public gui_themKhachHang() {
        try {
            KHACH_HANG_SERVICE = new KhachHangServiceImpl(new KhachHangDAOImpl());
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo service: " + e.getMessage());
            e.printStackTrace();
        }
        
    	getContentPane().setBackground(UIManager.getColor("CheckBox.darkShadow"));
        setBackground(SystemColor.textInactiveText);
        getContentPane().setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Thêm khách hàng");
        lblNewLabel.setIcon(new ImageIcon(gui_themKhachHang.class.getResource("/icon/btnKhachhang.png")));
        lblNewLabel.setForeground(new Color(255, 255, 0));
        lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel.setBounds(209, 10, 233, 46);
        getContentPane().add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("SDT");
        lblNewLabel_1.setBackground(SystemColor.text);
        lblNewLabel_1.setForeground(SystemColor.text);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(123, 79, 57, 23);
        getContentPane().add(lblNewLabel_1);
        
        txt_sdt = new JTextField();
        txt_sdt.setBorder(null);
        txt_sdt.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txt_sdt.setBounds(248, 75, 256, 27);
        getContentPane().add(txt_sdt);
        txt_sdt.setColumns(10);

        JLabel lblNewLabel_1_1 = new JLabel("TÊN");
        lblNewLabel_1_1.setForeground(SystemColor.text);
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1.setBounds(123, 132, 57, 23);
        getContentPane().add(lblNewLabel_1_1);
        
        txt_ten = new JTextField();
        txt_ten.setBorder(null);
        txt_ten.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txt_ten.setColumns(10);
        txt_ten.setBounds(248, 132, 256, 27);
        getContentPane().add(txt_ten);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("GIOI TINH");
        lblNewLabel_1_1_1.setForeground(SystemColor.text);
        lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1_1_1.setBounds(123, 190, 81, 23);
        getContentPane().add(lblNewLabel_1_1_1);
        
        cbb_gioiTinh = new JComboBox();
        cbb_gioiTinh.setModel(new DefaultComboBoxModel(new String[] {"Nam", "Nữ"}));
        cbb_gioiTinh.setBorder(null);
        cbb_gioiTinh.setBounds(248, 190, 256, 21);
        getContentPane().add(cbb_gioiTinh);
        
        JButton btn_yes = new JButton("     Thêm");
        btn_yes.setIcon(new ImageIcon(gui_themKhachHang.class.getResource("/icon/add.png")));
        btn_yes.setForeground(Color.BLACK);
        btn_yes.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_yes.setBorder(null);
        btn_yes.setBackground(Color.CYAN);
        btn_yes.setBounds(248, 283, 157, 40);
        getContentPane().add(btn_yes);
        btn_yes.addActionListener(e -> btnAddActionPerformed());
        
    }
    private boolean isValidateFields() {
        if (txt_ten.getText().trim().equals("")) {
            MessageDialog.warring(this, "Họ tên không được rỗng!");
            txt_ten.requestFocus();
            return false;
        }

        if (txt_sdt.getText().trim().equals("") || !Validation.isNumber(txt_sdt.getText()) || txt_sdt.getText().length() != 10) {
            MessageDialog.warring(this, "Số điện thoại không được rỗng và có 10 ký tự sô!");
            txt_sdt.requestFocus();
            return false;
        }

        return true;
    }

    private KhachHang getInputFields() {
        String id = RandomMa.maKHAuto();
        String hoTen = txt_ten.getText().trim();
        String sdt = txt_sdt.getText().trim();
        boolean gioiTinh = false ;
        if(cbb_gioiTinh.getSelectedIndex()==1) {
        	gioiTinh= true;
        }
        Date ngayThamGia = new Date(System.currentTimeMillis());
        LocalDate localDate = new java.sql.Date(ngayThamGia.getTime()).toLocalDate();
        kh = new KhachHang(id, hoTen, sdt, gioiTinh,localDate );
        return kh;
    }
    private void btnAddActionPerformed() {
        if (isValidateFields()) {
            KhachHang nv = getInputFields();
            try {
                KHACH_HANG_SERVICE.save(nv);
                MessageDialog.info(null, "Thêm thành công!");
                this.dispose();
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public KhachHang getKh() {
		return kh;
	}
}
