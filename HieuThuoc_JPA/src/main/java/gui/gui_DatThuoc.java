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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class gui_DatThuoc extends JPanel {
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
	private DefaultTableModel modal;
	private DefaultTableModel modalCart= new DefaultTableModel();

	private ThuocService THUOC_SEVICE;
	private KhachHangService KHACH_HANG_SEVICE;
	private DatThuocSevice DAT_THUOC_SEVICE;
	private PhieuDatThuocService PHEU_DAT_THUOC_SEVICE;
	private NhanVienService NHAN_VIEN_SECICE;

	private List<ChiTietPhieuDatThuoc> listCTPDT = new ArrayList<>();
	private List<Thuoc> listThuoc = new ArrayList<>();
	private TaiKhoan tklogin ;
	private JTable tableCart;
	private JComboBox cboxSearch;
	private JTextField txttongHD;

	private void initializeServices(){
		try {
			THUOC_SEVICE = new ThuocServiceImpl(new ThuocDAOImpl(), new DanhMucDAOImpl());
			KHACH_HANG_SEVICE = new KhachHangServiceImpl(new KhachHangDAOImpl());
			DAT_THUOC_SEVICE = new DatThuocServiceImpl(new PhieuDatThuocDAOImpl(), new ThuocDAOImpl(), new KhachHangDAOImpl(), new ChiTietPhieuDatThuocDAOImpl());
			PHEU_DAT_THUOC_SEVICE = new PhieuDatThuocServiceImpl(new PhieuDatThuocDAOImpl(), new ChiTietPhieuDatThuocDAOImpl());
			NHAN_VIEN_SECICE = new NhanVienServiceImpl(new NhanVienDAOImpl());
			loadDanhMucThuoc();
			formatTxt();
		} catch (Exception e) {
			MessageDialog.error(this, "Khởi tạo dịch vụ thất bại: " + e.getMessage());
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
		txtMaPDT = new JTextField();
		txtsdt = new JTextField();
		txtHoTenKH = new JTextField();
		txtTongTien = new JTextField();
		txttongHD = new JTextField();

		table = new JTable();
		tableCart = new JTable();
		cboxSearch = new JComboBox();

		txtHinhAnh.setBorder(BorderFactory.createEtchedBorder());
	}


    public gui_DatThuoc(TaiKhoan login) {

		this.tklogin = login;
		System.out.printf(login.toString());
		initializeComponents();
		initializeServices();
		setupUI();
		try {
			loadTable(THUOC_SEVICE.findAll());
		} catch (Exception e) {
			e.printStackTrace();
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

		JLabel lblMainTitle = new JLabel("ĐĂT THUỐC");
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

//		JPanel orderPreviewPanel = createOrderPreviewPanel();
//		contentPanel.add(orderPreviewPanel);

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
				field.setText("0.0");
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
		btnAddCart.addActionListener(e -> {
            try {
                btnAddCartActionPerformed();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
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

		JPanel invoiceHeader = createHeaderPanel("THÔNG TIN PHIẾU ĐẶT");
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
//		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.15; // Decreased label width
		JLabel lblMaHD = new JLabel("MAPDT");
		lblMaHD.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblMaHD, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85; // Increased text field width
		txtMaPDT = new JTextField();
		txtMaPDT.setEditable(false);
		txtMaPDT.setBackground(Color.WHITE);
		txtMaPDT.setFont(new Font("Arial", Font.BOLD, 15));
		txtMaPDT.setText(RandomMa.maPhieuDatAuto());
		panel.add(txtMaPDT, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.15; // Decreased label width
		JLabel lblSDT = new JLabel("SDT");
		lblSDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblSDT, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.7; // Increased text field width
		txtsdt = new JTextField();
		txtsdt.setHorizontalAlignment(SwingConstants.CENTER);
		txtsdt.setFont(new Font("Arial", Font.BOLD, 15));
		panel.add(txtsdt, gbc);

		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.15;
		JButton btnSearch = new JButton("");
		btnSearch.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/search.png")));
		btnSearch.setToolTipText("Tìm kiếm");
		btnSearch.setFont(new Font("Dialog", Font.BOLD, 16));
		btnSearch.addActionListener(e -> btnSearchActionPerformed());
		panel.add(btnSearch, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2; // Fixed gridy value
		gbc.gridwidth = 1;
		gbc.weightx = 0.15; // Decreased label width
		JLabel lblTenKH = new JLabel("TENKH");
		lblTenKH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblTenKH, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85; // Increased text field width
		txtHoTenKH = new JTextField();
		txtHoTenKH.setHorizontalAlignment(SwingConstants.CENTER);
		txtHoTenKH.setFont(new Font("Arial", Font.BOLD, 15));
		panel.add(txtHoTenKH, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3; // Fixed gridy value
		gbc.gridwidth = 4;
		gbc.weightx = 1.0;
		JSeparator separator = new JSeparator();
		panel.add(separator, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.15; // Decreased label width
		JLabel lblTongTien = new JLabel("TỔNG TIỀN");
		lblTongTien.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblTongTien, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85; // Increased text field width
		txtTongTien = new JTextField("0.0");
		txtTongTien.setHorizontalAlignment(SwingConstants.CENTER);
		txtTongTien.setFont(new Font("Arial", Font.BOLD, 15));
		txtTongTien.setFocusable(false);
		panel.add(txtTongTien, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0.15; // Decreased label width
		JLabel lblTongThanhToan = new JLabel("TỔNG THANH TOÁN");
		lblTongThanhToan.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblTongThanhToan, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85; // Increased text field width
		txttongHD = new JTextField();
		txttongHD.setHorizontalAlignment(SwingConstants.CENTER);
		txttongHD.setFont(new Font("Arial", Font.BOLD, 15));
		panel.add(txttongHD, gbc);

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
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.EAST;
		JButton btnThanhToan = new JButton("ĐẶT THUỐC");
		btnThanhToan.setIcon(new ImageIcon(gui_BanThuoc.class.getResource("/icon/bill.png")));
		btnThanhToan.setFont(new Font("Dialog", Font.BOLD, 16));
		btnThanhToan.setFocusable(false);
		btnThanhToan.addActionListener(e -> btnThanhToanActionPerformed());
		panel.add(btnThanhToan, gbc);

		return panel;
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
		modal.setRowCount(0);
		String search = txtSearch.getText().toLowerCase().trim();
		try {
			List<Thuoc> listsearch = THUOC_SEVICE.searchThuoc(search);
			loadTable(listsearch);
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tìm kiếm thuốc: " + e.getMessage());
			e.printStackTrace();
		}
    }

	private NhanVien getNhanVienbyID() throws RemoteException {
		NhanVien nhanVien = NHAN_VIEN_SECICE.findById(tklogin.getId());
		return nhanVien ;
	}


    private PhieuDatThuoc getInputPhieuDat() throws RemoteException {
        String idHD = txtMaPDT.getText();
        Timestamp thoiGian = new Timestamp(System.currentTimeMillis());
        NhanVien nhanVien = getNhanVienbyID();
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


    private ChiTietPhieuDatThuoc getInputChiTietPhieuDat() throws RemoteException {
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

    private void btnAddCartActionPerformed() throws RemoteException {
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
		try {
			txtSearch.setText("");
			cboxSearch.setSelectedIndex(0);
			loadTable(THUOC_SEVICE.findAll());
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi làm mới danh sách thuốc: " + e.getMessage());
		}

    }
    private void btnHuyActionPerformed() {
		if (MessageDialog.confirm(this, "Xác nhận hủy đặt thuốc?", "Hủy đặt thuốc")) {
			try {
				for (ChiTietPhieuDatThuoc ctpdt : listCTPDT) {
					Thuoc thuoc = THUOC_SEVICE.findById(ctpdt.getThuoc().getId());
					thuoc.setSoLuongTon(thuoc.getSoLuongTon() + ctpdt.getSoLuong());
					THUOC_SEVICE.update(thuoc);
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
			loadTable(THUOC_SEVICE.findAll());
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi làm mới danh sách thuốc: " + e.getMessage());
		}
		if (modalCart.getColumnCount() != 0) {
			modalCart.setRowCount(0);
		}
	}


    private void btnThanhToanActionPerformed() {
		try {
			if (modalCart.getColumnCount() != 0 && isValidPhieuDat()) {
				if (MessageDialog.confirm(this, "Xác nhận thanh toán?", "Lập phiếu đặt thuốc")) {
					PhieuDatThuoc pdt = getInputPhieuDat();

					if (pdt != null) {
						boolean saveSuccess = DAT_THUOC_SEVICE.addPhieuDatThuoc(pdt, listCTPDT);

						if (!saveSuccess) {
							MessageDialog.error(this, "Không thể lưu phiếu đặt vào cơ sở dữ liệu!");
							return;
						}

						MessageDialog.info(this, "Lập phiếu đặt thuốc thành công!");

						if (MessageDialog.confirm(this, "Bạn có muốn in phiếu đặt không?", "In phiếu đặt thuốc")) {
							new other.WritePDF().printPhieuDatThuoc(pdt, listCTPDT, 0);
						}

						// Reset UI and navigate
						txtMaPDT.setText(RandomMa.maPhieuNhapAuto());
						listCTPDT.clear();
						loadTableCart(listCTPDT);
						deteleAllTxt();

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

			}
		}catch(Exception e){
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
			for (DanhMuc dm : danhMucList) {
				model.addElement(dm.getTen());
			}
		} catch (Exception e) {
			MessageDialog.error(this, "Lỗi khi tải danh mục thuốc: " + e.getMessage());
			e.printStackTrace();
		}
    }
    public void loadTableTheoDanhMuc() {
		String selectedDanhMuc = (String) cboxSearch.getSelectedItem();
		if (selectedDanhMuc.equals("Tất cả")) {
			try {
				loadTable(THUOC_SEVICE.findAll());
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi tải thuốc: " + e.getMessage());
			}
		} else {
			try {
				List<Thuoc> list = THUOC_SEVICE.getThuocByDanhMuc(selectedDanhMuc);
				loadTable(list);
			} catch (Exception e) {
				MessageDialog.error(this, "Lỗi khi tải thuốc theo danh mục: " + e.getMessage());
				e.printStackTrace();
			}
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
    


 


