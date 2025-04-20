//package gui;
//
//import dao.*;
//import dao.impl.*;
//import entity.*;
//import other.Formatter;
//import other.MessageDialog;
//import other.RandomMa;
//import other.Validation;
//import service.*;
//import service.impl.*;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Container;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Frame;
//import java.awt.Image;
//import java.awt.SystemColor;
//import java.awt.Window;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JComponent;
//import javax.swing.JDialog;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JSeparator;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.SwingConstants;
//import javax.swing.SwingUtilities;
//import javax.swing.border.EtchedBorder;
//import javax.swing.border.LineBorder;
//import javax.swing.border.MatteBorder;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//
//
//
//public class gui_BanThuoc extends JPanel {
//    private JTextField textField_5;
//    private JTextField textField_6;
//    private JTextField textField_8;
//    private JTextField textField_9;
//    private JTextField textField_10;
//	private JComponent table_right;
//	private JTextField txtMaThuoc;
//	private JTextField txtTenThuoc;
//	private JTextField txtThanhPhan;
//	private JTextField txtDonGia;
//	private JTextField txtSearch;
//	private JTextField txtSoLuong;
//	private JTable table;
//	private JTextField txtMaHoaDon;
//	private JTextField txtsdt;
//	private JTextField txtHoTenKH;
//	private JTextField txtTongTien;
// 	JLabel txtHinhAnh = new JLabel("");
//
//	private DefaultTableModel modal;
//	private DefaultTableModel modalCart;
//	private List<Thuoc> listThuoc = new ArrayList<>();
//	private List<ChiTietHoaDon> listCTHD = new ArrayList<>();
//	private TaiKhoan tklogin;
//	private  ThuocService THUOC_SERVICE;
//	private  KhachHangService KH_SERVICE;
//	private  HoaDonService HD_SERVICE;
//	private  BanThuocService BAN_THUOC_SERVICE; // Tổng hợp các nghiệp vụ bán thuốc
//	private  PhieuDatThuocService DDT_SERVICE;
//
//
//
//	private JTable tableCart;
//	private JComboBox cboxSearch;
//	private JComboBox cbb_DDT;
//	private JTextField txttongHD;
//	private JTextField txtThue;
//	private JTextField txtSdtDDT;
//	private JTextField txtTenDDT;
//	private JTextField txtTimeDDT;
//
//
//
//    public gui_BanThuoc(TaiKhoan login) throws RemoteException {
//		this.tklogin = login;
//		try {
//			Registry registry = LocateRegistry.getRegistry("localhost", 1099); // Thay đổi nếu server chạy trên host/port khác
//			this.THUOC_SERVICE = (ThuocServiceImpl) registry.lookup("ThuocService");
//			this.KH_SERVICE = (KhachHangServiceImpl) registry.lookup("KhachHangService");
//			this.HD_SERVICE = (HoaDonServiceImpl) registry.lookup("HoaDonService");
//			this.BAN_THUOC_SERVICE = (BanThuocServiceImpl) registry.lookup("BanThuocService");
//			this.DDT_SERVICE = (PhieuDatThuocServiceImpl) registry.lookup("PhieuDatThuocService");
//			loadTable(THUOC_SERVICE.findAll());
//			loadDanhMucThuoc();
//			loadDDT();
//			txtMaHoaDon.setText(RandomMa.maHoaDonAuto());
//			txtThue.setText(Formatter.FormatVND(0.05)); // Ví dụ thuế 5%
//			formatTxt();
//		} catch (Exception e) {
//			MessageDialog.error(this, "Không thể kết nối đến server: " + e.getMessage());
//			e.printStackTrace();
//			// Có thể disable các chức năng hoặc hiển thị thông báo lỗi khác
//		}
//
//
//
//
//    	setLayout(null);
//    	setSize(1400,800);
//    	JPanel panel = new JPanel();
//    	panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
//    	panel.setBounds(10, 59, 708, 654);
//    	add(panel);
//    	panel.setLayout(null);
//
//    	JPanel panel_2 = new JPanel();
//    	panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
//    	panel_2.setBackground(SystemColor.activeCaption);
//    	panel_2.setBounds(174, 10, 411, 35);
//    	panel.add(panel_2);
//    	panel_2.setLayout(new BorderLayout(0, 0));
//
//    	JLabel lblNewLabel_1_1_1 = new JLabel("THÔNG TIN THUỐC");
//    	lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
//    	lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
//    	panel_2.add(lblNewLabel_1_1_1);
//    	lblNewLabel_1_1_1.setForeground(Color.BLACK);
//    	lblNewLabel_1_1_1.setBackground(Color.BLACK);
//
//    	JPanel panel_3 = new JPanel();
//    	panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
//    	panel_3.setBounds(20, 55, 210, 176);
//    	panel.add(panel_3);
//    	panel_3.setLayout(null);
//
//
//    	txtHinhAnh.setBounds(10, 10, 190, 161);
//    	panel_3.add(txtHinhAnh);
//
//    	JPanel panel_4 = new JPanel();
//    	panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
//    	panel_4.setBounds(284, 55, 372, 176);
//    	panel.add(panel_4);
//    	panel_4.setLayout(null);
//
//    	JLabel lblNewLabel_2 = new JLabel("MATHUOC:");
//    	lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2.setBounds(10, 10, 84, 27);
//    	panel_4.add(lblNewLabel_2);
//
//    	JLabel lblNewLabel_2_1 = new JLabel("TENTHUOC:");
//    	lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_1.setBounds(10, 47, 84, 27);
//    	panel_4.add(lblNewLabel_2_1);
//
//    	JLabel lblNewLabel_2_2 = new JLabel("THANHPHAN:");
//    	lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_2.setBounds(10, 84, 107, 27);
//    	panel_4.add(lblNewLabel_2_2);
//
//    	JLabel lblNewLabel_2_3 = new JLabel("DONGIA:");
//    	lblNewLabel_2_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_3.setBounds(10, 139, 84, 27);
//    	panel_4.add(lblNewLabel_2_3);
//
//    	txtMaThuoc = new JTextField();
//    	txtMaThuoc.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtMaThuoc.setBounds(128, 16, 215, 19);
//    	panel_4.add(txtMaThuoc);
//    	txtMaThuoc.setColumns(10);
//
//    	txtTenThuoc = new JTextField();
//    	txtTenThuoc.setFont(new Font("Tahoma", Font.BOLD, 10));
//    	txtTenThuoc.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtTenThuoc.setColumns(10);
//    	txtTenThuoc.setBounds(128, 53, 215, 19);
//    	panel_4.add(txtTenThuoc);
//
//    	txtThanhPhan = new JTextField();
//    	txtThanhPhan.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtThanhPhan.setColumns(10);
//    	txtThanhPhan.setBounds(128, 90, 215, 42);
//    	panel_4.add(txtThanhPhan);
//
//    	txtDonGia = new JTextField();
//    	txtDonGia.setFont(new Font("Tahoma", Font.BOLD, 10));
//    	txtDonGia.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtDonGia.setColumns(10);
//    	txtDonGia.setBounds(128, 145, 215, 19);
//    	 txtDonGia.setText("0.0");
//         txtDonGia.setFocusable(false);
//    	panel_4.add(txtDonGia);
//
//    	JPanel panel_5 = new JPanel();
//    	panel_5.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh s\u00E1ch Thu\u1ED1c", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
//    	panel_5.setBounds(21, 332, 647, 182);
//    	panel.add(panel_5);
//    	panel_5.setLayout(null);
//
//    	JScrollPane scrollPane = new JScrollPane();
//
//    	scrollPane.setBounds(20, 24, 603, 148);
//    	panel_5.add(scrollPane);
//
//    	table = new JTable();
//    	table.addMouseListener(new MouseAdapter() {
//    		@Override
//    		public void mouseClicked(MouseEvent e) {
//    			tableMouseClicked();
//    		}
//    	});
//
//
//    	scrollPane.setViewportView(table);
//    	JPanel panel_6 = new JPanel();
//    	panel_6.setBounds(21, 241, 647, 81);
//    	panel.add(panel_6);
//    	panel_6.setLayout(null);
//
//    	 cboxSearch = new JComboBox();
//    	 cboxSearch.addActionListener(new ActionListener() {
//    	 	public void actionPerformed(ActionEvent e) {
//    	 		loadTableTheoDanhMuc();
//    	 	}
//    	 });
//    	 cboxSearch.setBackground(SystemColor.text);
//    	 cboxSearch.setModel(new DefaultComboBoxModel(new String[] {"Tất cả"}));
//    	 cboxSearch.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Lo\u1EA1i Thu\u1ED1c", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
//
//    	cboxSearch.setBounds(10, 24, 160, 42);
//    	panel_6.add(cboxSearch);
//    	//
//
//    	//
//    	txtSearch = new JTextField("");
//    	txtSearch.addKeyListener(new KeyAdapter() {
//    		@Override
//    		public void keyReleased(KeyEvent e) {
//    			ketQuaTimKiem();
//    		}
//    	});
//
//    	txtSearch.setBorder(new TitledBorder(null, "Tra c\u1EE9u", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//    	txtSearch.setDragEnabled(true);
//
//    	txtSearch.setToolTipText("Tra cứu");
//    	txtSearch.setBounds(180, 24, 128, 42);
//    	panel_6.add(txtSearch);
//    	txtSearch.setColumns(10);
//
//    	JSeparator separator = new JSeparator();
//    	separator.setFont(new Font("Arial", Font.PLAIN, 20));
//    	separator.setOrientation(SwingConstants.VERTICAL);
//    	separator.setBounds(375, 16, 2, 55);
//    	panel_6.add(separator);
//
//    	JButton btnAddCart = new JButton();
//    	btnAddCart.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnAddCartActionPerformed();
//    		}
//    	});
//
//    	btnAddCart.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/add-to-cart.png")));
//    	btnAddCart.setText("THÊM");
//    	btnAddCart.setPreferredSize(new Dimension(120, 40));
//    	btnAddCart.setForeground(SystemColor.desktop);
//    	btnAddCart.setFont(new Font("Dialog", Font.BOLD, 16));
//    	btnAddCart.setFocusable(false);
//    	btnAddCart.setFocusPainted(false);
//    	btnAddCart.setBorderPainted(false);
//    	btnAddCart.setBackground(SystemColor.activeCaptionBorder);
//    	btnAddCart.setBounds(520, 24, 117, 36);
//    	panel_6.add(btnAddCart);
//
//    	JButton btnReload = new JButton();
//    	btnReload.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnReloadActionPerformed();
//    		}
//    	});
//    	btnReload.setBackground(SystemColor.activeCaptionBorder);
//    	btnReload.setBounds(318, 24, 47, 42);
//    	panel_6.add(btnReload);
//    	btnReload.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/refresh.png")));
//    	btnReload.setToolTipText("Làm mới");
//    	btnReload.setPreferredSize(new Dimension(40, 40));
//    	btnReload.setHorizontalTextPosition(SwingConstants.CENTER);
//    	btnReload.setFocusable(false);
//    	btnReload.setFocusPainted(false);
//    	btnReload.setBorderPainted(false);
//    	btnReload.setBorder(null);
//
//    	txtSoLuong = new JTextField();
//    	txtSoLuong.setFont(new Font("Tahoma", Font.BOLD, 10));
//    	txtSoLuong.setBorder(new TitledBorder(null, "S\u1ED1 l\u01B0\u1EE3ng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//    	txtSoLuong.setBounds(387, 21, 123, 40);
//    	panel_6.add(txtSoLuong);
//    	txtSoLuong.setColumns(10);
//
//    	JPanel panel_7 = new JPanel();
//    	panel_7.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u0110\u01A1n \u0111\u1EB7t thu\u1ED1c t\u1EEB tr\u01B0\u1EDBc", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
//    	panel_7.setBounds(20, 524, 648, 120);
//    	panel.add(panel_7);
//    	panel_7.setLayout(null);
//
//    	JLabel lbl_DDT = new JLabel("Mã đơn đặt");
//    	lbl_DDT.setBounds(10, 19, 97, 19);
//    	lbl_DDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	panel_7.add(lbl_DDT);
//
//
//    	JButton btnAddCart_1 = new JButton();
//    	btnAddCart_1.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnAddDDT();
//    		}
//    	});
//    	btnAddCart_1.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/add-to-cart.png")));
//    	btnAddCart_1.setText("Xem Chi Tiết");
//    	btnAddCart_1.setPreferredSize(new Dimension(120, 40));
//    	btnAddCart_1.setForeground(SystemColor.desktop);
//    	btnAddCart_1.setFont(new Font("Dialog", Font.BOLD, 16));
//    	btnAddCart_1.setFocusable(false);
//    	btnAddCart_1.setFocusPainted(false);
//    	btnAddCart_1.setBorderPainted(false);
//    	btnAddCart_1.setBackground(SystemColor.activeCaptionBorder);
//    	btnAddCart_1.setBounds(463, 66, 175, 44);
//    	panel_7.add(btnAddCart_1);
//
//    	 cbb_DDT = new JComboBox();
//    	 cbb_DDT.setModel(new DefaultComboBoxModel(new String[] {"Tất cả"}));
//    	 cbb_DDT.setFont(new Font("Tahoma", Font.BOLD, 10));
//
//    	 cbb_DDT.addActionListener(new ActionListener() {
//    	 	public void actionPerformed(ActionEvent e) {
//    	 		selectDDT();
//    	 	}
//
//    	 });
//    	 cbb_DDT.setBackground(SystemColor.text);
//    	 cbb_DDT.setBounds(142, 20, 142, 21);
//    	 panel_7.add(cbb_DDT);
//
//    	 JLabel lbl_DDT_1 = new JLabel("Số điện thoại");
//    	 lbl_DDT_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	 lbl_DDT_1.setBounds(317, 19, 97, 19);
//    	 panel_7.add(lbl_DDT_1);
//
//    	 txtSdtDDT = new JTextField();
//    	 txtSdtDDT.setFont(new Font("Tahoma", Font.BOLD, 10));
//    	 txtSdtDDT.setHorizontalAlignment(SwingConstants.CENTER);
//    	 txtSdtDDT.setColumns(10);
//    	 txtSdtDDT.setBounds(424, 21, 164, 35);
//    	 panel_7.add(txtSdtDDT);
//
//    	 JButton btn_tkimDDT = new JButton();
//    	 btn_tkimDDT.addActionListener(new ActionListener() {
//    	 	public void actionPerformed(ActionEvent e) {
//    	 		btnTKDDT();
//    	 	}
//    	 });
//    	 btn_tkimDDT.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/search.png")));
//    	 btn_tkimDDT.setToolTipText("Tìm");
//    	 btn_tkimDDT.setPreferredSize(new Dimension(40, 40));
//    	 btn_tkimDDT.setHorizontalTextPosition(SwingConstants.CENTER);
//    	 btn_tkimDDT.setFocusable(false);
//    	 btn_tkimDDT.setFocusPainted(false);
//    	 btn_tkimDDT.setBorderPainted(false);
//    	 btn_tkimDDT.setBorder(null);
//    	 btn_tkimDDT.setBackground(SystemColor.activeCaptionBorder);
//    	 btn_tkimDDT.setBounds(598, 19, 40, 37);
//    	 panel_7.add(btn_tkimDDT);
//
//    	 JLabel lbl_DDT_1_1 = new JLabel("Tên khách hàng");
//    	 lbl_DDT_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	 lbl_DDT_1_1.setBounds(10, 56, 110, 19);
//    	 panel_7.add(lbl_DDT_1_1);
//
//    	 txtTenDDT = new JTextField();
//    	 txtTenDDT.setFont(new Font("Tahoma", Font.BOLD, 10));
//    	 txtTenDDT.setHorizontalAlignment(SwingConstants.CENTER);
//    	 txtTenDDT.setColumns(10);
//    	 txtTenDDT.setBounds(142, 58, 142, 19);
//    	 panel_7.add(txtTenDDT);
//
//    	 JLabel lbl_DDT_1_1_1 = new JLabel("Thời gian đặt");
//    	 lbl_DDT_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	 lbl_DDT_1_1_1.setBounds(10, 91, 110, 19);
//    	 panel_7.add(lbl_DDT_1_1_1);
//
//    	 txtTimeDDT = new JTextField();
//    	 txtTimeDDT.setFont(new Font("Tahoma", Font.BOLD, 10));
//    	 txtTimeDDT.setHorizontalAlignment(SwingConstants.CENTER);
//    	 txtTimeDDT.setColumns(10);
//    	 txtTimeDDT.setBounds(142, 91, 142, 19);
//    	 panel_7.add(txtTimeDDT);
//
//    	JPanel panel_1 = new JPanel();
//    	panel_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
//    	panel_1.setBounds(744, 59, 646, 654);
//    	add(panel_1);
//    	panel_1.setLayout(null);
//
//    	JPanel panel_2_1 = new JPanel();
//    	panel_2_1.setBackground(SystemColor.activeCaption);
//    	panel_2_1.setBounds(142, 10, 411, 35);
//    	panel_1.add(panel_2_1);
//
//    	JLabel lblNewLabel_1_1_1_1 = new JLabel("CHI TIẾT HÓA ĐƠN");
//    	lblNewLabel_1_1_1_1.setForeground(Color.BLACK);
//    	lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
//    	lblNewLabel_1_1_1_1.setBackground(Color.BLACK);
//    	panel_2_1.add(lblNewLabel_1_1_1_1);
//
//    	JPanel panel_5_1 = new JPanel();
//    	panel_5_1.setLayout(null);
//    	panel_5_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Chi Ti\u1EBFt", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
//    	panel_5_1.setBounds(42, 55, 594, 202);
//    	panel_1.add(panel_5_1);
//
//    	JScrollPane scrollPane_1 = new JScrollPane();
//    	scrollPane_1.setBounds(10, 25, 574, 121);
//    	panel_5_1.add(scrollPane_1);
//
//    	tableCart = new JTable();
//    	tableCart.setModel(new DefaultTableModel(new Object[][] {}, new String[] {"STT", "Tên thuốc", "Số lượng", "Đơn giá"}));
//
//
//    	scrollPane_1.setViewportView(tableCart);
//
//    	JPanel panel_8 = new JPanel();
//    	panel_8.setBounds(471, 151, 113, 41);
//    	panel_5_1.add(panel_8);
//    	panel_8.setLayout(null);
//
//    	JButton btnXoa = new JButton();
//    	btnXoa.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/delete.png")));
//    	btnXoa.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnDeleteCartItemActionPerformed();
//    		}
//    	});
//    	btnXoa.setText("XÓA");
//    	btnXoa.setPreferredSize(new Dimension(120, 40));
//    	btnXoa.setForeground(SystemColor.desktop);
//    	btnXoa.setFont(new Font("Dialog", Font.BOLD, 16));
//    	btnXoa.setFocusable(false);
//    	btnXoa.setFocusPainted(false);
//    	btnXoa.setBorderPainted(false);
//    	btnXoa.setBackground(SystemColor.activeCaptionBorder);
//    	btnXoa.setBounds(0, 0, 113, 41);
//    	panel_8.add(btnXoa);
//
//    	JPanel panel_2_1_1 = new JPanel();
//    	panel_2_1_1.setBackground(SystemColor.activeCaption);
//    	panel_2_1_1.setBounds(142, 271, 411, 35);
//    	panel_1.add(panel_2_1_1);
//
//    	JLabel lblNewLabel_1_1_1_1_1 = new JLabel("HÓA ĐƠN");
//    	lblNewLabel_1_1_1_1_1.setForeground(Color.BLACK);
//    	lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
//    	lblNewLabel_1_1_1_1_1.setBackground(Color.BLACK);
//    	panel_2_1_1.add(lblNewLabel_1_1_1_1_1);
//
//    	JPanel panel_4_1 = new JPanel();
//    	panel_4_1.setLayout(null);
//    	panel_4_1.setBorder(new LineBorder(new Color(0, 0, 0)));
//    	panel_4_1.setBounds(42, 316, 594, 328);
//    	panel_1.add(panel_4_1);
//
//    	JLabel lblNewLabel_2_4 = new JLabel("MAHD");
//    	lblNewLabel_2_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_4.setBounds(33, 14, 84, 27);
//    	panel_4_1.add(lblNewLabel_2_4);
//
//    	JLabel lblNewLabel_2_1_1 = new JLabel("SDT");
//    	lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_1_1.setBounds(33, 54, 84, 27);
//    	panel_4_1.add(lblNewLabel_2_1_1);
//
//    	JLabel lblNewLabel_2_2_1 = new JLabel("TENKH");
//    	lblNewLabel_2_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_2_1.setBounds(33, 98, 107, 27);
//    	panel_4_1.add(lblNewLabel_2_2_1);
//
//    	JLabel lblNewLabel_2_3_1 = new JLabel("TỔNG TIỀN");
//    	lblNewLabel_2_3_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_3_1.setBounds(33, 171, 84, 27);
//    	panel_4_1.add(lblNewLabel_2_3_1);
//
//    	txtMaHoaDon = new JTextField();
//    	txtMaHoaDon.setEditable(false);
//    	txtMaHoaDon.setBackground(new Color(255, 255, 255));
//    	txtMaHoaDon.setFont(new Font("Arial", Font.BOLD, 15));
//    	txtMaHoaDon.setColumns(10);
//    	txtMaHoaDon.setBounds(128, 16, 348, 27);
//
//    	// call RamDom maHD
//
//    	txtMaHoaDon.setEnabled(false);
//    	panel_4_1.add(txtMaHoaDon);
//
//    	txtsdt = new JTextField();
//    	txtsdt.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtsdt.setFont(new Font("Arial", Font.BOLD, 15));
//    	txtsdt.setColumns(10);
//    	txtsdt.setBounds(128, 53, 348, 34);
//    	panel_4_1.add(txtsdt);
//
//    	txtHoTenKH = new JTextField();
//    	txtHoTenKH.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtHoTenKH.setFont(new Font("Arial", Font.BOLD, 15));
//    	txtHoTenKH.setColumns(10);
//    	txtHoTenKH.setBounds(127, 97, 349, 34);
//    	panel_4_1.add(txtHoTenKH);
//
//    	txtTongTien = new JTextField("0.0");
//    	txtTongTien.setHorizontalAlignment(SwingConstants.CENTER);
//    	txtTongTien.setFont(new Font("Arial", Font.BOLD, 15));
//
//    	txtTongTien.setFocusable(false);
//    	txtTongTien.setColumns(10);
//    	txtTongTien.setBounds(127, 168, 186, 34);
//    	panel_4_1.add(txtTongTien);
//
//    	JLabel lblNewLabel_3 = new JLabel("----------------------------------------------------------------------------------");
//    	lblNewLabel_3.setBounds(128, 145, 328, 13);
//    	panel_4_1.add(lblNewLabel_3);
//
//    	JButton btn_tkim = new JButton();
//    	btn_tkim.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnSearchActionPerformed();
//
//    		}
//    	});
//    	btn_tkim.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/search.png")));
//    	btn_tkim.setToolTipText("Tìm");
//    	btn_tkim.setPreferredSize(new Dimension(40, 40));
//    	btn_tkim.setHorizontalTextPosition(SwingConstants.CENTER);
//    	btn_tkim.setFocusable(false);
//    	btn_tkim.setFocusPainted(false);
//    	btn_tkim.setBorderPainted(false);
//    	btn_tkim.setBorder(null);
//    	btn_tkim.setBackground(SystemColor.activeCaptionBorder);
//    	btn_tkim.setBounds(499, 47, 40, 40);
//    	panel_4_1.add(btn_tkim);
//
//    	JButton btnKhachHang = new JButton();
//    	btnKhachHang.addActionListener(new ActionListener() {
//    	    public void actionPerformed(ActionEvent e) {
////    	        gui_themKhachHang dialog = new gui_themKhachHang();
////    	       dialog.setSize(600, 400);
////    	        dialog.setLocationRelativeTo(null);
////    	        dialog.setVisible(true);
////
//
//    	    }
//    	});
//
//
//    	btnKhachHang.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/user.png")));
//    	btnKhachHang.setToolTipText("Xem ");
//    	btnKhachHang.setPreferredSize(new Dimension(40, 40));
//    	btnKhachHang.setHorizontalTextPosition(SwingConstants.CENTER);
//    	btnKhachHang.setFocusable(false);
//    	btnKhachHang.setFocusPainted(false);
//    	btnKhachHang.setBorderPainted(false);
//    	btnKhachHang.setBorder(null);
//    	btnKhachHang.setBackground(SystemColor.activeCaptionBorder);
//    	btnKhachHang.setBounds(499, 98, 40, 40);
//    	panel_4_1.add(btnKhachHang);
//
//    	JLabel lblNewLabel_2_3_1_1 = new JLabel("VAT");
//    	lblNewLabel_2_3_1_1.setHorizontalAlignment(SwingConstants.CENTER);
//    	lblNewLabel_2_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_3_1_1.setBounds(304, 171, 69, 27);
//    	panel_4_1.add(lblNewLabel_2_3_1_1);
//
//    	JButton btnLuuhd = new JButton();
//    	btnLuuhd.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/bill.png")));
//    	btnLuuhd.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnThanhToanActionPerformed();
//    		}
//    	});
//    	btnLuuhd.setText("THANH TOÁN");
//    	btnLuuhd.setPreferredSize(new Dimension(120, 40));
//    	btnLuuhd.setForeground(new Color(0, 0, 0));
//    	btnLuuhd.setFont(new Font("Dialog", Font.BOLD, 16));
//    	btnLuuhd.setFocusable(false);
//    	btnLuuhd.setFocusPainted(false);
//    	btnLuuhd.setBorderPainted(false);
//    	btnLuuhd.setBackground(SystemColor.activeCaptionBorder);
//    	btnLuuhd.setBounds(290, 271, 186, 44);
//    	panel_4_1.add(btnLuuhd);
//
//    	JLabel lblNewLabel_2_3_1_2 = new JLabel("TỔNG THANH TOÁN");
//    	lblNewLabel_2_3_1_2.setVerticalAlignment(SwingConstants.BOTTOM);
//    	lblNewLabel_2_3_1_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
//    	lblNewLabel_2_3_1_2.setBounds(33, 222, 172, 27);
//    	panel_4_1.add(lblNewLabel_2_3_1_2);
//
//    	txttongHD = new JTextField("");
//    	txttongHD.setHorizontalAlignment(SwingConstants.CENTER);
//    	txttongHD.setFont(new Font("Arial", Font.BOLD, 15));
//    	txttongHD.setColumns(10);
//    	txttongHD.setBounds(215, 212, 261, 34);
//    	panel_4_1.add(txttongHD);
//
//    	txtThue = new JTextField("");
//    	txtThue.setFont(new Font("Arial", Font.BOLD, 15));
//    	txtThue.setColumns(10);
//    	txtThue.setBounds(373, 168, 103, 34);
//    	panel_4_1.add(txtThue);
//
//    	JLabel lblNewLabel = new JLabel("BÁN THUỐC");
//    	lblNewLabel.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/bill.png")));
//    	lblNewLabel.setForeground(new Color(0, 0, 0));
//    	lblNewLabel.setBackground(Color.GREEN);
//    	lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 35));
//    	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//    	lblNewLabel.setBounds(581, 10, 296, 39);
//    	add(lblNewLabel);
//        //
//    	//
//    	txtMaHoaDon.setText(RandomMa.maHoaDonAuto());
//
//    	JButton btnHuyHD = new JButton();
//    	btnHuyHD.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/return.png")));
//    	btnHuyHD.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			btnHuyActionPerformed();
//    		}
//
//
//    	});
//    	btnHuyHD.setText("     HỦY ");
//    	btnHuyHD.setPreferredSize(new Dimension(120, 40));
//    	btnHuyHD.setForeground(new Color(255, 0, 0));
//    	btnHuyHD.setFont(new Font("Dialog", Font.BOLD, 16));
//    	btnHuyHD.setFocusable(false);
//    	btnHuyHD.setFocusPainted(false);
//    	btnHuyHD.setBorderPainted(false);
//    	btnHuyHD.setBackground(SystemColor.activeCaptionBorder);
//    	btnHuyHD.setBounds(33, 271, 150, 44);
//    	panel_4_1.add(btnHuyHD);
//    	//
//      	loadDanhMucThuoc();
//    	loadTable(listThuoc);
//    	loadDDT();
//
//
//
//    }
//
//	public void loadTable(List<Thuoc> list) {
//		String[] header = new String[]{"STT", "Mã thuốc", "Tên thuốc", "Danh mục", "Nhà Sản Xuất", "Đơn vị tính", "Số lượng tồn", "Đơn giá", "Giảm giá"};
//		modal = new DefaultTableModel();
//		modal.setColumnIdentifiers(header);
//		table.setModel(modal);
//		modal.setRowCount(0);
//		listThuoc = list;
//		int stt = 1;
//		double giamGia;
//		try {
//			for (Thuoc e : listThuoc) {
//				giamGia = 0; // Tạm thời để 0, cần xử lý logic khuyến mãi nếu có
//				if (e.getHanSuDung().after(new Date(System.currentTimeMillis()))) {
//					modal.addRow(new Object[]{String.valueOf(stt), e.getId(), e.getTen(), e.getDanhMuc().getTen(),
//							e.getNhaSanXuat().getTen(), e.getDonViTinh(), e.getSoLuongTon(), Formatter.FormatVND(e.getDonGia()), Formatter.formatPercentage(giamGia)});
//					stt++;
//				}
//			}
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi tải dữ liệu thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void loadTableCart(List<ChiTietHoaDon> list) {
//		modalCart = new DefaultTableModel(new Object[][]{}, new String[]{"STT", "Tên thuốc", "Số lượng", "Đơn giá đã giảm"});
//		tableCart.setModel(modalCart);
//		modalCart.setRowCount(0);
//		listCTHD = list;
//		int stt = 1;
//		double sum = 0;
//		for (ChiTietHoaDon e : listCTHD) {
//			sum = sum + e.getDonGia() * e.getSoLuong();
//			modalCart.addRow(new Object[]{
//					String.valueOf(stt),
//					e.getThuoc().getTen(),
//					e.getSoLuong(),
//					Formatter.FormatVND(e.getDonGia()),});
//			stt++;
//		}
//		txtTongTien.setText(Formatter.FormatVND(sum));
//		txttongHD.setText(Formatter.FormatVND(sum));
//	}
//
//	public void btnAddDDT() {
//		if (cbb_DDT.getSelectedItem() == null || cbb_DDT.getSelectedItem().equals("KHONGCO")) {
//			MessageDialog.error(this, "Không có đơn đặt thuốc nào để thêm.");
//			return;
//		}
//		String selectedMaPDT = (String) cbb_DDT.getSelectedItem();
//		try {
//			PhieuDatThuoc selectedPDT = DDT_SERVICE.findById(selectedMaPDT);
//			if (selectedPDT == null) {
//				MessageDialog.error(this, "Phiếu đặt thuốc không tồn tại.");
//				return;
//			}
//
//			List<ChiTietPhieuDatThuoc> chiTietPhieuDatList = DDT_SERVICE.findChiTietByPhieuDatThuoc(selectedPDT);
//			if (chiTietPhieuDatList == null || chiTietPhieuDatList.isEmpty()) {
//				MessageDialog.error(this, "Không có chi tiết nào trong đơn đặt thuốc.");
//				return;
//			}
//
//			List<ChiTietHoaDon> chiTietHoaDonList = convertToChiTietHoaDon(chiTietPhieuDatList);
//
//			// Hiển thị thông tin khách hàng
//			KhachHang khachHang = KH_SERVICE.findById(selectedPDT.getKhachHang().getId());
//			if (khachHang != null) {
//				txtsdt.setText(khachHang.getSoDienThoai());
//				txtHoTenKH.setText(khachHang.getHoTen());
//			} else {
//				MessageDialog.warring(this, "Không tìm thấy thông tin khách hàng cho đơn đặt thuốc này.");
//			}
//
//			tableCart.removeAll();
//			loadTableCart(chiTietHoaDonList);
//			DDT_SERVICE.updateTrangThai(selectedMaPDT, true); // Cập nhật trạng thái đã xử lý
//			loadDDT(); // Reload lại danh sách đơn đặt thuốc
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi xử lý đơn đặt thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	private List<ChiTietHoaDon> convertToChiTietHoaDon(List<ChiTietPhieuDatThuoc> arr) {
//		List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
//		for (ChiTietPhieuDatThuoc item : arr) {
//			ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
//			chiTietHoaDon.setHoaDon(new HoaDon(txtMaHoaDon.getText()));
//			chiTietHoaDon.setThuoc(item.getThuoc());
//			chiTietHoaDon.setSoLuong(item.getSoLuong());
//			chiTietHoaDon.setDonGia(item.getDonGia());
//			chiTietHoaDonList.add(chiTietHoaDon);
//		}
//		return chiTietHoaDonList;
//	}
//
//	private void tableMouseClicked() {
//		int row = table.getSelectedRow();
//		if (row >= 0) {
//			String idThuoc = modal.getValueAt(row, 1).toString();
//			try {
//				Thuoc e = THUOC_SERVICE.findById(idThuoc);
//				if (e != null) {
//					ImageIcon imageIcon = new ImageIcon(
//							new ImageIcon(e.getHinhAnh()).getImage().getScaledInstance(txtHinhAnh.getWidth(), txtHinhAnh.getHeight(), Image.SCALE_SMOOTH));
//					txtHinhAnh.setIcon(imageIcon);
//
//					txtMaThuoc.setText(e.getId());
//					txtTenThuoc.setText(e.getTen());
//					txtThanhPhan.setText(e.getThanhPhan());
//					txtDonGia.setText(Formatter.FormatVND(e.getDonGia()));
//				}
//			} catch (Exception e) {
//				MessageDialog.error(this, "Lỗi khi lấy thông tin thuốc: " + e.getMessage());
//				e.printStackTrace();
//			}
//		}
//	}
//	private void ketQuaTimKiem() {
//		modal.setRowCount(0);
//		String search = txtSearch.getText().toLowerCase().trim();
//		try {
//			List<Thuoc> listsearch = THUOC_SERVICE.searchThuoc(search);
//			loadTable(listsearch);
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi tìm kiếm thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	private HoaDon getInputHoaDon() {
//		String idHD = txtMaHoaDon.getText();
//		Timestamp thoiGian = new Timestamp(System.currentTimeMillis());
//		NhanVien nhanVien = tklogin.getNhanVien();
//		try {
//			KhachHang khachHang = KH_SERVICE.findBySdt(txtsdt.getText());
//			return new HoaDon(idHD, thoiGian, nhanVien, khachHang);
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private boolean isValidHoaDon() {
//		if (listCTHD.isEmpty()) {
//			MessageDialog.warring(this, "Vui lòng chọn sản phẩm!");
//			return false;
//		}
//		if (Validation.isEmpty(txtsdt.getText())) {
//			MessageDialog.warring(this, "Vui lòng nhập khách hàng!");
//			txtsdt.requestFocus();
//			return false;
//		}
//		if (Validation.isEmpty(txtHoTenKH.getText())) {
//			MessageDialog.warring(this, "Số điện thoại sai !");
//			txtsdt.requestFocus();
//			return false;
//		}
//		return true;
//	}
//
//	private boolean isValidChiTietHoaDon() {
//		if (Validation.isEmpty(txtSoLuong.getText().trim())) {
//			MessageDialog.warring(this, "Số lượng không được để trống!");
//			txtSoLuong.requestFocus();
//			return false;
//		} else {
//			try {
//				Thuoc selectedThuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
//				if (selectedThuoc == null) {
//					MessageDialog.warring(this, "Vui lòng chọn sản phẩm");
//					return false;
//				}
//				int soLuongTon = selectedThuoc.getSoLuongTon();
//				int sl = Integer.parseInt(txtSoLuong.getText());
//				if (sl <= 0) {
//					MessageDialog.warring(this, "Số lượng đưa phải > 0");
//					txtSoLuong.requestFocus();
//					return false;
//				} else if (soLuongTon < sl) {
//					MessageDialog.warring(this, "Không đủ số lượng!");
//					txtSoLuong.requestFocus();
//					return false;
//				}
//			} catch (NumberFormatException e) {
//				MessageDialog.warring(this, "Số lượng đưa phải là số!");
//				txtSoLuong.requestFocus();
//				return false;
//			} catch (Exception e) {
//				MessageDialog.error(this, "Lỗi khi kiểm tra số lượng thuốc: " + e.getMessage());
//				e.printStackTrace();
//				return false;
//			}
//		}
//
//		try {
//			Thuoc selectedThuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
//			for (ChiTietHoaDon cthd : listCTHD) {
//				if (cthd.getThuoc().equals(selectedThuoc)) {
//					MessageDialog.warring(this, "Thuốc đã tồn tại trong giỏ hàng!");
//					return false;
//				}
//			}
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi kiểm tra giỏ hàng: " + e.getMessage());
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	private ChiTietHoaDon getInputChiTietHoaDon() {
//		HoaDon hoaDon = getInputHoaDon();
//		try {
//			Thuoc thuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
//			int soLuong = Integer.parseInt(txtSoLuong.getText());
//			double donGia = thuoc.getDonGia();
//			return new ChiTietHoaDon(hoaDon, thuoc, soLuong, donGia);
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi lấy thông tin thuốc: " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private void btnAddCartActionPerformed() {
//		if (isValidChiTietHoaDon()) {
//			ChiTietHoaDon cthd = getInputChiTietHoaDon();
//			if (cthd != null) {
//				listCTHD.add(cthd);
//				loadTableCart(listCTHD);
//
//				try {
//					Thuoc thuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
//					int updatedSoLuongTon = thuoc.getSoLuongTon() - cthd.getSoLuong();
//					THUOC_SERVICE.update(new Thuoc(thuoc.getId(), thuoc.getTen(), thuoc.getDonViTinh(), thuoc.getGiaBan(), updatedSoLuongTon, thuoc.getHanSuDung(), thuoc.getHinhAnh(), thuoc.getThanhPhan(), thuoc.getCongDung(), thuoc.getDanhMuc(), thuoc.getNhaSanXuat(), thuoc.getKhuyenMai()));
//					loadTable(THUOC_SERVICE.findAll());
//					txtSoLuong.setText("");
//				} catch (Exception e) {
//					MessageDialog.error(this, "Lỗi khi cập nhật số lượng thuốc: " + e.getMessage());
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	private void btnSearchActionPerformed() {
//		try {
//			KhachHang kh = null;
//			if (Validation.isPhoneNumber(txtsdt.getText())) {
//				kh = KH_SERVICE.findBySdt(txtsdt.getText());
//			} else {
//				MessageDialog.error(this, "Kiểm tra lại số điện thoại");
//				return;
//			}
//
//			int selectedRow = tableCart.getSelectedRow();
//			if (selectedRow == -1) {
//				MessageDialog.warring(this, "Vui lòng chọn sản phẩm để xóa.");
//				return;
//			}
//
//			ChiTietHoaDon cthdToRemove = listCTHD.get(selectedRow);
//			listCTHD.remove(cthdToRemove);
//			loadTableCart(listCTHD);
//
//			try {
//				Thuoc thuoc = THUOC_SERVICE.findById(cthdToRemove.getThuoc().getId());
//				int updatedSoLuongTon = thuoc.getSoLuongTon() + cthdToRemove.getSoLuong();
//				THUOC_SERVICE.update(new Thuoc(thuoc.getId(), thuoc.getTen(), thuoc.getDonViTinh(), thuoc.getGiaBan(), updatedSoLuongTon, thuoc.getHanSuDung(), thuoc.getHinhAnh(), thuoc.getThanhPhan(), thuoc.getCongDung(), thuoc.getDanhMuc(), thuoc.getNhaSanXuat(), thuoc.getKhuyenMai()));
//				loadTable(THUOC_SERVICE.findAll());
//			} catch (Exception e) {
//				MessageDialog.error(this, "Lỗi khi cập nhật số lượng thuốc: " + e.getMessage());
//				e.printStackTrace();
//			}
//		}
//	}
//
//    private void btnDeleteCartItemActionPerformed() {
//        if (MessageDialog.confirm(this, "Bạn có chắc muốc xóa khỏi giỏ hàng?", "Xóa thuốc khỏi giỏ hàng")) {
//            if (listCTHD.isEmpty()) {
//                MessageDialog.error(this, "Không có sản phẩm trong giỏ hàng!");
//                return;
//            }
//
//            ChiTietHoaDon cthd = listCTHD.get(tableCart.getSelectedRow());
//            listCTHD.remove(tableCart.getSelectedRow());
//            loadTableCart(listCTHD);
//
//            // Update số lượng tồn
//            Thuoc thuocCTHD = cthd.getThuoc();
//            Thuoc thuoc = listThuoc.get(listThuoc.indexOf(thuocCTHD));
//            int updatedSoLuongTon = thuoc.getSoLuongTon() + cthd.getSoLuong();
//            THUOC_DAO.updateSoLuongTon(thuoc, updatedSoLuongTon);
//            loadTable(THUOC_DAO.findAll());
//        }
//    }
//    public List<Thuoc> getSearchTable(String text) {
//        text = text.toLowerCase();
//        List result = new ArrayList<Thuoc>();
//               for (Thuoc e : THUOC_DAO.findAll()) {
//                    if (e.getId().toLowerCase().contains(text)
//                            || e.getTen().toLowerCase().contains(text)
//                            || e.getDanhMuc().getTen().toLowerCase().contains(text)
//                            || e.getDonViTinh().toLowerCase().contains(text)
//                    		|| e.getNhaSanXuat().getTen().toLowerCase().contains(text)) {
//                        result.add(e);
//                    }
//                }
//
//        return result;
//    }
//
//
//    private void btnReloadActionPerformed() {
//
//        txtSearch.setText("");
//        cboxSearch.setSelectedIndex(0);
//        loadTable(THUOC_DAO.findAll());
//    }
//    private void btnHuyActionPerformed() {
//        if (MessageDialog.confirm(this, "Xác nhận hủy hóa đơn?", "Hủy hóa đơn")) {
//            for (ChiTietHoaDon cthd : listCTHD) {
//                Thuoc thuocCTHD = cthd.getThuoc();
//                Thuoc thuoc = listThuoc.get(listThuoc.indexOf(thuocCTHD));
//                int updatedSoLuongTon = thuoc.getSoLuongTon() + cthd.getSoLuong();
//                THUOC_DAO.updateSoLuongTon(thuoc, updatedSoLuongTon);
//            }
//
//           deteleAllTxt();
//
//           Window parentWindow = SwingUtilities.getWindowAncestor(this);
//           if (parentWindow != null) {
//               parentWindow.dispose();
//           }
//           gui_TrangChu trangChu = new gui_TrangChu(tklogin);
//           trangChu.setVisible(true);
//
//
//
//        }
//    }
//    private void deteleAllTxt() {
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
//    	txtHinhAnh.setIcon(null);
//
//           loadTable(THUOC_DAO.findAll());
//           if(modalCart.getColumnCount()!=0) {
//        	   modalCart.setRowCount(0);
//           }
//
//    }
//    private void btnThanhToanActionPerformed() {
//        try {
//			if(modalCart.getColumnCount()!=0 &&isValidHoaDon()) {
//				if (MessageDialog.confirm(this, "Xác nhận thanh toán?", "Lập hóa đơn")) {
//			        HoaDon hd = getInputHoaDon();
//			        if (hd != null) {
//			            HD_DAO.save(hd);
//						for (ChiTietHoaDon a : listCTHD) {
//							CTHD_DAO.save((ChiTietHoaDon) listCTHD);
//			            }
//
//
//			            MessageDialog.info(this, "Lập hóa đơn thành công!");
//
//
//			            if (MessageDialog.confirm(this, "Bạn có muốn in hóa đơn không?", "In hóa đơn")) {
//			                new other.WritePDF().printHoaDon(hd, listCTHD,Double.parseDouble(txtThue.getText().replace("đ", "").trim().replace(",", "").trim()));
//			            }
//			            Container parent = this.getParent();
//			            if (parent != null) {
//			                parent.remove(this); // Xóa panel hiện tại
//			                parent.add(new gui_BanThuoc(tklogin)); // Thêm panel mới
//			                parent.revalidate(); // Cập nhật lại giao diện
//			                parent.repaint();    // Vẽ lại giao diện
//			            } else {
//			                System.err.println("Lỗi: Không tìm thấy container cha.");
//			            }
//			        } else {
//			            MessageDialog.error(this, "Lỗi: Không thể lập hóa đơn.");
//			        }
//			    }
//			}else {
//				  MessageDialog.error(this, "Kiểm tra lại các thông tin ");
//			}
//		} catch (NumberFormatException e) {
//			MessageDialog.error(this, "Lỗi thanh toán , vui lòng kiểm tra lại.");
//		}
//    }
//
//	public void loadDanhMucThuoc() {
//		List<DanhMuc> danhMucs = null;
//		try {
//			danhMucs = THUOC_SERVICE.getAllDanhMuc();
//			cboxSearch.addItem("Tất cả");
//			for (DanhMuc a : danhMucs) {
//				cboxSearch.addItem(a.getTen());
//			}
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi tải danh mục thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void loadTableTheoDanhMuc() {
//		String selectedDanhMuc = cboxSearch.getSelectedItem().toString();
//		List<Thuoc> filList = new ArrayList<>();
//		if (selectedDanhMuc.equals("Tất cả")) {
//			try {
//				filList = THUOC_SERVICE.findAll();
//			} catch (Exception e) {
//				MessageDialog.error(this, "Lỗi khi tải tất cả thuốc: " + e.getMessage());
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				filList = THUOC_SERVICE.getThuocByDanhMuc(selectedDanhMuc);
//			} catch (Exception e) {
//				MessageDialog.error(this, "Lỗi khi tải thuốc theo danh mục: " + e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		loadTable(filList);
//	}
//
//	private void loadDDT() {
//		cbb_DDT.removeAllItems();
//		cbb_DDT.addItem("Tất cả");
//		List<PhieuDatThuoc> listDDT = null;
//		try {
//			listDDT = DDT_SERVICE.findAll();
//			boolean hasUnprocessed = false;
//			for (PhieuDatThuoc a : listDDT) {
//				if (!a.isDaXuLy()) {
//					cbb_DDT.addItem(a.getId());
//					hasUnprocessed = true;
//				}
//			}
//			if (!hasUnprocessed) {
//				cbb_DDT.addItem("Không có");
//			}
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi tải danh sách đơn đặt thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	private void selectDDT() {
//		txtSdtDDT.setText("");
//		txtTenDDT.setText("");
//		txtTimeDDT.setText("");
//		String select = (String) cbb_DDT.getSelectedItem();
//		if (select == null || select.equals("Không có") || select.equals("Tất cả")) {
//			return;
//		}
//
//		try {
//			PhieuDatThuoc pdt = DDT_SERVICE.findById(select);
//			if (pdt != null) {
//				KhachHang kh = KH_SERVICE.findById(pdt.getKhachHang().getId());
//				if (kh != null) {
//					txtSdtDDT.setText(kh.getSoDienThoai());
//					txtTenDDT.setText(kh.getHoTen());
//				}
//				txtTimeDDT.setText(pdt.getThoiGian().toString());
//			}
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi chọn đơn đặt thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	private void btnTKDDT() {
//		if (txtSdtDDT.getText().isEmpty()) {
//			MessageDialog.error(this, "Vui lòng nhập số điện thoại.");
//			return;
//		}
//		KhachHang kh = null;
//		try {
//			kh = KH_SERVICE.findBySdt(txtSdtDDT.getText());
//			if (kh == null) {
//				MessageDialog.error(this, "Không tìm thấy khách hàng với số điện thoại này.");
//				resetDDTFields();
//				return;
//			}
//
//			List<PhieuDatThuoc> listPDT = DDT_SERVICE.findByKhachHang(kh);
//			boolean foundUnprocessed = false;
//			cbb_DDT.removeAllItems();
//			cbb_DDT.addItem("Tất cả");
//			for (PhieuDatThuoc pdt : listPDT) {
//				if (!pdt.isDaXuLy()) {
//					cbb_DDT.addItem(pdt.getId());
//					foundUnprocessed = true;
//				}
//			}
//			if (!foundUnprocessed) {
//				cbb_DDT.addItem("Không có");
//			}
//			txtTenDDT.setText(kh.getHoTen());
//		} catch (Exception e) {
//			MessageDialog.error(this, "Lỗi khi tìm kiếm đơn đặt thuốc: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	private void resetDDTFields() {
//		txtTenDDT.setText("");
//		txtTimeDDT.setText("");
//	}
//
//	private void formatTxt() {
//		try {
//			String txtDonGiaFormat = Formatter.FormatVND(Double.parseDouble(txtDonGia.getText().replaceAll("[^\\d.]", "")));
//			txtDonGia.setText(txtDonGiaFormat);
//			String txtTongFormat = Formatter.FormatVND(Double.parseDouble(txtTongTien.getText().replaceAll("[^\\d.]", "")));
//			txtTongTien.setText(txtTongFormat);
//		} catch (NumberFormatException e) {
//			// Xử lý trường hợp không phải là số
//		}
//	}
//
//    public void setPanel(JPanel newPanel) {
//        getRootPane().removeAll();
//        getRootPane().add(newPanel);
//        revalidate();
//        repaint();
//    }
//}
//
//
//
//
//
//
