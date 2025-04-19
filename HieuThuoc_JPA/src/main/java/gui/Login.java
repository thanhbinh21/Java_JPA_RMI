package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import dao.TaiKhoanDAO;

import dao.impl.TaiKhoanDAOImpl;
import entity.TaiKhoan;
import other.MessageDialog;
import other.Validation;

public class Login extends JFrame implements ActionListener {

    private JFrame frmlogin;
    private JTextField tx_username;
    private JPasswordField tx_password;
    private TaiKhoanDAO TK_DAO = new TaiKhoanDAOImpl();
    private boolean showPassword = false; 

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Login window = new Login();
                window.frmlogin.setVisible(true);
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    	
	
		public JFrame getFrmlogin() {
			return frmlogin;
		}
	
		public void setFrmlogin(JFrame frmlogin) {
			this.frmlogin = frmlogin;
		}
	
	    public Login() {
	        login();
	    }
    
    private void login() {
        frmlogin = new JFrame();
        setSize(600, 350);
        frmlogin.setFont(new Font("Dialog", Font.BOLD, 15));
       // frmlogin.setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("icon/QuanLyNhaThuoc.png")));
        frmlogin.setTitle("NHÀ THUỐC OVERRATED");
        frmlogin.setBounds(100, 100, 593, 355);
        frmlogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmlogin.getContentPane().setLayout(null);
        frmlogin.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(SystemColor.textInactiveText);
        panel.setBounds(0, 0, 589, 330);
        frmlogin.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lbtitlelogin = new JLabel("ĐĂNG NHẬP YÊN TÂM");
        lbtitlelogin.setForeground(new Color(255, 255, 0));
        lbtitlelogin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lbtitlelogin.setBounds(176, 10, 237, 55);
        panel.add(lbtitlelogin);

        JLabel lb_username = new JLabel("Tên tài khoản");
        lb_username.setBackground(SystemColor.text);
        lb_username.setForeground(SystemColor.text);
        lb_username.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lb_username.setBounds(104, 75, 130, 70);
        panel.add(lb_username);

        tx_username = new JTextField();
        tx_username.setBorder(null);
        tx_username.setBounds(250, 96, 200, 30);
        panel.add(tx_username);

        JLabel lb_password = new JLabel("Mật khẩu");
        lb_password.setForeground(SystemColor.text);
        lb_password.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lb_password.setBounds(104, 159, 130, 70);
        panel.add(lb_password);

        // Tạo JPanel chứa cả JPasswordField và nút con mắt
        JPanel passwordPanel = new JPanel();
        passwordPanel.setBackground(UIManager.getColor("Button.highlight"));
        passwordPanel.setLayout(null);
        passwordPanel.setBounds(250, 168, 200, 30);
        panel.add(passwordPanel);

        // Trường nhập mật khẩu
        tx_password = new JPasswordField();
        tx_password.setBorder(null);
        tx_password.setBounds(0, 0, 170, 30);
        tx_password.setEchoChar('•');
        passwordPanel.add(tx_password);

        // Nút con mắt
        System.out.println(Login.class.getResource("HieuThuoc_JPA/src/main/java/icon/eye.png"));
        JButton btn_eye = new JButton(new ImageIcon(Login.class.getResource("/icon/eye.png")));
        btn_eye.setBorder(null);
        btn_eye.setBackground(SystemColor.textHighlightText);
        btn_eye.setBounds(175, 5, 20, 20);
        btn_eye.setFocusPainted(false);
        btn_eye.setBorderPainted(false);
        btn_eye.setContentAreaFilled(false);
        passwordPanel.add(btn_eye);

        // Xử lý sự kiện ấn nút con mắt
        btn_eye.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePasswordVisibility();
                btn_eye.setIcon(new ImageIcon(Login.class.getResource(showPassword ? "/icon/visible.png" : "/icon/eye.png")));
            }
        });
//        tx_username.setText("ADMIN");
//		tx_password.setText("123");
        JButton btn_yes = new JButton("Đăng nhập");
        btn_yes.setBorder(null);
        btn_yes.setIcon(new ImageIcon(Login.class.getResource("/icon/enter.png")));
        btn_yes.setBackground(Color.CYAN);
        btn_yes.setForeground(Color.BLACK);
        btn_yes.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_yes.setBounds(293, 239, 157, 40);
        panel.add(btn_yes);

        JButton btn_no = new JButton("     Thoát");
        btn_no.setBorder(null);
        btn_no.setIcon(new ImageIcon(Login.class.getResource("/icon/logout.png")));
        btn_no.setForeground(Color.BLACK);
        btn_no.setBackground(Color.CYAN);
        btn_no.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_no.setBounds(94, 239, 140, 40);
        panel.add(btn_no);

        btn_yes.addActionListener(e -> authentication());
        btn_no.addActionListener(e -> System.exit(0));
    }

    private void togglePasswordVisibility() {
        showPassword = !showPassword;
        tx_password.setEchoChar(showPassword ? (char) 0 : '•'); // Hiện hoặc ẩn mật khẩu
    }
	private boolean isValidateFields() {
		if (Validation.isEmpty(tx_username.getText()) || Validation.isEmpty(String.valueOf(tx_password.getPassword()))) {
			MessageDialog.warring(this, "Không được để trống!");
			return false;
		}

		return true;
	}

	private void authentication() {
    String username = tx_username.getText();
    String password = new String(((JPasswordField) tx_password).getPassword());


    if (isValidateFields()) {
        TaiKhoan tk = TK_DAO.findById(username);

        if (tk == null) {
            MessageDialog.error(this, "Tài khoản không tồn tại!");
            return;
        }

        if (username.equals(tk.getId()) && password.equals(tk.getPassword())) {
            gui_TrangChu trangChu = new gui_TrangChu(tk); // Truyền tài khoản vào đây
            trangChu.setVisible(true);
            frmlogin.setVisible(false); 
            this.dispose();
        } else {
            MessageDialog.error(this, "Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại!");
        }
    }
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        // Không dùng trong ví dụ này
    }
}


