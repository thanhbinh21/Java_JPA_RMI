package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import entity.Thuoc;
import service.ThongKeThuocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import SLF4J for logging

public class gui_thongKeThuoc extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(gui_thongKeThuoc.class); // Logger
	private JTable table;
	private DefaultTableModel tableModel; // Renamed 'model' to 'tableModel' for clarity
	private JTextField txtTotalQuantity; // Renamed for clarity
	private JTextField txtTotalTypes;   // Renamed for clarity
	private JDateChooser dateChooserStart, dateChooserEnd; // Renamed for clarity
	private JButton btnExpiredDrugs, btnSoldDrugs, btnAvailableDrugs, btnWarehouseStock; // Renamed
	private ThongKeThuocService thongKeThuocService;
	private JPanel mainPanel; // Added a main panel for better layout

	public gui_thongKeThuoc() {
		initRMIServices();
		initializeUI();
		addActionListeners();
	}

	private void initRMIServices() {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 8989);
			thongKeThuocService = (ThongKeThuocService) registry.lookup("ThongKeThuocService");
		} catch (Exception e) {
			logger.error("Error connecting to RMI Server", e); // Log the error
			JOptionPane.showMessageDialog(this, "Error connecting to RMI Server: " + e.getMessage(), "RMI Connection Error", JOptionPane.ERROR_MESSAGE);
			// Consider application exit or retry mechanism here
		}
	}

	private void initializeUI() {
		setLayout(new BorderLayout()); // Use BorderLayout for main panel
		setSize(1400, 800);
		setBackground(SystemColor.controlHighlight);

		mainPanel = new JPanel();
		mainPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		mainPanel.setBackground(SystemColor.controlHighlight);
		mainPanel.setLayout(new GridBagLayout()); // Use GridBagLayout
		add(mainPanel, BorderLayout.CENTER);

		// Header Panel
		JPanel headerPanel = createHeaderPanel();
		add(headerPanel, BorderLayout.NORTH);

		// Form Panel
		JPanel formPanel = createFormPanel();
		add(formPanel, BorderLayout.WEST);

		// Report Panel
		JPanel reportPanel = createReportPanel();
		add(reportPanel, BorderLayout.EAST);

		// Table Panel
		JScrollPane tableScrollPane = createTablePanel();
		add(tableScrollPane, BorderLayout.SOUTH);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		headerPanel.setBackground(SystemColor.controlHighlight);
		headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel titleLabel = new JLabel("THỐNG KÊ TÌNH TRẠNG THUỐC");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(Color.RED);
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 35));
		headerPanel.add(titleLabel);

		return headerPanel;
	}

	private JPanel createFormPanel() {
		JPanel formPanel = new JPanel();
		formPanel.setPreferredSize(new Dimension(700, 250)); // Set preferred size
		formPanel.setBackground(SystemColor.controlHighlight);
		formPanel.setBorder(new TitledBorder(null, "Hình thức thống kê", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		formPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblStartDate = new JLabel("Thời gian từ:");
		lblStartDate.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(lblStartDate, gbc);

		dateChooserStart = new JDateChooser();
		dateChooserStart.setLocale(new Locale("vi", "VN"));
		dateChooserStart.setDateFormatString("dd/MM/yyyy");
		gbc.gridx = 1;
		gbc.gridy = 0;
		formPanel.add(dateChooserStart, gbc);

		JLabel lblEndDate = new JLabel("Đến:");
		lblEndDate.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		gbc.gridx = 2;
		gbc.gridy = 0;
		formPanel.add(lblEndDate, gbc);

		dateChooserEnd = new JDateChooser();
		dateChooserEnd.setLocale(new Locale("vi", "VN"));
		dateChooserEnd.setDateFormatString("dd/MM/yyyy");
		gbc.gridx = 3;
		gbc.gridy = 0;
		formPanel.add(dateChooserEnd, gbc);

		JPanel buttonPanel = createButtonPanel();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		formPanel.add(buttonPanel, gbc);

		return formPanel;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(SystemColor.controlHighlight);
		buttonPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		buttonPanel.setLayout(new GridLayout(2, 2, 5, 5)); // 2 rows, 2 columns, 5px gap

		btnExpiredDrugs = new JButton("Xem thuốc hết hạn");
		btnExpiredDrugs.setFont(new Font("Times New Roman", Font.BOLD, 15));
		buttonPanel.add(btnExpiredDrugs);

		btnSoldDrugs = new JButton("Xem thuốc đã bán");
		btnSoldDrugs.setFont(new Font("Times New Roman", Font.BOLD, 15));
		buttonPanel.add(btnSoldDrugs);

		btnAvailableDrugs = new JButton("Xem thuốc còn hạn sử dụng");
		btnAvailableDrugs.setFont(new Font("Times New Roman", Font.BOLD, 15));
		buttonPanel.add(btnAvailableDrugs);

		btnWarehouseStock = new JButton("Xem thuốc còn lại trong kho");
		btnWarehouseStock.setFont(new Font("Times New Roman", Font.BOLD, 15));
		buttonPanel.add(btnWarehouseStock);

		return buttonPanel;
	}

	private JPanel createReportPanel() {
		JPanel reportPanel = new JPanel();
		reportPanel.setPreferredSize(new Dimension(600, 250));
		reportPanel.setBackground(SystemColor.controlHighlight);
		reportPanel.setBorder(new TitledBorder(null, "Báo cáo sau thống kê", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		reportPanel.setLayout(new GridBagLayout());

		JPanel summaryPanel = createSummaryPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		reportPanel.add(summaryPanel, gbc);

		return reportPanel;
	}

	private JPanel createSummaryPanel() {
		JPanel summaryPanel = new JPanel();
		summaryPanel.setBackground(SystemColor.controlHighlight);
		summaryPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		summaryPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		JLabel lblTotalQuantity = new JLabel("Tổng số lượng thuốc:");
		lblTotalQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		gbc.gridx = 0;
		gbc.gridy = 0;
		summaryPanel.add(lblTotalQuantity, gbc);

		txtTotalQuantity = new JTextField(10);
		txtTotalQuantity.setEditable(false);
		txtTotalQuantity.setForeground(Color.BLUE);
		gbc.gridx = 1;
		gbc.gridy = 0;
		summaryPanel.add(txtTotalQuantity, gbc);

		JLabel lblTotalTypes = new JLabel("Tổng số loại thuốc:");
		lblTotalTypes.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		gbc.gridx = 0;
		gbc.gridy = 1;
		summaryPanel.add(lblTotalTypes, gbc);

		txtTotalTypes = new JTextField(10);
		txtTotalTypes.setEditable(false);
		txtTotalTypes.setForeground(Color.BLUE);
		gbc.gridx = 1;
		gbc.gridy = 1;
		summaryPanel.add(txtTotalTypes, gbc);

		return summaryPanel;
	}

	private JScrollPane createTablePanel() {
		String[] columnNames = { "Mã Thuốc", "Tên Thuốc", "Đơn Vị Tính", "Thành Phần", "Số Lượng Tồn", "Giá Nhập", "Đơn Giá", "Hạn Sử Dụng" };
		tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable(tableModel);
		table.setBackground(new Color(245, 245, 220));
		return new JScrollPane(table);
	}

	private void addActionListeners() {
		btnExpiredDrugs.addActionListener(e -> viewExpiredDrugs());
		btnSoldDrugs.addActionListener(e -> viewSoldDrugs());
		btnAvailableDrugs.addActionListener(e -> viewAvailableDrugs());
		btnWarehouseStock.addActionListener(e -> viewWarehouseStock());
	}

	private java.sql.Date[] getDateRange() {
		if (dateChooserStart.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu!", "Input Error", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		if (dateChooserEnd.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày kết thúc!", "Input Error", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		LocalDate startLocalDate = dateChooserStart.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = dateChooserEnd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		return new java.sql.Date[] { java.sql.Date.valueOf(startLocalDate), java.sql.Date.valueOf(endLocalDate) };
	}

	private void viewExpiredDrugs() {
		java.sql.Date[] dateRange = getDateRange();
		if (dateRange == null) return;

		try {
			List<Thuoc> expiredDrugs = thongKeThuocService.danhSachThuocHetHan(dateRange[0], dateRange[1]);
			if (expiredDrugs.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không có thuốc hết hạn trong khoảng thời gian này.", "No Data", JOptionPane.INFORMATION_MESSAGE);
				clearReport();
				return;
			}
			updateTableAndTotals(expiredDrugs);
		} catch (Exception ex) {
			logger.error("Error fetching expired drugs", ex);
			JOptionPane.showMessageDialog(this, "Error fetching expired drugs: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			clearReport();
		}
	}

	private void viewSoldDrugs() {
		java.sql.Date[] dateRange = getDateRange();
		if (dateRange == null) return;

		try {
			List<Thuoc> soldDrugs = thongKeThuocService.danhSachThuocDaBan(dateRange[0], dateRange[1]);
			if (soldDrugs.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không có thuốc đã bán vào thời gian này.", "No Data", JOptionPane.INFORMATION_MESSAGE);
				clearReport();
				return;
			}

			int totalQuantity = soldDrugs.stream().mapToInt(Thuoc::getSoLuongTon).sum();
			int totalTypes = thongKeThuocService.tinhTongLoaiThuocDaBan(dateRange[0], dateRange[1]);

			updateTable(soldDrugs);
			txtTotalQuantity.setText(String.valueOf(totalQuantity));
			txtTotalTypes.setText(String.valueOf(totalTypes));

		} catch (Exception ex) {
			logger.error("Error fetching sold drugs", ex);
			JOptionPane.showMessageDialog(this, "Error fetching sold drugs: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			clearReport();
		}
	}

	private void viewAvailableDrugs() {
		java.sql.Date[] dateRange = getDateRange();
		if (dateRange == null) return;

		try {
			List<Thuoc> availableDrugs = thongKeThuocService.danhSachThuocConLai(dateRange[0], dateRange[1]);
			if (availableDrugs.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không có thuốc còn hạn trong khoảng thời gian này.", "No Data", JOptionPane.INFORMATION_MESSAGE);
				clearReport();
				return;
			}
			updateTableAndTotals(availableDrugs);
		} catch (Exception ex) {
			logger.error("Error fetching available drugs", ex);
			JOptionPane.showMessageDialog(this, "Error fetching available drugs: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			clearReport();
		}
	}

	private void viewWarehouseStock() {
		java.sql.Date[] dateRange = getDateRange();
		if (dateRange == null) return;

		try {
			List<Thuoc> warehouseStock = thongKeThuocService.danhsachThuocConLaiTrongKho(dateRange[0], dateRange[1]);
			if (warehouseStock.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Trong kho đã hết thuốc cho khoảng thời gian này.", "No Data", JOptionPane.INFORMATION_MESSAGE);
				clearReport();
				return;
			}
			updateTableAndTotals(warehouseStock);
		} catch (Exception ex) {
			logger.error("Error fetching warehouse stock", ex);
			JOptionPane.showMessageDialog(this, "Error fetching warehouse stock: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			clearReport();
		}
	}

	private void updateTableAndTotals(List<Thuoc> drugList) {
		int totalQuantity = 0;
		int totalTypes = 0;

		for (Thuoc drug : drugList) {
			totalTypes++;
			totalQuantity += drug.getSoLuongTon();
		}

		updateTable(drugList);
		txtTotalQuantity.setText(String.valueOf(totalQuantity));
		txtTotalTypes.setText(String.valueOf(totalTypes));
	}

	private void updateTable(List<Thuoc> drugList) {
		tableModel.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (Thuoc drug : drugList) {
			String expirationDate = drug.getHanSuDung() != null ? sdf.format(drug.getHanSuDung()) : "N/A";
			tableModel.addRow(new Object[]{
					drug.getId(), drug.getTen(), drug.getDonViTinh(), drug.getThanhPhan(),
					drug.getSoLuongTon(), drug.getKhuyenMai(), drug.getDonGia(), expirationDate
			});
		}
	}

	private void clearReport() {
		tableModel.setRowCount(0);
		txtTotalQuantity.setText("");
		txtTotalTypes.setText("");
	}
}