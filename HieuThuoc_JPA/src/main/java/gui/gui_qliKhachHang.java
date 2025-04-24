package gui;

import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import dao.impl.KhachHangDAOImpl;
import entity.KhachHang;
import entity.TaiKhoan;
import other.RandomMa;
import service.KhachHangService;
import service.impl.KhachHangServiceImpl;

public class gui_qliKhachHang extends JPanel implements MouseListener {

	private JTextField txtMaKH, txtTenKH, txtDienThoai;
	private JButton btnThem, btnXoa, btnSua, btnThoat, btnLamMoi;
	private JDateChooser dateChooser;
	private JComboBox<String> cboGioiTinh;
	private JTable table;
	private DefaultTableModel model;
	private KhachHangService KHACH_HANG_SEVICE;
	private static TaiKhoan loginNV = null;

	// Define colors and fonts for styling
	// AliceBlue
	// mau trang
	private static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // AliceBlue
	private static final Color TITLE_COLOR = new Color(70, 130, 180); // SteelBlue
	private static final Color BUTTON_BG = new Color(100, 149, 237); // CornflowerBlue
	private static final Color BUTTON_TEXT = Color.WHITE;
	private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
	private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Quản Lý Khách Hàng");
			gui_qliKhachHang panel = new gui_qliKhachHang(loginNV);
			frame.setContentPane(panel);
			frame.setSize(1200, 700); // Increased width to accommodate EAST panel
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}

	public gui_qliKhachHang(TaiKhoan login) {
		try {
			// rmi
			Registry registry = LocateRegistry.getRegistry(8989);
			KHACH_HANG_SEVICE = (KhachHangService) registry.lookup("KhachHangService");

		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, "Lỗi khởi tạo service: " + e.getMessage());
			e.printStackTrace();
		} catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

        // Set background color for the main panel
		this.setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(10, 10)); // Add gaps for padding
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the panel

		// Title Panel
		JPanel pnTitle = createTitlePanel();
		add(pnTitle, BorderLayout.NORTH);

		// Center Panel to hold Info and Button Panels
		JPanel pnCenter = new JPanel(new BorderLayout());
		pnCenter.setBackground(BACKGROUND_COLOR);

		// Info Panel
		JPanel pnInfo = createInfoPanel();
		pnInfo.setPreferredSize(new Dimension(400, 200)); // Adjusted width
		pnCenter.add(pnInfo, BorderLayout.NORTH);

		// Button Panel
		JPanel pnButtons = createButtonPanel();
		pnCenter.add(pnButtons, BorderLayout.CENTER);

		add(pnCenter, BorderLayout.EAST); // Add center panel to the east side

		// Table Panel
		JScrollPane scrollPane = createTablePanel();
		scrollPane.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the table panel
		add(scrollPane, BorderLayout.CENTER);

		DocDuLieuDatabaseVaoTable();
		table.addMouseListener(this);
		attachButtonListeners();
		
		// Generate random customer ID initially
		generateRandomCustomerId();

		loginNV = login;

	}

	private JPanel createTitlePanel() {
		JPanel pnTitle = new JPanel();
		pnTitle.setBackground(BACKGROUND_COLOR);
		JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 28));
		lblTitle.setForeground(TITLE_COLOR);
		pnTitle.add(lblTitle);
		pnTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Add padding below the title
		return pnTitle;
	}

	private JPanel createInfoPanel() {
		// Panel chính chứa thông tin khách hàng

		JPanel pnInfo = new JPanel();
		pnInfo.setBackground(BACKGROUND_COLOR);
		pnInfo.setBorder(BorderFactory.createTitledBorder(
				new LineBorder(Color.GRAY), "Thông Tin Khách Hàng",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
				LABEL_FONT, TITLE_COLOR));

		// Layout chính của pnInfo
		GroupLayout layout = new GroupLayout(pnInfo);
		pnInfo.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		// Labels
		JLabel lblMaKH = createLabel("Mã Khách Hàng:");
		JLabel lblTenKH = createLabel("Tên Khách Hàng:");
		JLabel lblDienThoai = createLabel("Số Điện Thoại:");
		JLabel lblGioiTinh = createLabel("Giới Tính:");
		JLabel lblNgayThamGia = createLabel("Ngày Tham Gia:");

		// Input fields
		txtMaKH = new JTextField(10);
		txtMaKH.setEditable(false); // Make ID field read-only since it's auto-generated
		txtTenKH = new JTextField(20);
		txtDienThoai = new JTextField(15);
		cboGioiTinh = new JComboBox<>(new String[]{"Nữ", "Nam"});
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setDate(new Date()); // Default to current date
		dateChooser.setPreferredSize(new Dimension(150, 30));

		// Horizontal layout (X axis)
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(lblMaKH)
						.addComponent(lblTenKH)
						.addComponent(lblDienThoai)
						.addComponent(lblGioiTinh)
						.addComponent(lblNgayThamGia))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(txtMaKH)
						.addComponent(txtTenKH)
						.addComponent(txtDienThoai)
						.addComponent(cboGioiTinh)
						.addComponent(dateChooser))


		);

		// Vertical layout (Y axis)
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblMaKH)
						.addComponent(txtMaKH))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTenKH)
						.addComponent(txtTenKH))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDienThoai)
						.addComponent(txtDienThoai))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblGioiTinh)
						.addComponent(cboGioiTinh))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblNgayThamGia)
						.addComponent(dateChooser))

		);

		return pnInfo; // Return the info panel directly
	}


	private JPanel createButtonPanel() {
		JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Increased horizontal gap
		pnButtons.setBackground(BACKGROUND_COLOR);

		btnThem = createButton("Thêm Khách Hàng");
		btnXoa = createButton("Xóa Khách Hàng");
		btnSua = createButton("Sửa Thông Tin");
		btnLamMoi = createButton("Làm Mới");
		btnThoat = createButton("Thoát");

		pnButtons.add(btnThem);
		pnButtons.add(btnXoa);
		pnButtons.add(btnSua);
		pnButtons.add(btnLamMoi);
		pnButtons.add(btnThoat);

		pnButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));  // Add padding above the buttons

		return pnButtons;
	}

	private JScrollPane createTablePanel() {
		String[] columnNames = {"Mã Khách Hàng", "Họ Tên Khách Hàng", "Số Điện Thoại", "Giới Tính", "Ngày Tham Gia"};
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make table cells non-editable
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one row selection
		table.setRowHeight(25); // Increase row height for better readability
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold table header
		table.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				new LineBorder(Color.GRAY), "Danh Sách Khách Hàng",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
				LABEL_FONT, TITLE_COLOR));
		return scrollPane;
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(LABEL_FONT);
		return label;
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setBackground(BUTTON_BG);
		button.setForeground(BUTTON_TEXT);
		button.setFocusPainted(false); // Remove focus highlight
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand when hovering
		
		// Add hover effect
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(BUTTON_BG.darker());
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(BUTTON_BG);
			}
		});
		
		return button;
	}

	private void attachButtonListeners() {
		btnThem.addActionListener(e -> themKhachHang());
		btnXoa.addActionListener(e -> xoaKhachHang());
		btnSua.addActionListener(e -> suaKhachHang());
		btnLamMoi.addActionListener(e -> lamMoiForm());
		btnThoat.addActionListener(e -> {
			Window parentWindow = SwingUtilities.getWindowAncestor(this);
			if (parentWindow != null) {
				parentWindow.dispose(); // Close the current window
			}

            gui_TrangChu mainFrame = null;
            try {
                mainFrame = new gui_TrangChu(loginNV);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            mainFrame.setVisible(true); // Show the main frame
		});
	}

	private void themKhachHang() {
		try {
			String maKH = txtMaKH.getText().trim();
			String tenKH = txtTenKH.getText().trim();
			String dienThoai = txtDienThoai.getText().trim();
			String gioiTinh = cboGioiTinh.getSelectedItem().toString();
			boolean gioiTinhBoolean = gioiTinh.equals("Nữ");
			Date ngayTGUtil = dateChooser.getDate();

			if (ngayTGUtil == null || maKH.isEmpty() || tenKH.isEmpty() || dienThoai.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
				return;
			}



			if (!dienThoai.matches("^0\\d{9}$")) {
				JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
				return;
			}

			LocalDate ngayThamGia = ngayTGUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			KhachHang kh = new KhachHang(maKH, tenKH, dienThoai, gioiTinhBoolean, ngayThamGia);

			if (KHACH_HANG_SEVICE.save(kh)) {
				JOptionPane.showMessageDialog(null, "Đã thêm khách hàng thành công!");
				// Reset the table and form
				DocDuLieuDatabaseVaoTable();
				lamMoiForm();
			} else {
				JOptionPane.showMessageDialog(null, "Thêm thất bại!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng: " + ex.getMessage());
		}
	}

	private void xoaKhachHang() {
		try {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(null, "Chọn một dòng để xóa!");
				return;
			}
			
			String maKH = model.getValueAt(row, 0).toString();
			String tenKH = model.getValueAt(row, 1).toString();
			
			int confirm = JOptionPane.showConfirmDialog(
				null, 
				"Bạn có chắc chắn muốn xóa khách hàng '" + tenKH + "' không?", 
				"Xác nhận xóa", 
				JOptionPane.YES_NO_OPTION
			);
			
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
			
			if (KHACH_HANG_SEVICE.delete(maKH)) {
				DocDuLieuDatabaseVaoTable(); // Refresh the table
				lamMoiForm(); // Reset form
				JOptionPane.showMessageDialog(null, "Xóa thành công!");
			} else {
				JOptionPane.showMessageDialog(null, "Xóa thất bại!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + ex.getMessage());
		}
	}

	private void suaKhachHang() {
		try {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa");
				return;
			}

			String maKH = txtMaKH.getText().trim();
			String tenKH = txtTenKH.getText().trim();
			String dienThoai = txtDienThoai.getText().trim();
			String gioiTinh = cboGioiTinh.getSelectedItem().toString();
			boolean gioiTinhBoolean = gioiTinh.equals("Nữ");
			Date ngayTGUtil = dateChooser.getDate();
			
			if (tenKH.isEmpty() || dienThoai.isEmpty() || ngayTGUtil == null) {
				JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!");
				return;
			}

			if (!dienThoai.matches("^0\\d{9}$")) {
				JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
				return;
			}

			LocalDate ngayThamGia = ngayTGUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			KhachHang kh = new KhachHang(maKH, tenKH, dienThoai, gioiTinhBoolean, ngayThamGia);

			if (KHACH_HANG_SEVICE.update(kh)) {
				DocDuLieuDatabaseVaoTable(); // Refresh the table
				JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
			} else {
				JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + ex.getMessage());
		}
	}
	
	private void lamMoiForm() {
		generateRandomCustomerId();
		txtTenKH.setText("");
		txtDienThoai.setText("");

		cboGioiTinh.setSelectedIndex(0);
		dateChooser.setDate(new Date()); // Reset to current date
		table.clearSelection();
		txtTenKH.requestFocus();
	}
	
	private void generateRandomCustomerId() {
		txtMaKH.setText(RandomMa.maKHAuto());

	}

	private void DocDuLieuDatabaseVaoTable() {
		try {
			model.setRowCount(0);
			for (KhachHang kh : KHACH_HANG_SEVICE.findAll()) {
				model.addRow(new Object[]{
						kh.getId(),
						kh.getHoTen(),
						kh.getSoDienThoai(),
						kh.isGioiTinh() ? "Nữ" : "Nam",
						kh.getNgayThamGia()
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row >= 0) {
			txtMaKH.setText(model.getValueAt(row, 0).toString());
			txtTenKH.setText(model.getValueAt(row, 1).toString());
			txtDienThoai.setText(model.getValueAt(row, 2).toString());
			cboGioiTinh.setSelectedItem(model.getValueAt(row, 3).toString());
			LocalDate ngayThamGia = (LocalDate) model.getValueAt(row, 4);
			Date ngayTGUtil = Date.from(ngayThamGia.atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateChooser.setDate(ngayTGUtil);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}