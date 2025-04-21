package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import dao.*;
import dao.impl.*;
import entity.ChiTietPhieuDatThuoc;
import entity.KhachHang;
import entity.TaiKhoan;
import entity.Thuoc;
import other.Formatter;
import other.MessageDialog;
import other.RandomMa;
import other.Validation;
import service.DatThuocSevice;
import service.KhachHangService;
import service.PhieuDatThuocService;
import service.ThuocService;


import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class gui_DatThuoc extends JPanel {
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextField textField_8;
    private JTextField textField_9;
    private JTextField textField_10;
	private JComponent table_right;
	private JTextField txtMaThuoc;
	private JTextField txtTenThuoc;
	private JTextField txtThanhPhan;
	private JTextField txtDonGia;
	private JTextField txtSearch;
	private JTextField txtSoLuong;
	private JTable table;

	private JTextField txtMaPDT;
	private JTextField txtsdt; 
	
	private JTextField txtHoTenKH;
	private JTextField txtTongTien;
 	JLabel txtHinhAnh = new JLabel("");

	private ThuocService THUOC_SEVICE;
	private KhachHangService KHACH_HANG_SEVICE;
	private DatThuocSevice DAT_THUOC_SEVICE;
	private PhieuDatThuocService PHEU_DAT_THUOC_SEVICE;




	private DefaultTableModel modal;
	private DefaultTableModel modalCart= new DefaultTableModel();

	private List<ChiTietPhieuDatThuoc> listCTHD = new ArrayList<>();
	private List<Thuoc> listThuoc = new ArrayList<>();
	private TaiKhoan tklogin ;
	private JTable tableCart;
	private JComboBox cboxSearch;
	private JTextField txttongHD;
	private JTextField txtThue;


	public static void main(String[] args) {
		// Tạo tài khoản demo
		TaiKhoan tkDemo = new TaiKhoan();
		tkDemo.setId("admin");
		tkDemo.setPassword("123456");

		// Tạo và hiển thị giao diện
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Đặt Thuốc Siêu Xịn");
			gui_DatThuoc panel = new gui_DatThuoc(tkDemo);

			frame.setContentPane(panel);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}




    public gui_DatThuoc(TaiKhoan login) {
    	setBackground(SystemColor.control);
    	tklogin = login;
    	
    	setLayout(null);
    	setSize(1400,800);
    	JPanel panel = new JPanel();
    	panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    	panel.setBounds(10, 59, 708, 654);
    	add(panel);
    	panel.setLayout(null);
    	
    	JPanel panel_2 = new JPanel();
    	panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
    	panel_2.setBackground(SystemColor.activeCaption);
    	panel_2.setBounds(174, 10, 411, 35);
    	panel.add(panel_2);
    	panel_2.setLayout(new BorderLayout(0, 0));
    	
    	JLabel lblNewLabel_1_1_1 = new JLabel("THÔNG TIN THUỐC");
    	lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    	lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
    	panel_2.add(lblNewLabel_1_1_1);
    	lblNewLabel_1_1_1.setForeground(Color.BLACK);
    	lblNewLabel_1_1_1.setBackground(Color.BLACK);
    	
    	JPanel panel_3 = new JPanel();
    	panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
    	panel_3.setBounds(20, 55, 210, 176);
    	panel.add(panel_3);
    	panel_3.setLayout(null);
    	
   
    	txtHinhAnh.setBounds(10, 10, 190, 161);
    	panel_3.add(txtHinhAnh);
    	
    	JPanel panel_4 = new JPanel();
    	panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
    	panel_4.setBounds(284, 55, 384, 176);
    	panel.add(panel_4);
    	panel_4.setLayout(null);
    	
    	JLabel lblNewLabel_2 = new JLabel("MATHUOC:");
    	lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2.setBounds(10, 10, 84, 27);
    	panel_4.add(lblNewLabel_2);
    	
    	JLabel lblNewLabel_2_1 = new JLabel("TENTHUOC:");
    	lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_1.setBounds(10, 47, 84, 27);
    	panel_4.add(lblNewLabel_2_1);
    	
    	JLabel lblNewLabel_2_2 = new JLabel("THANHPHAN:");
    	lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_2.setBounds(10, 84, 107, 27);
    	panel_4.add(lblNewLabel_2_2);
    	
    	JLabel lblNewLabel_2_3 = new JLabel("DONGIA:");
    	lblNewLabel_2_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_3.setBounds(10, 139, 84, 27);
    	panel_4.add(lblNewLabel_2_3);
    	
    	txtMaThuoc = new JTextField();
    	txtMaThuoc.setHorizontalAlignment(SwingConstants.CENTER);
    	txtMaThuoc.setBounds(128, 16, 215, 19);
    	panel_4.add(txtMaThuoc);
    	txtMaThuoc.setColumns(10);
    	
    	txtTenThuoc = new JTextField();
    	txtTenThuoc.setFont(new Font("Tahoma", Font.BOLD, 10));
    	txtTenThuoc.setHorizontalAlignment(SwingConstants.CENTER);
    	txtTenThuoc.setColumns(10);
    	txtTenThuoc.setBounds(128, 53, 215, 19);
    	panel_4.add(txtTenThuoc);
    	
    	txtThanhPhan = new JTextField();
    	txtThanhPhan.setHorizontalAlignment(SwingConstants.CENTER);
    	txtThanhPhan.setColumns(10);
    	txtThanhPhan.setBounds(128, 90, 215, 42);
    	panel_4.add(txtThanhPhan);
    	
    	txtDonGia = new JTextField();
    	txtDonGia.setFont(new Font("Tahoma", Font.BOLD, 10));
    	txtDonGia.setHorizontalAlignment(SwingConstants.CENTER);
    	txtDonGia.setColumns(10);
    	txtDonGia.setBounds(128, 145, 215, 19);
    	 txtDonGia.setText("0.0");
         txtDonGia.setFocusable(false);
    	panel_4.add(txtDonGia);
    	
    	JPanel panel_5 = new JPanel();
    	panel_5.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh s\u00E1ch Thu\u1ED1c", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
    	panel_5.setBounds(21, 332, 647, 312);
    	panel.add(panel_5);
    	panel_5.setLayout(null);
    	
    	JScrollPane scrollPane = new JScrollPane();
    
    	scrollPane.setBounds(20, 24, 603, 266);
    	panel_5.add(scrollPane);
    	
    	table = new JTable();
    	table.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	table.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			tableMouseClicked();
    		}
    	});
    
    
    	scrollPane.setViewportView(table);
    	JPanel panel_6 = new JPanel();
    	panel_6.setBounds(21, 241, 647, 81);
    	panel.add(panel_6);
    	panel_6.setLayout(null);
    	
    	 cboxSearch = new JComboBox();
    	 cboxSearch.addActionListener(new ActionListener() {
    	 	public void actionPerformed(ActionEvent e) {
    	 		loadTableTheoDanhMuc();
    	 	}
    	 });
    	 cboxSearch.setBackground(SystemColor.text);
    	 cboxSearch.setModel(new DefaultComboBoxModel(new String[] {"Tất cả"}));
    	 cboxSearch.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Lo\u1EA1i Thu\u1ED1c", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
    	
    	cboxSearch.setBounds(10, 24, 160, 42);
    	panel_6.add(cboxSearch);
    	//
  
    	//
    	txtSearch = new JTextField("");
    	txtSearch.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			ketQuaTimKiem();
    		}
    	});
    	
    	txtSearch.setBorder(new TitledBorder(null, "Tra c\u1EE9u", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	txtSearch.setDragEnabled(true);
    	
    	txtSearch.setToolTipText("Tra cứu");
    	txtSearch.setBounds(180, 24, 128, 42);
    	panel_6.add(txtSearch);
    	txtSearch.setColumns(10);
    	
    	JSeparator separator = new JSeparator();
    	separator.setFont(new Font("Arial", Font.PLAIN, 20));
    	separator.setOrientation(SwingConstants.VERTICAL);
    	separator.setBounds(375, 16, 2, 55);
    	panel_6.add(separator);
    	
    	JButton btnAddCart = new JButton();
    	btnAddCart.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnAddCartActionPerformed();
    		}
    	});
    	
    	btnAddCart.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/add-to-cart.png")));
    	btnAddCart.setText("THÊM");
    	btnAddCart.setPreferredSize(new Dimension(120, 40));
    	btnAddCart.setForeground(SystemColor.desktop);
    	btnAddCart.setFont(new Font("Dialog", Font.BOLD, 16));
    	btnAddCart.setFocusable(false);
    	btnAddCart.setFocusPainted(false);
    	btnAddCart.setBorderPainted(false);
    	btnAddCart.setBackground(SystemColor.activeCaptionBorder);
    	btnAddCart.setBounds(520, 24, 117, 36);
    	panel_6.add(btnAddCart);
    	
    	JButton btnReload = new JButton();
    	btnReload.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnReloadActionPerformed();
    		}
    	});
    	btnReload.setBackground(SystemColor.activeCaptionBorder);
    	btnReload.setBounds(318, 24, 47, 42);
    	panel_6.add(btnReload);
    	btnReload.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/refresh.png")));
    	btnReload.setToolTipText("Làm mới");
    	btnReload.setPreferredSize(new Dimension(40, 40));
    	btnReload.setHorizontalTextPosition(SwingConstants.CENTER);
    	btnReload.setFocusable(false);
    	btnReload.setFocusPainted(false);
    	btnReload.setBorderPainted(false);
    	btnReload.setBorder(null);
    	
    	txtSoLuong = new JTextField();
    	txtSoLuong.setFont(new Font("Tahoma", Font.BOLD, 10));
    	txtSoLuong.setBorder(new TitledBorder(null, "S\u1ED1 l\u01B0\u1EE3ng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	txtSoLuong.setBounds(387, 21, 123, 40);
    	panel_6.add(txtSoLuong);
    	txtSoLuong.setColumns(10);
    	
    	JPanel panel_1 = new JPanel();
    	panel_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    	panel_1.setBounds(744, 59, 646, 654);
    	add(panel_1);
    	panel_1.setLayout(null);
    	
    	JPanel panel_2_1 = new JPanel();
    	panel_2_1.setBackground(SystemColor.activeCaption);
    	panel_2_1.setBounds(142, 10, 411, 35);
    	panel_1.add(panel_2_1);
    	
    	JLabel lblNewLabel_1_1_1_1 = new JLabel("CHI TIẾT PHIẾU ĐẶT");
    	lblNewLabel_1_1_1_1.setForeground(Color.BLACK);
    	lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
    	lblNewLabel_1_1_1_1.setBackground(Color.BLACK);
    	panel_2_1.add(lblNewLabel_1_1_1_1);
    	
    	JPanel panel_5_1 = new JPanel();
    	panel_5_1.setLayout(null);
    	panel_5_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Chi Ti\u1EBFt", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    	panel_5_1.setBounds(42, 55, 594, 194);
    	panel_1.add(panel_5_1);
    	
    	JScrollPane scrollPane_1 = new JScrollPane();
    	scrollPane_1.setBounds(10, 25, 574, 107);
    	panel_5_1.add(scrollPane_1);
    	
    	tableCart = new JTable();
    	tableCart.setModel(new DefaultTableModel(new Object[][] {}, new String[] {"STT", "Tên thuốc", "Số lượng", "Đơn giá"}));
   
    	
    	scrollPane_1.setViewportView(tableCart);
    	
    	JPanel panel_8 = new JPanel();
    	panel_8.setBounds(471, 145, 93, 39);
    	panel_5_1.add(panel_8);
    	panel_8.setLayout(null);
    	
    	JButton btnXoa = new JButton();
    	btnXoa.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/trash.png")));
    	btnXoa.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnDeleteCartItemActionPerformed();
    		}
    	});
    	btnXoa.setText("XÓA");
    	btnXoa.setPreferredSize(new Dimension(120, 40));
    	btnXoa.setForeground(SystemColor.desktop);
    	btnXoa.setFont(new Font("Dialog", Font.BOLD, 16));
    	btnXoa.setFocusable(false);
    	btnXoa.setFocusPainted(false);
    	btnXoa.setBorderPainted(false);
    	btnXoa.setBackground(SystemColor.activeCaptionBorder);
    	btnXoa.setBounds(-11, 0, 117, 44);
    	panel_8.add(btnXoa);
    	
    	JPanel panel_2_1_1 = new JPanel();
    	panel_2_1_1.setBackground(SystemColor.activeCaption);
    	panel_2_1_1.setBounds(142, 259, 411, 35);
    	panel_1.add(panel_2_1_1);
    	
    	JLabel lblNewLabel_1_1_1_1_1 = new JLabel("PHIẾU ĐẶT");
    	lblNewLabel_1_1_1_1_1.setForeground(Color.BLACK);
    	lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
    	lblNewLabel_1_1_1_1_1.setBackground(Color.BLACK);
    	panel_2_1_1.add(lblNewLabel_1_1_1_1_1);
    	
    	JPanel panel_4_1 = new JPanel();
    	panel_4_1.setLayout(null);
    	panel_4_1.setBorder(new LineBorder(new Color(0, 0, 0)));
    	panel_4_1.setBounds(42, 316, 594, 328);
    	panel_1.add(panel_4_1);
    	
    	JLabel lblNewLabel_2_4 = new JLabel("Mã PDT");
    	lblNewLabel_2_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_4.setBounds(33, 14, 84, 27);
    	panel_4_1.add(lblNewLabel_2_4);
    	
    	JLabel lblNewLabel_2_1_1 = new JLabel("SDT");
    	lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_1_1.setBounds(33, 54, 84, 27);
    	panel_4_1.add(lblNewLabel_2_1_1);
    	
    	JLabel lblNewLabel_2_2_1 = new JLabel("TENKH");
    	lblNewLabel_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_2_1.setBounds(33, 98, 107, 27);
    	panel_4_1.add(lblNewLabel_2_2_1);
    	
    	JLabel lblNewLabel_2_3_1 = new JLabel("TỔNG TIỀN");
    	lblNewLabel_2_3_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_3_1.setBounds(33, 171, 84, 27);
    	panel_4_1.add(lblNewLabel_2_3_1);
    	
    	txtMaPDT = new JTextField();
    	
    	txtMaPDT.setEditable(false);
    	txtMaPDT.setBackground(new Color(255, 255, 255));
    	txtMaPDT.setFont(new Font("Arial", Font.BOLD, 15));
    	txtMaPDT.setColumns(10);
    	txtMaPDT.setBounds(128, 16, 359, 27);
    	
    	
    	
    	txtMaPDT.setEnabled(false);
    	panel_4_1.add(txtMaPDT);
   
    	txtsdt = new JTextField();
    	txtsdt.setHorizontalAlignment(SwingConstants.CENTER);
    	txtsdt.setFont(new Font("Arial", Font.BOLD, 15));
    	txtsdt.setColumns(10);
    	txtsdt.setBounds(128, 53, 359, 34);
    	panel_4_1.add(txtsdt);
    	
    	txtHoTenKH = new JTextField();
    	txtHoTenKH.setHorizontalAlignment(SwingConstants.CENTER);
    	txtHoTenKH.setFont(new Font("Arial", Font.BOLD, 15));
    	txtHoTenKH.setColumns(10);
    	txtHoTenKH.setBounds(127, 97, 360, 34);
    	panel_4_1.add(txtHoTenKH);
    	
    	txtTongTien = new JTextField("0.0");
    	txtTongTien.setHorizontalAlignment(SwingConstants.CENTER);
    	txtTongTien.setFont(new Font("Arial", Font.BOLD, 15));
    	
    	txtTongTien.setFocusable(false);
    	txtTongTien.setColumns(10);
    	txtTongTien.setBounds(127, 168, 186, 34);
    	panel_4_1.add(txtTongTien);
    	
    	JLabel lblNewLabel_3 = new JLabel("-------------------------------------------------------------------------------");
    	lblNewLabel_3.setBounds(161, 141, 326, 13);
    	panel_4_1.add(lblNewLabel_3);
    	
    	JButton btn_tkim = new JButton();
    	btn_tkim.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnSearchActionPerformed();
    			
    		}
    	});
    	btn_tkim.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/search.png")));
    	btn_tkim.setToolTipText("Tìm");
    	btn_tkim.setPreferredSize(new Dimension(40, 40));
    	btn_tkim.setHorizontalTextPosition(SwingConstants.CENTER);
    	btn_tkim.setFocusable(false);
    	btn_tkim.setFocusPainted(false);
    	btn_tkim.setBorderPainted(false);
    	btn_tkim.setBorder(null);
    	btn_tkim.setBackground(SystemColor.activeCaptionBorder);
    	btn_tkim.setBounds(516, 42, 40, 40);
    	panel_4_1.add(btn_tkim);
    	
    	JButton btnKhachHang = new JButton();
