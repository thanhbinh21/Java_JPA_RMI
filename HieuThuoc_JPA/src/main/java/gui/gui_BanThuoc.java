package gui;
import dao.impl.*;
import entity.*;
import other.Formatter;
import other.MessageDialog;
import other.RandomMa;
import other.Validation;
import service.*;
import service.impl.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class gui_BanThuoc extends JPanel {

	private JTextField txtMaThuoc;
	private JTextField txtTenThuoc;
	private JTextField txtThanhPhan;
	private JTextField txtDonGia;
	private JTextField txtSearch;
	private JTextField txtSoLuong;
	private JTable table;
	private JTextField txtMaHoaDon;
	private JTextField txtsdt;
	private JTextField txtHoTenKH;
	private JTextField txtTongTien;
	JLabel txtHinhAnh = new JLabel("");

	private DefaultTableModel modal;
	private DefaultTableModel modalCart;
	private List<Thuoc> listThuoc = new ArrayList<>();
	private List<ChiTietHoaDon> listCTHD = new ArrayList<>();
	private TaiKhoan tklogin;
	private ThuocService THUOC_SERVICE;
	private KhachHangService KH_SERVICE;
	private HoaDonService HD_SERVICE;
	private BanThuocService BAN_THUOC_SERVICE;
	private PhieuDatThuocService DDT_SERVICE;

	private JTable tableCart;
	private JComboBox cboxSearch = new JComboBox();
	private JComboBox cbb_DDT = new JComboBox();
	private JTextField txttongHD;
	private JTextField txtSdtDDT;
	private JTextField txtTenDDT;
	private JTextField txtTimeDDT;


	public gui_BanThuoc(TaiKhoan login) {
		this.tklogin = login;
		initializeComponents();
		initializeServices();
		setupUI();

		try {
			loadTable(THUOC_SERVICE.findAll());
			loadDanhMucThuoc();
			loadDDT();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initializeComponents() {
		txtMaThuoc = new JTextField();
		txtTenThuoc = new JTextField();
		txtThanhPhan = new JTextField();
		txtDonGia = new JTextField();
		txtSearch = new JTextField();
		txtSoLuong = new JTextField();
		txtMaHoaDon = new JTextField();
		txtsdt = new JTextField();
		txtHoTenKH = new JTextField();
		txtTongTien = new JTextField();
		txttongHD = new JTextField();
		txtSdtDDT = new JTextField();
		txtTenDDT = new JTextField();
		txtTimeDDT = new JTextField();

		cbb_DDT = new JComboBox();
		table = new JTable();
		tableCart = new JTable();
		cboxSearch = new JComboBox();

		txtHinhAnh.setBorder(BorderFactory.createEtchedBorder());
	}

	private void initializeServices() {
		try {
//			THUOC_SERVICE = new ThuocServiceImpl(new ThuocDAOImpl(), new DanhMucDAOImpl());
//			KH_SERVICE = new KhachHangServiceImpl(new KhachHangDAOImpl());
//			HD_SERVICE = new HoaDonServiceImpl(new HoaDonDAOImpl());
//
//			BAN_THUOC_SERVICE = new BanThuocServiceImpl(new ThuocDAOImpl(), new HoaDonDAOImpl(),
//					new KhachHangDAOImpl(), new ChiTietHoaDonDAOImpl(), new DanhMucDAOImpl());
//			DDT_SERVICE = new PhieuDatThuocServiceImpl(new PhieuDatThuocDAOImpl(), new ChiTietPhieuDatThuocDAOImpl());
			Registry registry = LocateRegistry.getRegistry("172.20.10.12", 8989);

			THUOC_SERVICE = (ThuocService) registry.lookup("ThuocService");
			KH_SERVICE = (KhachHangService) registry.lookup("KhachHangService");
			HD_SERVICE = (HoaDonService) registry.lookup("HoaDonService");
			BAN_THUOC_SERVICE = (BanThuocService) registry.lookup("BanThuocService");
			DDT_SERVICE = (PhieuDatThuocService) registry.lookup("PhieuDatThuocService");

			modal = new DefaultTableModel(new Object[][]{},
					new String[]{"STT", "Mã thuốc", "Tên thuốc", "Danh mục", "Nhà SX", "ĐVT", "Số lượng", "Đơn giá"});
			modalCart = new DefaultTableModel(new Object[][]{},
					new String[]{"STT", "Tên thuốc", "Số lượng", "Đơn giá"});
			table.setModel(modal);
			tableCart.setModel(modalCart);


			loadTable(THUOC_SERVICE.findAll());
			loadDanhMucThuoc();
			loadDDT();
			txtMaHoaDon.setText(RandomMa.maHoaDonAuto());
			formatTxt();
		} catch (Exception e) {
			MessageDialog.error(this, "Khởi tạo dịch vụ thất bại: " + e.getMessage());
		}
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		JPanel titlePanel = createTitlePanel();
		add(titlePanel, BorderLayout.NORTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerLocation(0.5);
		splitPane.setContinuousLayout(true);
		add(splitPane, BorderLayout.CENTER);

		JPanel leftPanel = createLeftPanel();
		splitPane.setLeftComponent(leftPanel);

		JPanel rightPanel = createRightPanel();
		splitPane.setRightComponent(rightPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension((int)(screenSize.getWidth() * 0.8),
				(int)(screenSize.getHeight() * 0.8)));

		revalidate();
		repaint();
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JLabel lblMainTitle = new JLabel("BÁN THUỐC");
		lblMainTitle.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/bill.png")));
		lblMainTitle.setForeground(new Color(0, 0, 0));
		lblMainTitle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 35));

		titlePanel.add(lblMainTitle);
		return titlePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(
				BorderFactory.createEmptyBorder(0, 10, 0, 10)
		);
		leftPanel.setLayout(new BorderLayout(0, 10));

		JPanel panelProductHeader = createHeaderPanel("THÔNG TIN THUỐC");
		leftPanel.add(panelProductHeader, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JPanel productDetailsPanel = createProductDetailsPanel();
		contentPanel.add(productDetailsPanel);

		JPanel searchPanel = createSearchPanel();
		contentPanel.add(searchPanel);

		JPanel productListPanel = createProductListPanel();
		contentPanel.add(productListPanel);

		JPanel orderPreviewPanel = createOrderPreviewPanel();
		contentPanel.add(orderPreviewPanel);

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		return leftPanel;
	}

	private JPanel createProductDetailsPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel imagePanel = new JPanel(new BorderLayout());
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		imagePanel.setPreferredSize(new Dimension(210, 176));

		txtHinhAnh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		imagePanel.add(txtHinhAnh, BorderLayout.CENTER);

		JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		String[][] formFields = {
				{"MATHUOC:", "txtMaThuoc"},
				{"TENTHUOC:", "txtTenThuoc"},
				{"THANHPHAN:", "txtThanhPhan"},
				{"DONGIA:", "txtDonGia"}
		};

		for (int i = 0; i < formFields.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 0.2;
			gbc.fill = GridBagConstraints.NONE;

			JLabel label = new JLabel(formFields[i][0]);
			label.setFont(new Font("Tahoma", Font.PLAIN, 15));
			detailsPanel.add(label, gbc);

			gbc.gridx = 1;
			gbc.weightx = 0.8;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			JTextField field = getTextField(formFields[i][1]);
			field.setHorizontalAlignment(SwingConstants.CENTER);
			if (field == txtDonGia) {
				field.setFocusable(false);
			}
			detailsPanel.add(field, gbc);
		}

		panel.add(imagePanel, BorderLayout.WEST);
		panel.add(detailsPanel, BorderLayout.CENTER);

		return panel;
	}

	private JTextField getTextField(String fieldName) {
		switch (fieldName) {
			case "txtMaThuoc": return txtMaThuoc;
			case "txtTenThuoc": return txtTenThuoc;
			case "txtThanhPhan": return txtThanhPhan;
			case "txtDonGia": return txtDonGia;
			default: return new JTextField();
		}
	}

	private JPanel createSearchPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		cboxSearch.setModel(new DefaultComboBoxModel(new String[]{"Tất cả"}));
		cboxSearch.setBorder(BorderFactory.createTitledBorder("Loại Thuốc"));
		cboxSearch.setPreferredSize(new Dimension(160, 40));
		cboxSearch.addActionListener(e -> loadTableTheoDanhMuc());
		panel.add(cboxSearch);

		txtSearch.setBorder(BorderFactory.createTitledBorder("Tra cứu"));
		txtSearch.setPreferredSize(new Dimension(200, 40));
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				ketQuaTimKiem();
			}
		});
		panel.add(txtSearch);

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(3, 40));
		panel.add(separator);

		JButton btnReload = new JButton("Làm mới");
		btnReload.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/refresh.png")));
		btnReload.setFont(new Font("Dialog", Font.BOLD, 16));
		btnReload.setFocusable(false);
		btnReload.addActionListener(e -> btnReloadActionPerformed());
		panel.add(btnReload);

		JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		separator1.setPreferredSize(new Dimension(3, 40));

		panel.add(Box.createHorizontalStrut(10));
		panel.add(separator1);
		JLabel label = new JLabel("Nhập số lượng: ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(label);
		txtSoLuong.setPreferredSize(new Dimension(150, 30));
		panel.add(txtSoLuong);

		JButton btnAddCart = new JButton("THÊM");
		btnAddCart.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/add-to-cart.png")));
		btnAddCart.setFont(new Font("Dialog", Font.BOLD, 16));
		btnAddCart.setFocusable(false);
		btnAddCart.addActionListener(e -> btnAddCartActionPerformed());
		panel.add(btnAddCart);

		return panel;
	}

	private JPanel createProductListPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Danh sách Thuốc"));

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tableMouseClicked();
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(scrollPane, BorderLayout.CENTER);

		panel.setPreferredSize(new Dimension(400, 200)); // Height will adapt, width will fill

		return panel;
	}

	private JPanel createOrderPreviewPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Đơn đặt thuốc từ trước"));
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		JLabel lbl_DDT = new JLabel("Mã đơn đặt");
		lbl_DDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lbl_DDT, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.3;
		cbb_DDT.setModel(new DefaultComboBoxModel(new String[]{"Tất cả"}));
		cbb_DDT.addActionListener(e -> selectDDT());
		panel.add(cbb_DDT, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0.2;
		JLabel lbl_SDT = new JLabel("Số điện thoại");
		lbl_SDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lbl_SDT, gbc);

		gbc.gridx = 3;
		gbc.weightx = 0.3;
		panel.add(txtSdtDDT, gbc);

		gbc.gridx = 4;
		gbc.weightx = 0.1;
		JButton btn_tkimDDT = new JButton("Tìm");
		btn_tkimDDT.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/search.png")));
		btn_tkimDDT.setFont(new Font("Dialog", Font.BOLD, 16));
		btn_tkimDDT.addActionListener(e -> btnTKDDT());
		panel.add(btn_tkimDDT, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.2;
		JLabel lbl_TenKH = new JLabel("Tên khách hàng");
		lbl_TenKH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lbl_TenKH, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.3;
		panel.add(txtTenDDT, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.2;
		JLabel lbl_Time = new JLabel("Thời gian đặt");
		lbl_Time.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lbl_Time, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.3;
		panel.add(txtTimeDDT, gbc);

		gbc.gridx = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 0.4;
		JButton btnViewOrderDetails = new JButton("Xem Chi Tiết");
		btnViewOrderDetails.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/add-to-cart.png")));
		btnViewOrderDetails.setFont(new Font("Dialog", Font.BOLD, 16));
		btnViewOrderDetails.setFocusable(false);
		btnViewOrderDetails.addActionListener(e -> btnAddDDT());
		panel.add(btnViewOrderDetails, gbc);

		return panel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(
				BorderFactory.createEmptyBorder(0, 5, 5, 5)

		);
		rightPanel.setLayout(new BorderLayout(0, 10));

		JPanel orderHeader = createHeaderPanel("THÔNG TIN GIỎ HÀNG");
		rightPanel.add(orderHeader, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JPanel cartDetailsPanel = createCartDetailsPanel();
		contentPanel.add(cartDetailsPanel);

		JPanel invoiceHeader = createHeaderPanel("THÔNG TIN HÓA ĐƠN");
		contentPanel.add(invoiceHeader);

		JPanel invoiceDetailsPanel = createInvoiceDetailsPanel();
		contentPanel.add(invoiceDetailsPanel);

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		rightPanel.add(scrollPane, BorderLayout.CENTER);

		return rightPanel;
	}

	private JPanel createHeaderPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel label = new JLabel(title);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(label);

		return panel;
	}

	private JPanel createCartDetailsPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		tableCart = new JTable();
		tableCart.setModel(new DefaultTableModel(
				new Object[][] {},
				new String[] {"STT", "Tên thuốc", "Số lượng", "Đơn giá đã giảm"}
		));

		JScrollPane scrollPane = new JScrollPane(tableCart);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnXoa = new JButton("XÓA");
		btnXoa.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/delete.png")));
		btnXoa.setFont(new Font("Dialog", Font.BOLD, 16));
		btnXoa.setFocusable(false);
		btnXoa.addActionListener(e -> btnDeleteCartItemActionPerformed());
		buttonPanel.add(btnXoa);

		panel.add(buttonPanel, BorderLayout.SOUTH);
		panel.setPreferredSize(new Dimension(0, 200));

		return panel;
	}
	private JPanel createInvoiceDetailsPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Hàng 0: Mã hóa đơn
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.15;
		JLabel lblMaHD = new JLabel("Mã hóa đơn");
		lblMaHD.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblMaHD, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85;
		txtMaHoaDon = new JTextField();
		txtMaHoaDon.setEditable(false);
		txtMaHoaDon.setBackground(Color.WHITE);
		txtMaHoaDon.setFont(new Font("Arial", Font.BOLD, 15));
		txtMaHoaDon.setText(RandomMa.maHoaDonAuto());
		panel.add(txtMaHoaDon, gbc);

		// Hàng 1: Số điện thoại và nút tìm kiếm
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.15;
		JLabel lblSDT = new JLabel("Số điện thoại");
		lblSDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblSDT, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2; // Reduced width for text field
		gbc.weightx = 0.70;
		txtsdt = new JTextField();
		txtsdt.setHorizontalAlignment(SwingConstants.CENTER);
		txtsdt.setFont(new Font("Arial", Font.BOLD, 15));
		panel.add(txtsdt, gbc);

		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.15;
		gbc.anchor = GridBagConstraints.EAST; // Align to the right
		JButton btnSearch = new JButton("");
		btnSearch.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/search.png")));
		btnSearch.setToolTipText("Tìm kiếm");
		btnSearch.setFont(new Font("Dialog", Font.BOLD, 16));
		btnSearch.addActionListener(e -> btnSearchActionPerformed());
		panel.add(btnSearch, gbc);
		gbc.anchor = GridBagConstraints.WEST; // Reset anchor

		// Hàng 2: Tên khách hàng và nút thêm khách hàng
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.15;
		JLabel lblTenKH = new JLabel("Tên khách hàng");
		lblTenKH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblTenKH, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2; // Reduced width for text field
		gbc.weightx = 0.70;
		txtHoTenKH = new JTextField();
		txtHoTenKH.setHorizontalAlignment(SwingConstants.CENTER);
		txtHoTenKH.setFont(new Font("Arial", Font.BOLD, 15));
		panel.add(txtHoTenKH, gbc);

		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.15;
		gbc.anchor = GridBagConstraints.EAST; // Align to the right
		JButton btnAddKH = new JButton("");
		btnAddKH.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/user.png")));
		btnAddKH.setToolTipText("Thêm khách hàng");
		btnAddKH.setFont(new Font("Dialog", Font.BOLD, 16));
		btnAddKH.addActionListener(e -> btnKhachHangActionPerformed());
		panel.add(btnAddKH, gbc);
		gbc.anchor = GridBagConstraints.WEST; // Reset anchor

		// Hàng 3: Dấu phân cách
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 4;
		gbc.weightx = 1.0;
		JSeparator separator = new JSeparator();
		panel.add(separator, gbc);

		// Hàng 4: Tổng tiền và VAT
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.15;
		JLabel lblTongTien = new JLabel("TỔNG TIỀN");
		lblTongTien.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblTongTien, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.35;
		txtTongTien = new JTextField("0.0");
		txtTongTien.setHorizontalAlignment(SwingConstants.CENTER);
		txtTongTien.setFont(new Font("Arial", Font.BOLD, 15));
		txtTongTien.setFocusable(false);
		panel.add(txtTongTien, gbc);

		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		JLabel vat = new JLabel("VAT: 10% ");

		vat.setForeground(Color.RED);
		vat.setFont(new Font("Tahoma", Font.ITALIC, 15));
		vat.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(vat, gbc);

		// Hàng 5: Tổng thanh toán
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.15;
		JLabel lblTongThanhToan = new JLabel("THANH TOÁN");
		lblTongThanhToan.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblTongThanhToan, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85;
		txttongHD = new JTextField();
		txttongHD.setHorizontalAlignment(SwingConstants.CENTER);
		txttongHD.setFont(new Font("Arial", Font.BOLD, 15));
		panel.add(txttongHD, gbc);

		// Hàng 6: Nút hủy và thanh toán
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		JButton btnHuyHD = new JButton("HỦY");
		btnHuyHD.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/return.png")));
		btnHuyHD.setFont(new Font("Dialog", Font.BOLD, 16));
		btnHuyHD.setForeground(Color.RED);
		btnHuyHD.setFocusable(false);
		btnHuyHD.addActionListener(e -> btnHuyActionPerformed());
		panel.add(btnHuyHD, gbc);

		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.EAST;
		JButton btnThanhToan = new JButton("THANH TOÁN");
		btnThanhToan.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/bill.png")));
		btnThanhToan.setFont(new Font("Dialog", Font.BOLD, 16));
		btnThanhToan.setFocusable(false);
		btnThanhToan.addActionListener(e -> btnThanhToanActionPerformed());
		panel.add(btnThanhToan, gbc);

		return panel;
	}
	private void btnKhachHangActionPerformed() {
		gui_themKhachHang dialog = new gui_themKhachHang();
		dialog.setSize(600, 400);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}



	private void handleWindowResize() {
		revalidate();
		repaint();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (getParent() != null) {
			Container topLevelContainer = getTopLevelAncestor();
			if (topLevelContainer instanceof Window) {
				Window window = (Window) topLevelContainer;
				window.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						handleWindowResize();
					}
				});
			}
		}
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


				if (e.getHanSuDung().after(new Date(System.currentTimeMillis()))) {
					modal.addRow(new Object[]{String.valueOf(stt), e.getId(), e.getTen(), e.getDanhMuc().getTen(),
							e.getNhaSanXuat().getTen(), e.getDonViTinh(), e.getSoLuongTon(), Formatter.FormatVND(e.getDonGia()), Formatter.formatPercentage(e.getKhuyenMai().getPhanTramGiamGia())});
					stt++;
				}
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tải dữ liệu thuốc: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void loadTableCart(List<ChiTietHoaDon> list) {
		modalCart = new DefaultTableModel(new Object[][]{}, new String[]{"STT", "Tên thuốc", "Số lượng", "Đơn giá đã giảm"});
		tableCart.setModel(modalCart);
		modalCart.setRowCount(0);
		listCTHD = list;
		int stt = 1;
		double sum = 0;
		for (ChiTietHoaDon e : listCTHD) {
			sum = sum + e.getDonGia() * e.getSoLuong();
			modalCart.addRow(new Object[]{
					String.valueOf(stt),
					e.getThuoc().getTen(),
					e.getSoLuong(),
					Formatter.FormatVND(e.getDonGia()),});
			stt++;
		}
		txtTongTien.setText(Formatter.FormatVND(sum));
		txttongHD.setText(Formatter.FormatVND(sum*1.1));
	}


	private List<ChiTietHoaDon> convertToChiTietHoaDon(List<ChiTietPhieuDatThuoc> arr) {
		List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
		for (ChiTietPhieuDatThuoc item : arr) {
			ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
			chiTietHoaDon.setHoaDon(new HoaDon(txtMaHoaDon.getText()));
			chiTietHoaDon.setThuoc(item.getThuoc());
			chiTietHoaDon.setSoLuong(item.getSoLuong());
			chiTietHoaDon.setDonGia(item.getDonGia());
			chiTietHoaDonList.add(chiTietHoaDon);
		}
		return chiTietHoaDonList;
	}

	private void tableMouseClicked() {
		int row = table.getSelectedRow();
		if (row >= 0) {
			String idThuoc = (String) modal.getValueAt(row, 1);
			String defaultImagePath = "/product-image/default-drug.png";

			try {
				Thuoc e = THUOC_SERVICE.findById(idThuoc);
				if (e != null) {
					String baseResourcePath = "/product-image/";
					String imageName = e.getHinhAnh();
					ImageIcon imageIcon = null;

					if (imageName != null && !imageName.trim().isEmpty()) {
						java.net.URL imageUrl = getClass().getResource(baseResourcePath + imageName);

						if (imageUrl != null) {
							imageIcon = new ImageIcon(imageUrl);
						} else {
							File imageFile = new File("src/main/resources/product-image/" + imageName);
							if (imageFile.exists()) {
								imageIcon = new ImageIcon(imageFile.getAbsolutePath());
							} else {
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
		modal.setRowCount(0);
		String search = txtSearch.getText().toLowerCase().trim();
		try {
			List<Thuoc> listsearch = THUOC_SERVICE.searchThuoc(search);
			loadTable(listsearch);
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tìm kiếm thuốc: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private HoaDon getInputHoaDon() {
		String idHD = txtMaHoaDon.getText();
		Timestamp thoiGian = new Timestamp(System.currentTimeMillis());
		NhanVien nhanVien = tklogin.getNhanVien();
		try {
			KhachHang khachHang = KH_SERVICE.getKhachHangBySdt(txtsdt.getText());
			return new HoaDon(idHD, thoiGian, nhanVien, khachHang);
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private boolean isValidHoaDon() {
		if (listCTHD.isEmpty()) {
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

	private boolean isValidChiTietHoaDon() {
		if (Validation.isEmpty(txtSoLuong.getText().trim())) {
			MessageDialog.warring(this, "Số lượng không được để trống!");
			txtSoLuong.requestFocus();
			return false;
		} else {
			try {
				Thuoc selectedThuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
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
			Thuoc selectedThuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
			for (ChiTietHoaDon cthd : listCTHD) {
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

	private ChiTietHoaDon getInputChiTietHoaDon() {
		HoaDon hoaDon = getInputHoaDon();
		try {
			Thuoc thuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
			int soLuong = Integer.parseInt(txtSoLuong.getText());
			double donGia = thuoc.getDonGia();
			return new ChiTietHoaDon(hoaDon, thuoc, soLuong, donGia);
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi lấy thông tin thuốc: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private void btnAddCartActionPerformed() {
		if (isValidChiTietHoaDon()) {
			ChiTietHoaDon cthd = getInputChiTietHoaDon();
			if (cthd != null) {
				String maThuocMoi = cthd.getThuoc().getId();
				boolean daTonTai = false;

				for (ChiTietHoaDon item : listCTHD) {
					if (item.getThuoc().getId().equals(maThuocMoi)) {
						// Tăng số lượng
						item.setSoLuong(item.getSoLuong() + cthd.getSoLuong());
						daTonTai = true;
						break;
					}
				}

				// Nếu thuốc chưa có trong giỏ thì thêm mới
				if (!daTonTai) {
					listCTHD.add(cthd);
				}

				// Cập nhật bảng giỏ hàng
				loadTableCart(listCTHD);

				try {
					Thuoc thuoc = THUOC_SERVICE.findById(txtMaThuoc.getText());
					int updatedSoLuongTon = thuoc.getSoLuongTon() - cthd.getSoLuong();

					// Cập nhật số lượng tồn
					thuoc.setSoLuongTon(updatedSoLuongTon);
					THUOC_SERVICE.update(thuoc);

					// Làm mới bảng thuốc
					loadTable(THUOC_SERVICE.findAll());

					// Reset số lượng
					txtSoLuong.setText("");
				} catch (Exception e) {
					MessageDialog.error(this, "Lỗi khi cập nhật số lượng thuốc: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}


	private void btnSearchActionPerformed() {
		try {
			String sdt = txtsdt.getText().trim();
			if (!Validation.isPhoneNumber(sdt)) {
				MessageDialog.error(this, "Số điện thoại không hợp lệ!");
				txtsdt.requestFocus();
				return;
			}

			KhachHang kh = KH_SERVICE.getKhachHangBySdt(sdt);
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
			if (selectedRow < 0 || selectedRow >= listCTHD.size()) {
				MessageDialog.warring(this, "Vui lòng chọn thuốc cần xóa!");
				return;
			}

			ChiTietHoaDon cthd = listCTHD.remove(selectedRow);
			loadTableCart(listCTHD);

			try {
				Thuoc thuoc = THUOC_SERVICE.findById(cthd.getThuoc().getId());
				thuoc.setSoLuongTon(thuoc.getSoLuongTon() + cthd.getSoLuong());
				THUOC_SERVICE.update(thuoc);
				loadTable(THUOC_SERVICE.findAll());
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi cập nhật thuốc: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public List<Thuoc> getSearchTable(String text) {
		try {
			return THUOC_SERVICE.searchThuoc(text.trim().toLowerCase());
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tìm kiếm thuốc: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private void btnReloadActionPerformed() {
		try {
			txtSearch.setText("");
			cboxSearch.setSelectedIndex(0);
			loadTable(THUOC_SERVICE.findAll());
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi làm mới danh sách thuốc: " + e.getMessage());
		}
	}

	private void btnHuyActionPerformed() {
		if (MessageDialog.confirm(this, "Xác nhận hủy hóa đơn?", "Hủy hóa đơn")) {
			try {
				for (ChiTietHoaDon cthd : listCTHD) {
					Thuoc thuoc = THUOC_SERVICE.findById(cthd.getThuoc().getId());
					thuoc.setSoLuongTon(thuoc.getSoLuongTon() + cthd.getSoLuong());
					THUOC_SERVICE.update(thuoc);
				}
				deteleAllTxt();

				Window parentWindow = SwingUtilities.getWindowAncestor(this);
				if (parentWindow != null) {
					parentWindow.dispose();
				}
				new gui_TrangChu(tklogin).setVisible(true);
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi hoàn tác thuốc: " + e.getMessage());
			}
		}
	}

	private void deteleAllTxt() {
		txtDonGia.setText("");
		txtHinhAnh.setText("");
		txtHoTenKH.setText("");
		txtMaThuoc.setText("");
		txtsdt.setText("");
		txtSearch.setText("");
		txtSoLuong.setText("");
		txtTenThuoc.setText("");
		txtThanhPhan.setText("");
		txttongHD.setText("");
		txtTongTien.setText("");
		txtHinhAnh.setIcon(null);

		try {
			loadTable(THUOC_SERVICE.findAll());
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi làm mới danh sách thuốc: " + e.getMessage());
		}
		if (modalCart.getColumnCount() != 0) {
			modalCart.setRowCount(0);
		}
	}

	private void btnThanhToanActionPerformed() {
		try {
			if (modalCart.getColumnCount() != 0 && isValidHoaDon()) {
				if (MessageDialog.confirm(this, "Xác nhận thanh toán?", "Lập hóa đơn")) {
					HoaDon hd = getInputHoaDon();
					System.out.println(hd);
					System.out.println(listCTHD);;
					boolean saveSuccess = BAN_THUOC_SERVICE.createHoaDon(hd, listCTHD);

					if (!saveSuccess) {
						MessageDialog.error(this, "Không thể lưu hóa đơn vào cơ sở dữ liệu!");
						return;
					}

					MessageDialog.info(this, "Lập hóa đơn thành công!");

					if (MessageDialog.confirm(this, "Bạn có muốn in hóa đơn không?", "In hóa đơn")) {
						new other.WritePDF().printHoaDon(hd, listCTHD, 0.1);
					}

					// Reset UI and navigate
					txtMaHoaDon.setText(RandomMa.maHoaDonAuto());
					listCTHD.clear();
					loadTableCart(listCTHD);
					deteleAllTxt();

					Container parent = this.getParent();
					if (parent != null) {
						parent.remove(this);
						parent.add(new gui_BanThuoc(tklogin));
						parent.revalidate();
						parent.repaint();
					}
				} else {
					MessageDialog.error(this, "Không thể lập hóa đơn.");
				}
			}
		}
		catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi thanh toán: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void selectDDT() {
		String selectedMaPDT = (String) cbb_DDT.getSelectedItem();
		if (selectedMaPDT != null && !selectedMaPDT.equals("Tất cả")) {
			try {
				PhieuDatThuoc selectedPDT = DDT_SERVICE.findById(selectedMaPDT);
				if (selectedPDT != null) {
					txtsdt.setText(selectedPDT.getKhachHang().getSoDienThoai());
					txtTenDDT.setText(selectedPDT.getKhachHang().getHoTen());
					txtTimeDDT.setText(Formatter.FormatDate(selectedPDT.getThoiGian()));
				}
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi chọn đơn đặt thuốc: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void loadDanhMucThuoc() {
		cboxSearch.removeAllItems();
		cboxSearch.addItem("Tất cả");
		cboxSearch.setForeground(Color.BLACK);
		try {
			List<DanhMuc> listDanhMuc = THUOC_SERVICE.getAllDanhMuc();
			if (listDanhMuc != null && !listDanhMuc.isEmpty()) {
				for (DanhMuc a : listDanhMuc) {
					cboxSearch.addItem(a.getTen());
				}
			} else {
				System.out.println("No DanhMuc data found.");
			}
			cboxSearch.setSelectedIndex(0);
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.error(this, "Lỗi khi tải danh mục thuốc: " + e.getMessage());
		}
	}

	private void loadDDT() {
		try {
			// Xóa toàn bộ item cũ
			cbb_DDT.removeAllItems();
			// Thêm mục mặc định
			cbb_DDT.addItem("Tất cả");

			List<PhieuDatThuoc> dsPhieuDat = DDT_SERVICE.findAll();
			System.out.println(dsPhieuDat);
			boolean hasUnprocessed = false;

			if (dsPhieuDat != null && !dsPhieuDat.isEmpty()) {
				for (PhieuDatThuoc pdt : dsPhieuDat) {
					// Kiểm tra trạng thái đơn đặt (giả sử false là chưa xử lý)
					if (!pdt.isTrangThai()) {
						cbb_DDT.addItem(pdt.getId());
						hasUnprocessed = true;
					}
				}
			}

			// Nếu không có đơn chưa xử lý, hiển thị thông báo
			if (!hasUnprocessed) {
				cbb_DDT.removeAllItems();
				cbb_DDT.addItem("Không có đơn đặt thuốc");
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tải đơn đặt thuốc: " + e.getMessage());
			e.printStackTrace();
		}
	}
	private void loadTableTheoDanhMuc() {
		String selectedDanhMuc = (String) cboxSearch.getSelectedItem();
		if ("Tất cả".equals(selectedDanhMuc)) {
			try {
				loadTable(THUOC_SERVICE.findAll());
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi tải thuốc: " + e.getMessage());
			}
		} else {
			try {
				List<Thuoc> list = THUOC_SERVICE.getThuocByTenDanhMuc(selectedDanhMuc);
				loadTable(list);
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi tải thuốc theo danh mục: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void btnTKDDT() {
		String sdt = txtSdtDDT.getText().trim();
		if (sdt.isEmpty()) {
			MessageDialog.error(this, "Vui lòng nhập số điện thoại!");
			return;
		}
		try {
			List<PhieuDatThuoc> listPDT = DDT_SERVICE.findBySdt(sdt);
			DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbb_DDT.getModel();
			model.removeAllElements();
			for (PhieuDatThuoc pdt : listPDT) {
				model.addElement(pdt.getId());
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tìm đơn đặt thuốc: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void btnAddDDT() {
		String selectedMaPDT = (String) cbb_DDT.getSelectedItem();
		if (selectedMaPDT == null || selectedMaPDT.equals("KHONGCO")) {
			MessageDialog.error(this, "Vui lòng chọn đơn đặt thuốc!");
			return;
		}
		try {
			PhieuDatThuoc pdt = DDT_SERVICE.findById(selectedMaPDT);
			if (pdt == null) {
				MessageDialog.error(this, "Đơn đặt thuốc không tồn tại!");
				return;
			}
			List<ChiTietPhieuDatThuoc> listCTPDT = DDT_SERVICE.findChiTietByPhieuDatThuoc(pdt);
			listCTHD.clear();
			for (ChiTietPhieuDatThuoc ct : listCTPDT) {
				ChiTietHoaDon cthd = new ChiTietHoaDon();
				cthd.setThuoc(ct.getThuoc());
				cthd.setSoLuong(ct.getSoLuong());
				cthd.setDonGia(ct.getDonGia());
				listCTHD.add(cthd);
			}
			loadTableCart(listCTHD);
			KhachHang kh = pdt.getKhachHang();
			txtsdt.setText(kh.getSoDienThoai());
			txtHoTenKH.setText(kh.getHoTen());
			DDT_SERVICE.updateTrangThai(pdt.getId(), true);
			loadDDT();
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi thêm đơn đặt thuốc: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void formatTxt() {

		txtTongTien.setEditable(false);
		txttongHD.setEditable(false);
	}

	public void setPanel(JPanel newPanel) {
		getRootPane().removeAll();
		getRootPane().add(newPanel);
		revalidate();
		repaint();
	}
}