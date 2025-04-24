package gui;

import dao.*;
import dao.impl.*;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import service.ChiTietPhieuNhapThuocService;
import service.PhieuNhapThuocService;
import service.impl.ChiTietPhieuNhapThuocServiceImpl;
import service.impl.PhieuNhapThuocServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class gui_timKiemPhieuNhap extends JPanel implements ActionListener {
    private JPanel pnlMain;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;
    
    // Search components
    private JLabel lblTitle;
    private JLabel lblReceiptId;
    private JLabel lblSupplier;
    private JLabel lblDateFrom;
    private JLabel lblDateTo;
    private JTextField txtReceiptId;
    private JTextField txtSupplier;
    private JTextField txtDateFrom;
    private JTextField txtDateTo;
    private JButton btnSearch;
    private JButton btnClear;
    
    // Result tables
    private JTable tblReceipts;
    private DefaultTableModel receiptTableModel;
    private JScrollPane scrollReceipts;
    
    // Detail panel and table
    private JPanel pnlDetails;
    private JTable tblDetails;
    private DefaultTableModel detailTableModel;
    private JScrollPane scrollDetails;
    
    // Services
    private PhieuNhapThuocService phieuNhapThuocService;
    private ChiTietPhieuNhapThuocService chiTietPhieuNhapThuocService;
    
    // Date formatters
    private DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public gui_timKiemPhieuNhap() {
        // Initialize services
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            phieuNhapThuocService = (PhieuNhapThuocService) registry.lookup("PhieuNhapThuocService");
            chiTietPhieuNhapThuocService = (ChiTietPhieuNhapThuocService) registry.lookup("ChiTietPhieuNhapThuocService");


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing services: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create UI
        createUI();
        
        // Load sample data for testing
        loadSampleData();
    }
    
    private void createUI() {
        // Set layout for this panel
        setLayout(new BorderLayout());
        
        // Main panel with BorderLayout
        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(pnlMain, BorderLayout.CENTER);
        
        // North panel - Title and Search
        createNorthPanel();
        
        // Center panel - Results
        createCenterPanel();
        
        // South panel - Details
        createSouthPanel();
    }
    
    private void createNorthPanel() {
        pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Title
        JPanel pnlTitle = new JPanel();
        lblTitle = new JLabel("TÌM KIẾM PHIẾU NHẬP THUỐC");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTitle.add(lblTitle);
        pnlNorth.add(pnlTitle, BorderLayout.NORTH);
        
        // Search criteria panel
        JPanel pnlCriteria = new JPanel(new GridBagLayout());
        pnlCriteria.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Điều Kiện Tìm Kiếm", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Receipt ID
        lblReceiptId = new JLabel("Mã Phiếu Nhập:");
        lblReceiptId.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlCriteria.add(lblReceiptId, gbc);
        
        txtReceiptId = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlCriteria.add(txtReceiptId, gbc);
        
        // Supplier
        lblSupplier = new JLabel("Tên Nhà Cung Cấp:");
        lblSupplier.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        pnlCriteria.add(lblSupplier, gbc);
        
        txtSupplier = new JTextField(15);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlCriteria.add(txtSupplier, gbc);
        
        // Date From
        lblDateFrom = new JLabel("Từ Ngày:");
        lblDateFrom.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlCriteria.add(lblDateFrom, gbc);
        
        txtDateFrom = new JTextField(15);
        txtDateFrom.setToolTipText("Định dạng: dd/MM/yyyy");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlCriteria.add(txtDateFrom, gbc);
        
        // Date To
        lblDateTo = new JLabel("Đến Ngày:");
        lblDateTo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlCriteria.add(lblDateTo, gbc);
        
        txtDateTo = new JTextField(15);
        txtDateTo.setToolTipText("Định dạng: dd/MM/yyyy");
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlCriteria.add(txtDateTo, gbc);
        
        // Button panel
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setIcon(new ImageIcon(getClass().getResource("/icon/search.png")));
        btnSearch.addActionListener(this);
        
        btnClear = new JButton("Làm Mới");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.addActionListener(this);
        
        pnlButtons.add(btnSearch);
        pnlButtons.add(btnClear);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        pnlCriteria.add(pnlButtons, gbc);
        
        pnlNorth.add(pnlCriteria, BorderLayout.CENTER);
        pnlMain.add(pnlNorth, BorderLayout.NORTH);
    }
    
    private void createCenterPanel() {
        pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Kết Quả Tìm Kiếm", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        // Receipts table
        String[] receiptColumns = {"Mã Phiếu", "Ngày Lập", "Nhà Cung Cấp", "Nhân Viên", "Tổng Tiền"};
        receiptTableModel = new DefaultTableModel(receiptColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblReceipts = new JTable(receiptTableModel);
        tblReceipts.setFont(new Font("Arial", Font.PLAIN, 14));
        tblReceipts.setRowHeight(25);
        tblReceipts.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblReceipts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReceipts.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        tblReceipts.getColumnModel().getColumn(0).setPreferredWidth(100);  // ID
        tblReceipts.getColumnModel().getColumn(1).setPreferredWidth(150);  // Date
        tblReceipts.getColumnModel().getColumn(2).setPreferredWidth(200);  // Supplier
        tblReceipts.getColumnModel().getColumn(3).setPreferredWidth(200);  // Employee
        tblReceipts.getColumnModel().getColumn(4).setPreferredWidth(120);  // Total Amount
        
        scrollReceipts = new JScrollPane(tblReceipts);
        scrollReceipts.setPreferredSize(new Dimension(800, 200));
        pnlCenter.add(scrollReceipts, BorderLayout.CENTER);
        
        // Add table selection listener
        tblReceipts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblReceipts.getSelectedRow();
                if (selectedRow >= 0) {
                    String receiptId = tblReceipts.getValueAt(selectedRow, 0).toString();
                    loadReceiptDetails(receiptId);
                }
            }
        });
        
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
    }
    
    private void createSouthPanel() {
        pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Chi Tiết Phiếu Nhập", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        // Details table
        String[] detailColumns = {"Thuốc", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        detailTableModel = new DefaultTableModel(detailColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblDetails = new JTable(detailTableModel);
        tblDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        tblDetails.setRowHeight(25);
        tblDetails.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDetails.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(300);  // Medicine
        tblDetails.getColumnModel().getColumn(1).setPreferredWidth(80);   // Quantity
        tblDetails.getColumnModel().getColumn(2).setPreferredWidth(100);  // Unit Price
        tblDetails.getColumnModel().getColumn(3).setPreferredWidth(120);  // Total
        
        scrollDetails = new JScrollPane(tblDetails);
        scrollDetails.setPreferredSize(new Dimension(800, 150));
        pnlSouth.add(scrollDetails, BorderLayout.CENTER);
        
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);
    }
    
    private void searchReceipts() {
        try {
            // Clear previous results
            receiptTableModel.setRowCount(0);
            detailTableModel.setRowCount(0);
            
            // Get search criteria
            String receiptId = txtReceiptId.getText().trim();
            String supplierName = txtSupplier.getText().trim();
            LocalDateTime fromDate = null;
            LocalDateTime toDate = null;
            
            try {
                if (!txtDateFrom.getText().trim().isEmpty()) {
                    LocalDate date = LocalDate.parse(txtDateFrom.getText().trim(), displayDateFormatter);
                    fromDate = date.atStartOfDay();
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng dd/MM/yyyy");
                return;
            }
            
            try {
                if (!txtDateTo.getText().trim().isEmpty()) {
                    LocalDate date = LocalDate.parse(txtDateTo.getText().trim(), displayDateFormatter);
                    toDate = date.plusDays(1).atStartOfDay();  // Include the entire day
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng dd/MM/yyyy");
                return;
            }
            
            System.out.println("Tìm kiếm với điều kiện: Mã=" + receiptId + ", Nhà cung cấp=" + supplierName);
            
            // If no criteria provided, search all
            List<PhieuNhapThuoc> receipts;
            if (receiptId.isEmpty() && supplierName.isEmpty() && fromDate == null && toDate == null) {
                System.out.println("Không có điều kiện, lấy tất cả phiếu nhập");
                receipts = phieuNhapThuocService.findAll();
            } else {
                // Get receipts based on criteria
                receipts = phieuNhapThuocService.searchPhieuNhap(receiptId, supplierName, fromDate, toDate);
            }
            
            System.out.println("Tìm thấy " + receipts.size() + " phiếu nhập");
            
            if (receipts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu nhập phù hợp với điều kiện tìm kiếm");
                return;
            }
            
            // Add receipts to table
            for (PhieuNhapThuoc receipt : receipts) {
                double totalAmount = 0;
                try {
                    totalAmount = chiTietPhieuNhapThuocService.calculateTotalAmount(receipt.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                String formattedDate = receipt.getThoiGian() != null 
                        ? receipt.getThoiGian().format(fullDateTimeFormatter) 
                        : "";
                
                Object[] row = {
                    receipt.getId(),
                    formattedDate,
                    receipt.getNhaCungCap() != null ? receipt.getNhaCungCap().getTen() : "",
                    receipt.getNhanVien() != null ? receipt.getNhanVien().getHoTen() : "",
                    String.format("%,.0f", totalAmount)
                };
                receiptTableModel.addRow(row);
                System.out.println("Đã thêm phiếu nhập: " + receipt.getId());
            }
            
            // Select first row by default and load its details
            if (tblReceipts.getRowCount() > 0) {
                tblReceipts.setRowSelectionInterval(0, 0);
                String receiptIdToLoad = tblReceipts.getValueAt(0, 0).toString();
                System.out.println("Đang tải chi tiết cho phiếu nhập: " + receiptIdToLoad);
                loadReceiptDetails(receiptIdToLoad);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phiếu nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadReceiptDetails(String receiptId) {
        try {
            System.out.println("Đang tải chi tiết cho phiếu nhập ID: " + receiptId);
            
            // Clear existing details
            detailTableModel.setRowCount(0);
            
            // Get receipt details
            List<ChiTietPhieuNhapThuoc> details = chiTietPhieuNhapThuocService.findByPhieuNhapThuoc(receiptId);
            System.out.println("Tìm thấy " + details.size() + " chi tiết phiếu nhập");
            
            if (details.isEmpty()) {
                System.out.println("Không tìm thấy chi tiết cho phiếu nhập ID: " + receiptId);
                return;
            }
            
            // Add details to table
            double totalAmount = 0;
            for (ChiTietPhieuNhapThuoc detail : details) {
                double total = detail.getSoLuong() * detail.getDonGia();
                totalAmount += total;
                
                String medicineName = detail.getThuoc() != null ? detail.getThuoc().getTen() : "Không xác định";
                System.out.println("Thêm chi tiết: " + medicineName + ", Số lượng: " + detail.getSoLuong());
                
                Object[] row = {
                    medicineName,
                    detail.getSoLuong(),
                    String.format("%,.0f", detail.getDonGia()),
                    String.format("%,.0f", total)
                };
                detailTableModel.addRow(row);
            }
            
            System.out.println("Tổng tiền phiếu nhập: " + totalAmount);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải chi tiết phiếu nhập: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết phiếu nhập: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        // Clear search fields
        txtReceiptId.setText("");
        txtSupplier.setText("");
        txtDateFrom.setText("");
        txtDateTo.setText("");
        
        // Clear both tables
        receiptTableModel.setRowCount(0);
        detailTableModel.setRowCount(0);
        
        // Reload sample data or all data instead of just clearing tables
        try {
            // Reload all receipts
            List<PhieuNhapThuoc> allReceipts = phieuNhapThuocService.findAll();
            
            // If database has data, show it
            if (allReceipts.size() > 0) {
                searchReceipts(); // This will reload with empty criteria
            } else {
                // Otherwise show sample data
                loadSampleData();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải lại dữ liệu sau khi làm mới: " + e.getMessage());
            e.printStackTrace();
            // If error, just load sample data
            loadSampleData();
        }
    }
    
    // Method to load sample data for testing
    private void loadSampleData() {
        try {
            // Add some rows to the receipts table for visual testing
            receiptTableModel.setRowCount(0);
            
            // Get a list of all receipts - this tests if the service connection works
            List<PhieuNhapThuoc> allReceipts = phieuNhapThuocService.findAll();
            if (allReceipts.size() > 0) {
                System.out.println("Found " + allReceipts.size() + " existing receipts in database");
                for (PhieuNhapThuoc receipt : allReceipts) {
                    System.out.println("Receipt ID: " + receipt.getId() + 
                                     ", Supplier: " + (receipt.getNhaCungCap() != null ? receipt.getNhaCungCap().getTen() : "N/A"));
                }
                // Now perform a search with empty criteria, which should show all receipts
                searchReceipts();
                return;
            }
            
            // If no receipts were found, add sample data to at least show the tables working
            System.out.println("No receipts found in database, adding sample data for visual testing");
            
            Object[][] sampleReceipts = {
                {"PN20240501001", "2024-05-01 10:30", "Supplier A", "Employee 1", "1,250,000"},
                {"PN20240502002", "2024-05-02 14:15", "Supplier B", "Employee 2", "2,345,000"},
                {"PN20240503003", "2024-05-03 09:45", "Supplier C", "Employee 1", "980,000"}
            };
            
            for (Object[] row : sampleReceipts) {
                receiptTableModel.addRow(row);
            }
            
            // Add sample details for the first receipt
            detailTableModel.setRowCount(0);
            
            Object[][] sampleDetails = {
                {"Paracetamol 500mg", 200, "5,000", "1,000,000"},
                {"Vitamin C 1000mg", 50, "5,000", "250,000"}
            };
            
            for (Object[] row : sampleDetails) {
                detailTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            searchReceipts();
        } else if (e.getSource() == btnClear) {
            clearForm();
        }
    }
} 