//    	btnKhachHang.addActionListener(new ActionListener() {
//    	    public void actionPerformed(ActionEvent e) {
//    	        gui_themKhachHang dialog = new gui_themKhachHang();
//    	        dialog.setSize(600, 400);
//    	        dialog.setLocationRelativeTo(null);
//    	        dialog.setVisible(true);
//
//    	    }
//    	});


    	btnKhachHang.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/user.png")));
    	btnKhachHang.setToolTipText("Xem ");
    	btnKhachHang.setPreferredSize(new Dimension(40, 40));
    	btnKhachHang.setHorizontalTextPosition(SwingConstants.CENTER);
    	btnKhachHang.setFocusable(false);
    	btnKhachHang.setFocusPainted(false);
    	btnKhachHang.setBorderPainted(false);
    	btnKhachHang.setBorder(null);
    	btnKhachHang.setBackground(SystemColor.activeCaptionBorder);
    	btnKhachHang.setBounds(516, 98, 40, 40);
    	panel_4_1.add(btnKhachHang);
    	
    	JLabel lblNewLabel_2_3_1_1 = new JLabel("VAT");
    	lblNewLabel_2_3_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    	lblNewLabel_2_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_3_1_1.setBounds(304, 171, 69, 27);
    	panel_4_1.add(lblNewLabel_2_3_1_1);
    	
    	JButton btnLuuhd = new JButton();
    	btnLuuhd.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/bill.png")));
    	btnLuuhd.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnThanhToanActionPerformed();
    		}
    	});
    	btnLuuhd.setText("ĐẶT THUỐC");
    	btnLuuhd.setPreferredSize(new Dimension(120, 40));
    	btnLuuhd.setForeground(SystemColor.desktop);
    	btnLuuhd.setFont(new Font("Dialog", Font.BOLD, 16));
    	btnLuuhd.setFocusable(false);
    	btnLuuhd.setFocusPainted(false);
    	btnLuuhd.setBorderPainted(false);
    	btnLuuhd.setBackground(SystemColor.activeCaptionBorder);
    	btnLuuhd.setBounds(323, 271, 164, 44);
    	panel_4_1.add(btnLuuhd);
    	
    	JLabel lblNewLabel_2_3_1_2 = new JLabel("TỔNG THANH TOÁN");
    	lblNewLabel_2_3_1_2.setVerticalAlignment(SwingConstants.BOTTOM);
    	lblNewLabel_2_3_1_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
    	lblNewLabel_2_3_1_2.setBounds(33, 222, 172, 27);
    	panel_4_1.add(lblNewLabel_2_3_1_2);
    	
    	txttongHD = new JTextField("");
    	txttongHD.setHorizontalAlignment(SwingConstants.CENTER);
    	txttongHD.setFont(new Font("Arial", Font.BOLD, 15));
    	txttongHD.setColumns(10);
    	txttongHD.setBounds(215, 212, 272, 34);
    	panel_4_1.add(txttongHD);
    	
    	txtThue = new JTextField("");
    	txtThue.setFont(new Font("Arial", Font.BOLD, 15));
    	txtThue.setColumns(10);
    	txtThue.setBounds(373, 168, 114, 34);
    	panel_4_1.add(txtThue);
    	
    	JLabel lblNewLabel = new JLabel("ĐẶT THUỐC");
    	lblNewLabel.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/fulfillment.png")));
    	lblNewLabel.setForeground(SystemColor.desktop);
    	lblNewLabel.setBackground(Color.GREEN);
    	lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 35));
    	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	lblNewLabel.setBounds(581, 10, 296, 39);
    	add(lblNewLabel);
        //
    	//
    	txtMaPDT.setText(RandomMa.maPhieuDatAuto());
    	
    	JButton btnHuyHD = new JButton();
    	btnHuyHD.setIcon(new ImageIcon(gui_DatThuoc.class.getResource("/icon/return.png")));
    	btnHuyHD.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnHuyActionPerformed();
    		}
    	
    		
    	});
    	btnHuyHD.setText("HỦY ");
    	btnHuyHD.setPreferredSize(new Dimension(120, 40));
    	btnHuyHD.setForeground(new Color(255, 0, 0));
    	btnHuyHD.setFont(new Font("Dialog", Font.BOLD, 16));
    	btnHuyHD.setFocusable(false);
    	btnHuyHD.setFocusPainted(false);
    	btnHuyHD.setBorderPainted(false);
    	btnHuyHD.setBackground(SystemColor.activeCaptionBorder);
    	btnHuyHD.setBounds(33, 271, 150, 44);
    	panel_4_1.add(btnHuyHD);
    	//
      	loadDanhMucThuoc();
        try {
           listThuoc = THUOC_SEVICE.findAll();
			System.out.printf(THUOC_SEVICE.findAll().toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        loadTable(listThuoc);

    }

	public void loadTable(List<Thuoc> list) {
		String[] header = new String[]{"STT", "Mã thuốc", "Tên thuốc", "Danh mục", "Nhà Sản Xuất", "Đơn vị tính", "Số lượng tồn", "Đơn giá", "Giảm giá"};
		modal = new DefaultTableModel();
		modal.setColumnIdentifiers(header);
		table.setModel(modal);
		modal.setRowCount(0);
		listThuoc = list;
		int stt = 1;
		double giamGia;
		try {
			for (Thuoc e : listThuoc) {
				giamGia = 0; // Tạm thời để 0, cần xử lý logic khuyến mãi nếu có
				if (e.getHanSuDung().after(new Date(System.currentTimeMillis()))) {
					modal.addRow(new Object[]{String.valueOf(stt), e.getId(), e.getTen(), e.getDanhMuc().getTen(),
							e.getNhaSanXuat().getTen(), e.getDonViTinh(), e.getSoLuongTon(), Formatter.FormatVND(e.getDonGia()), Formatter.formatPercentage(giamGia)});
					stt++;
				}
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tải dữ liệu thuốc: " + e.getMessage());
			e.printStackTrace();
		}
	}

    private void tableMouseClicked() {
//        int row = table.getSelectedRow();
//        String idThuoc = modal.getValueAt(row, 1).toString();
//        Thuoc e = THUOC_CON.selectById(idThuoc);
//
//        ImageIcon imageIcon = new ImageIcon(
//                new ImageIcon(e.getHinhAnh()).getImage().getScaledInstance(txtHinhAnh.getWidth(), txtHinhAnh.getHeight(), Image.SCALE_SMOOTH));
//        txtHinhAnh.setIcon(imageIcon);
//       // txtHinhAnh.setIcon(imageIcon);
//
//        txtMaThuoc.setText(e.getMaThuoc());
//
//        txtTenThuoc.setText(e.getTenThuoc());
//        txtThanhPhan.setText(e.getThanhPhan());
//        txtDonGia.setText(String.valueOf(e.getDonGia()));
//        txtDonGia.setText(Formatter.FormatVND(e.getDonGia()));

    }
    private void ketQuaTimKiem() {
//        modal.setRowCount(0);
//        String search = txtSearch.getText().toLowerCase().trim();
//        List<Thuoc> listsearch = getSearchTable(search);
//        loadTable(listsearch);
    }

//    private PhieuDatThuoc getInputPhieuDat() {
//        String idHD = txtMaPDT.getText();
//        Timestamp thoiGian = new Timestamp(System.currentTimeMillis());
//        NhanVien nhanVien = tklogin.getNhanVien();
//        KhachHang khachHang = KH_DAO.selectBySdt(txtsdt.getText());
//        Thue thue;
//	      if ("0.0".equals(txtThue.getText())) {
//	          thue = THUE_DAO.selectById("NO");
//	      } else {
//	          thue = THUE_DAO.selectById("GTGT");
//	      }
//
//
//        return new PhieuDatThuoc(idHD, thoiGian, nhanVien, khachHang,false);
//    }



//    private ChiTietPhieuDatThuoc getInputChiTietPhieuDat() {
//        PhieuDatThuoc phieudat = getInputPhieuDat();
//        Thuoc thuoc = THUOC_CON.selectById(txtMaThuoc.getText());
//        int soLuong = Integer.parseInt(txtSoLuong.getText());
//
//        double donGia = thuoc.getDonGia();
//        KhuyenMai km = null;
//
//        if (thuoc.getKhuyenMai() != null) {
//            String makm = thuoc.getKhuyenMai().getMaKM();
//            km = KM_DAO.selectById(makm);
//            if(km.getTimeEnd()!=null &&  km.getTimeEnd().after(new Timestamp(System.currentTimeMillis())) &&  km.getTimeStart().before(new Timestamp(System.currentTimeMillis()))) {
//            	donGia = thuoc.getDonGia()*(1-km.getPhanTram());
//            }
//        }
//
//
//        return new ChiTietPhieuDatThuoc(phieudat, thuoc, soLuong, donGia);
//    }

    private void btnAddCartActionPerformed() {
//    		if(isValidChiTietPhieuDat()) {
//
//	            ChiTietPhieuDatThuoc cthd = getInputChiTietPhieuDat();
//
//	            listCTHD.add(cthd);
//	            loadTableCart(listCTHD);
//
//
//	            Thuoc thuoc = THUOC_CON.selectById(txtMaThuoc.getText());
//	            int updatedSoLuongTon = thuoc.getSoLuongTon() - cthd.getSoLuong();
//	            THUOC_CON.updateSoLuongTon(thuoc, updatedSoLuongTon);
//	            loadTable(THUOC_CON.selectAll());
//
//	            txtSoLuong.setText("");
//    		}
        	
        }
    private void btnSearchActionPerformed() {
//	   	 KhachHang kh = null;
//	   	if(Validation.isPhoneNumber(txtsdt.getText())) {
//	   		  kh = new KhachHangDAO().selectBySdt(txtsdt.getText());
//	   	}else {
//	   		MessageDialog.error(this, "Kiểm tra lại số điện thoại!");
//	   		return;
//	   	}
//
//
//	       if (kh == null) {
//	           MessageDialog.error(this, "Không tìm thấy khách hàng!");
//	           txtHoTenKH.setText("");
//
//	           txtHoTenKH.setEnabled(true);
//
//	       } else {
//	           txtHoTenKH.setText(kh.getHoTen());
//
//	           txtHoTenKH.setEnabled(false);
//
//	       }
   }
    
    private void btnDeleteCartItemActionPerformed() {
//        if (MessageDialog.confirm(this, "Bạn có chắc muốc xóa khỏi giỏ hàng?", "Xóa thuốc khỏi giỏ hàng")) {
//            if (listCTHD.isEmpty()) {
//                MessageDialog.error(this, "Không có sản phẩm trong giỏ hàng!");
//                return;
//            }
//
//            ChiTietPhieuDatThuoc cthd = listCTHD.get(tableCart.getSelectedRow());
//            listCTHD.remove(tableCart.getSelectedRow());
//            loadTableCart(listCTHD);
//
//            // Update số lượng tồn
//            Thuoc thuocCTHD = cthd.getThuoc();
//            Thuoc thuoc = listThuoc.get(listThuoc.indexOf(thuocCTHD));
//            int updatedSoLuongTon = thuoc.getSoLuongTon() + cthd.getSoLuong();
//            THUOC_CON.updateSoLuongTon(thuoc, updatedSoLuongTon);
//            loadTable(THUOC_CON.selectAll());
//        }
    }
//    public List<Thuoc> getSearchTable(String text) {
//        text = text.toLowerCase();
//        List result = new ArrayList<Thuoc>();
//               for (Thuoc e : THUOC_CON.selectAll()) {
//                    if (e.getMaThuoc().toLowerCase().contains(text)
//                            || e.getTenThuoc().toLowerCase().contains(text)
//                            || e.getDanhMuc().getTen().toLowerCase().contains(text)
//                            || e.getDonViTinh().toLowerCase().contains(text)
//                    		|| e.getNhaSanXuat().getTen().toLowerCase().contains(text)) {
//                        result.add(e);
//                    }
//                }
//
//        return result;
//    }

 
    private void btnReloadActionPerformed() {

    }
    private void btnHuyActionPerformed() {

    }
    private void deteleAllTxt() {
//    	txtDonGia.setText("");
//    	txtHinhAnh.setText("");
//    	txtHoTenKH.setText("");
//    	txtMaThuoc.setText("");
//    	txtsdt.setText("");
//    	txtSearch.setText("");
//    	txtSoLuong.setText("");
//    	txtTenThuoc.setText("");
//    	txtThanhPhan.setText("");
//    	txtThue.setText("");
//    	txttongHD.setText("");
//    	txtTongTien.setText("");
//    	txtHinhAnh.setText("");;
//    	txtHinhAnh.setIcon(null);
//
//           loadTable(THUOC_CON.selectAll());
//
//           if(modalCart.getColumnCount()!=0) {
//        	   modalCart.setRowCount(0);
//           }
           
    }
    private void btnThanhToanActionPerformed() {
//        try {
//			if(modalCart.getColumnCount()!=0 &&isValidPhieuDat()) {
//				if (MessageDialog.confirm(this, "Xác nhận thanh toán?", "Lập hóa đơn")) {
//			        PhieuDatThuoc hd = getInputPhieuDat();
//			        if (hd != null) {
//			            DDT_DAO.create(hd);
//			            CTDDT_DAO.create(listCTHD);
//			            loadTableCart(listCTHD);
//			            MessageDialog.info(this, "Lập phiếu đặt thành công!");
//
//			            Container parent = this.getParent();
//			            if (parent != null) {
//			                parent.remove(this); // Xóa panel hiện tại
//			                parent.add(new gui_DatThuoc(tklogin)); // Thêm panel mới
//			                parent.revalidate(); // Cập nhật lại giao diện
//			                parent.repaint();    // Vẽ lại giao diện
//			            } else {
//			                System.err.println("Lỗi: Không tìm thấy container cha.");
//			            }
//
//			        } else {
//			            MessageDialog.error(this, "Lỗi: Không thể lập phiếu đặt.");
//			        }
//			    }
//			}else {
//				  MessageDialog.error(this, "Chưa thêm thuốc");
//			}
//		} catch (NumberFormatException e) {
//			MessageDialog.error(this, "Lỗi thanh toán , vui lòng kiểm tra lại.");
//		}
    }

    public void loadDanhMucThuoc() {
//        for (DanhMuc a: DANHMUC_DAO.selectAll()) {
//        	cboxSearch.addItem(a.getTen());
//        }
           
    }
    public void loadTableTheoDanhMuc() {
//
//        String selectedDanhMuc = cboxSearch.getSelectedItem().toString();
//        loadTable(THUOC_CON.selectAll());
//        List<Thuoc> filList = new ArrayList<>();
//        if (selectedDanhMuc.equals("Tất cả")) {
//            filList = listThuoc;
//        } else {
//            for (Thuoc e : listThuoc) {
//                if (e.getDanhMuc().getTen().equals(selectedDanhMuc)) {
//                    filList.add(e);
//                }
//            }
//        }
//
//        loadTable(filList);
    }




    private void formatTxt() {
        String txtDonGiaFormat = Formatter.FormatVND(Double.parseDouble(txtDonGia.getText()));
        txtDonGia.setText(txtDonGiaFormat);
        String txtTongFormat = Formatter.FormatVND(Double.parseDouble(txtTongTien.getText()));
        txtTongTien.setText(txtTongFormat);
    }
    
    public void setPanel(JPanel newPanel) {
        getRootPane().removeAll();
        getRootPane().add(newPanel);
        revalidate();
        repaint();
    }
}
    


 


