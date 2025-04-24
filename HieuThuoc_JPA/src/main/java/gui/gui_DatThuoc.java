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
import entity.*;
import other.Formatter;
import other.MessageDialog;
import other.RandomMa;
import other.Validation;
import service.DatThuocSevice;
import service.KhachHangService;
import service.PhieuDatThuocService;
import service.ThuocService;
import service.impl.DatThuocServiceImpl;
import service.impl.KhachHangServiceImpl;
import service.impl.PhieuDatThuocServiceImpl;
import service.impl.ThuocServiceImpl;


import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.io.File;
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

	private List<ChiTietPhieuDatThuoc> listCTPDT = new ArrayList<>();
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


	private void initializeServices(){
		try {
			THUOC_SEVICE = new ThuocServiceImpl(new ThuocDAOImpl(), new DanhMucDAOImpl());
			KHACH_HANG_SEVICE = new KhachHangServiceImpl(new KhachHangDAOImpl());
			DAT_THUOC_SEVICE = new DatThuocServiceImpl(new PhieuDatThuocDAOImpl(), new ThuocDAOImpl(), new KhachHangDAOImpl(), new ChiTietPhieuDatThuocDAOImpl());
			PHEU_DAT_THUOC_SEVICE = new PhieuDatThuocServiceImpl(new PhieuDatThuocDAOImpl(), new ChiTietPhieuDatThuocDAOImpl());
			loadDanhMucThuoc();
			//formatTxt();
		} catch (Exception e) {
			MessageDialog.error(this, "Khởi tạo dịch vụ thất bại: " + e.getMessage());
			e.printStackTrace();
		}

	}


    public gui_DatThuoc(TaiKhoan login) {

		initializeServices();
		System.out.println("DAT_THUOC_SEVICE = " + DAT_THUOC_SEVICE);

		System.out.println("PHIEU_DAT_SEVICE = " + PHEU_DAT_THUOC_SEVICE);


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
    	btnKhachHang.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
//    	        gui_themKhachHang dialog = new gui_themKhachHang();
//    	        dialog.setSize(600, 400);
//    	        dialog.setLocationRelativeTo(null);
//    	        dialog.setVisible(true);

    	    }
    	});


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
				giamGia = 0;
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
		int row = table.getSelectedRow();
		if (row >= 0) {
			String idThuoc = (String) modal.getValueAt(row, 1);
			String defaultImagePath = "/product-image/default-drug.png"; // Khai báo ở phạm vi ngoài try-catch

			try {
				Thuoc e = THUOC_SEVICE.findById(idThuoc);
				if (e != null) {
					String baseResourcePath = "/product-image/";
					String imageName = e.getHinhAnh();
					ImageIcon imageIcon = null;

					if (imageName != null && !imageName.trim().isEmpty()) {
						// Load từ classpath
						java.net.URL imageUrl = getClass().getResource(baseResourcePath + imageName);

						if (imageUrl != null) {
							imageIcon = new ImageIcon(imageUrl);
						} else {
							// Fallback 1: Kiểm tra file system
							File imageFile = new File("src/main/resources/product-image/" + imageName);
							if (imageFile.exists()) {
								imageIcon = new ImageIcon(imageFile.getAbsolutePath());
							} else {
								// Fallback 2: Dùng ảnh mặc định từ classpath
								imageIcon = new ImageIcon(getClass().getResource(defaultImagePath));
							}
						}
					} else {
						imageIcon = new ImageIcon(getClass().getResource(defaultImagePath));
					}

					if (imageIcon != null && imageIcon.getImage() != null) {
						Image scaledImage = imageIcon.getImage()
								.getScaledInstance(txtHinhAnh.getWidth(), txtHinhAnh.getHeight(), Image.SCALE_SMOOTH);
						txtHinhAnh.setIcon(new ImageIcon(scaledImage));
					}

					txtMaThuoc.setText(e.getId());
					txtTenThuoc.setText(e.getTen());
					txtThanhPhan.setText(e.getThanhPhan());
					txtDonGia.setText(Formatter.FormatVND(e.getDonGia()));
				}
			} catch (Exception ex) {
				MessageDialog.error(this, "Lỗi tải thông tin: " + ex.getMessage());
				try {
					ImageIcon defaultIcon = new ImageIcon(getClass().getResource(defaultImagePath));
					txtHinhAnh.setIcon(defaultIcon);
				} catch (Exception e) {
					txtHinhAnh.setIcon(null);
				}
			}
		}

    }
    private void ketQuaTimKiem() {
//        modal.setRowCount(0);
//        String search = txtSearch.getText().toLowerCase().trim();
//        List<Thuoc> listsearch = getSearchTable(search);
//        loadTable(listsearch);
    }

    private PhieuDatThuoc getInputPhieuDat() {
        String idHD = txtMaPDT.getText();
        Timestamp thoiGian = new Timestamp(System.currentTimeMillis());
        NhanVien nhanVien = tklogin.getNhanVien();
		try {
			KhachHang khachHang = KHACH_HANG_SEVICE.getKhachHangBySdt(txtsdt.getText());
			return new PhieuDatThuoc(idHD, thoiGian, nhanVien, khachHang,false);
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
			e.printStackTrace();
			return null;
		}


    }

	public void loadTableCart(List<ChiTietPhieuDatThuoc> list) {
		modalCart = new DefaultTableModel(new Object[][]{}, new String[]{"STT", "Tên thuốc", "Số lượng", "Đơn giá "});

		tableCart.setModel(modalCart);
		modalCart.setRowCount(0);
		listCTPDT = list;
		int stt = 1;
		double sum = 0;
		for (ChiTietPhieuDatThuoc e : listCTPDT) {
			sum = sum + e.getDonGia() * e.getSoLuong();
			modalCart.addRow(new Object[]{
					String.valueOf(stt),
					e.getThuoc().getTen(),
					e.getSoLuong(),
					Formatter.FormatVND(e.getDonGia()),});
			stt++;
		}
		txtTongTien.setText(Formatter.FormatVND(sum));
		txttongHD.setText(Formatter.FormatVND(sum));
	}


    private ChiTietPhieuDatThuoc getInputChiTietPhieuDat() {
        PhieuDatThuoc phieudat = getInputPhieuDat();
		try {
			Thuoc thuoc = THUOC_SEVICE.findById(txtMaThuoc.getText());
			int soLuong = Integer.parseInt(txtSoLuong.getText());
			double donGia = thuoc.getDonGia();
			return new ChiTietPhieuDatThuoc(phieudat, thuoc, soLuong, donGia);
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi lấy thông tin thuốc: " + e.getMessage());
			e.printStackTrace();
			return null;
		}


    }

    private void btnAddCartActionPerformed() {
		if (isValidChiTietPhieuDat()) {

			ChiTietPhieuDatThuoc ctpdt = getInputChiTietPhieuDat();
			if (ctpdt != null) {
				listCTPDT.add(ctpdt);
				loadTableCart(listCTPDT);

				try {
					Thuoc thuoc = THUOC_SEVICE.findById(txtMaThuoc.getText());
					int updatedSoLuongTon = thuoc.getSoLuongTon() - ctpdt.getSoLuong();

					// Cập nhật số lượng tồn
					thuoc.setSoLuongTon(updatedSoLuongTon);
					THUOC_SEVICE.update(thuoc);

					// Làm mới bảng thuốc
					loadTable(THUOC_SEVICE.findAll());

					// Reset số lượng
					txtSoLuong.setText("");
				} catch (Exception e) {
					MessageDialog.error(this, "Lỗi khi cập nhật số lượng thuốc: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
        	
	}

	private boolean isValidPhieuDat() {
		if (listCTPDT.isEmpty()) {
			MessageDialog.warring(this, "Vui lòng chọn sản phẩm!");
			return false;
		}
		if (Validation.isEmpty(txtsdt.getText())) {
			MessageDialog.warring(this, "Vui lòng nhập khách hàng!");
			txtsdt.requestFocus();
			return false;
		}
		if (Validation.isEmpty(txtHoTenKH.getText())) {
			MessageDialog.warring(this, "Số điện thoại sai !");
			txtsdt.requestFocus();
			return false;
		}
		return true;
	}
	private boolean isValidChiTietPhieuDat() {
		if (Validation.isEmpty(txtSoLuong.getText().trim())) {
			MessageDialog.warring(this, "Số lượng không được để trống!");
			txtSoLuong.requestFocus();
			return false;
		} else {
			try {
				Thuoc selectedThuoc = THUOC_SEVICE.findById(txtMaThuoc.getText());
				if (selectedThuoc == null) {
					MessageDialog.warring(this, "Vui lòng chọn sản phẩm");
					return false;
				}
				int soLuongTon = selectedThuoc.getSoLuongTon();
				int sl = Integer.parseInt(txtSoLuong.getText());
				if (sl <= 0) {
					MessageDialog.warring(this, "Số lượng đưa phải > 0");
					txtSoLuong.requestFocus();
					return false;
				} else if (soLuongTon < sl) {
					MessageDialog.warring(this, "Không đủ số lượng!");
					txtSoLuong.requestFocus();
					return false;
				}
			} catch (NumberFormatException e) {
				MessageDialog.warring(this, "Số lượng đưa phải là số!");
				txtSoLuong.requestFocus();
				return false;
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi kiểm tra số lượng thuốc: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		try {
			Thuoc selectedThuoc = THUOC_SEVICE.findById(txtMaThuoc.getText());
			for (ChiTietPhieuDatThuoc cthd : listCTPDT) {
				if (cthd.getThuoc().equals(selectedThuoc)) {
					MessageDialog.warring(this, "Thuốc đã tồn tại trong giỏ hàng!");
					return false;
				}
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi kiểm tra giỏ hàng: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}


    private void btnSearchActionPerformed() {
		try {
			String sdt = txtsdt.getText().trim();
			if (!Validation.isPhoneNumber(sdt)) {
				MessageDialog.error(this, "Số điện thoại không hợp lệ!");
				txtsdt.requestFocus();
				return;
			}

			KhachHang kh = KHACH_HANG_SEVICE.getKhachHangBySdt(sdt);
			if (kh != null) {
				txtHoTenKH.setText(kh.getHoTen());
			} else {
				MessageDialog.warring(this, "Không tìm thấy khách hàng với số điện thoại đã nhập.");
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tìm khách hàng: " + e.getMessage());
			e.printStackTrace();
		}
   }
    
    private void btnDeleteCartItemActionPerformed() {
		if (MessageDialog.confirm(this, "Bạn có chắc muốn xóa khỏi giỏ hàng?", "Xóa thuốc khỏi giỏ hàng")) {
			int selectedRow = tableCart.getSelectedRow();
			if (selectedRow < 0 || selectedRow >= listCTPDT.size()) {
				MessageDialog.warring(this, "Vui lòng chọn thuốc cần xóa!");
				return;
			}

			ChiTietPhieuDatThuoc ctpdt = listCTPDT.remove(selectedRow);
			loadTableCart(listCTPDT);

			try {
				Thuoc thuoc = THUOC_SEVICE.findById(ctpdt.getThuoc().getId());
				thuoc.setSoLuongTon(thuoc.getSoLuongTon() + ctpdt.getSoLuong());
				THUOC_SEVICE.update(thuoc);
				loadTable(THUOC_SEVICE.findAll());
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi cập nhật thuốc: " + e.getMessage());
				e.printStackTrace();
			}
		}
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
		try {
			if (modalCart.getColumnCount() != 0 && isValidPhieuDat()) {
				if (MessageDialog.confirm(this, "Xác nhận thanh toán?", "Lập phiếu đặt thuốc")) {
					PhieuDatThuoc pdt = getInputPhieuDat();
					if (pdt != null) {
						DAT_THUOC_SEVICE.addPhieuDatThuoc(pdt);

						MessageDialog.info(this, "Lập phiếu đặt thuốc thành công!");

						if (MessageDialog.confirm(this, "Bạn có muốn in phiếu đặt thuốc không?", "In phiếu đặt thuốc")) {
//							new other.WritePDF().printHoaDon(hd, listCTHD, 0); // in phiếu đặt chưa làm
						}

						Container parent = this.getParent();
						if (parent != null) {
							parent.remove(this);
							parent.add(new gui_BanThuoc(tklogin));
							parent.revalidate();
							parent.repaint();
						}
					} else {
						MessageDialog.error(this, "Không thể lập phiếu đặt thuốc.");
					}
				}
			} else {
				MessageDialog.error(this, "Kiểm tra lại thông tin phiếu đặt thuốc.");
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi thanh toán: " + e.getMessage());
			e.printStackTrace();
		}
    }

    public void loadDanhMucThuoc() {
        try {
            List<DanhMuc> danhMucList = THUOC_SEVICE.getAllDanhMuc();
            
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cboxSearch.getModel();
            model.removeAllElements();
            model.addElement("Tất cả");
            
            if (danhMucList != null) {
                for (DanhMuc dm : danhMucList) {
                    if (dm != null && dm.getTen() != null) {
                        model.addElement(dm.getTen());
                    }
                }
            }
        } catch (Exception e) {
            MessageDialog.error(this, "Lỗi khi tải danh mục thuốc: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void loadTableTheoDanhMuc() {
        try {
            String selectedDanhMuc = (String) cboxSearch.getSelectedItem();
            if (selectedDanhMuc == null) {
                return;
            }
            
            List<Thuoc> filteredList;
            if (selectedDanhMuc.equals("Tất cả")) {
                filteredList = THUOC_SEVICE.findAll();
            } else {
                filteredList = THUOC_SEVICE.getThuocByTenDanhMuc(selectedDanhMuc);
            }
            
            if (filteredList != null) {
                loadTable(filteredList);
            }
        } catch (Exception e) {
            MessageDialog.error(this, "Lỗi khi tải thuốc theo danh mục: " + e.getMessage());
            e.printStackTrace();
        }
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






