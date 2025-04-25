package gui;

import dao.NhanVienDAO;
import entity.*;
import service.ChiTietPhieuNhapThuocService;
import service.NhaCungCapService;
import service.PhieuNhapThuocService;
import service.ThuocService;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class gui_nhapThuoc extends JPanel implements ActionListener {
    // UI Components
    private JPanel pnlMain;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;
    
    private JLabel lblTitle;
    private JTabbedPane tabPane;
    
    // Receipt panel components
    private JPanel pnlReceipt;
    private JLabel lblReceiptId;
    private JLabel lblReceiptDate;
    private JLabel lblSupplier;
    private JTextField txtReceiptId;
    private JTextField txtReceiptDate;
    private JComboBox<NhaCungCap> cboSupplier;
    private JButton btnGenerateId;
    private JButton btnCurrentDate;
    private JLabel lblTongTien;
    private JTextField txtTongTien;
    private JButton btnThanhToan;
    private double totalAmount = 0.0;
    
    // Payment section in details panel
    private JTextField txtDetailTongTien;
    private JButton btnDetailThanhToan;
    
    // Receipt details panel components
    private JPanel pnlDetails;
    private JComboBox<Thuoc> cboMedicine;
    private JTextField txtQuantity;
    private JTextField txtUnitPrice;
    private JButton btnAddDetail;
    private JButton btnRemoveDetail;
    
    // Table for receipts
    private JTable tblReceipts;
    private DefaultTableModel receiptTableModel;
    
    // Table for receipt details
    private JTable tblDetails;
    private DefaultTableModel detailTableModel;
    
    // Action buttons
    private JButton btnNewReceipt;
    private JButton btnClear;
    private JButton btnSearch;
    private JTextField txtSearch;
    
    // Services
    private PhieuNhapThuocService phieuNhapThuocService;
    private NhaCungCapService nhaCungCapService;
    private ThuocService thuocService;
    private ChiTietPhieuNhapThuocService chiTietPhieuNhapThuocService;
    
    // Current working objects
    private PhieuNhapThuoc currentReceipt;
    private List<ChiTietPhieuNhapThuoc> currentDetails;
    
    // Date formatter
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    // Added NhanVienDAO field
    private NhanVienDAO nhanVienDAO;
    
    // Store reference to logged in account for employee association
    private TaiKhoan loginAccount;
    
    public gui_nhapThuoc(TaiKhoan loginAccount) {
        this.loginAccount = loginAccount;
        
        // Initialize services
        initializeServices();
        
        // Initialize UI
        createUI();
        
        // Load initial data
        loadReceiptData();
        loadComboBoxData();
        
        // Initialize current objects
        currentDetails = new ArrayList<>();
    }
    
    private void initializeServices() {
        try {
            Registry registry = LocateRegistry.getRegistry(BinhCode.HOST, 8989);
            nhaCungCapService = (NhaCungCapService) registry.lookup( "NhaCungCapService") ;
            thuocService = (ThuocService) registry.lookup(  "ThuocService");
            phieuNhapThuocService = (PhieuNhapThuocService) registry.lookup( "PhieuNhapThuocService");
            chiTietPhieuNhapThuocService = (ChiTietPhieuNhapThuocService) registry.lookup(  "ChiTietPhieuNhapThuocService");


//            // Initialize DAOs
//            NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl();
//            PhieuNhapThuocDAO phieuNhapThuocDAO = new PhieuNhapThuocDAOImpl();
//            ThuocDAO thuocDAO = new ThuocDAOImpl();
//            ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO = new ChiTietPhieuNhapThuocDAOImpl();
//            DanhMucDAO danhMucDAO = new DanhMucDAOImpl();
//            NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();
//
//            // Initialize services
//            nhaCungCapService = new NhaCungCapServiceImpl(nhaCungCapDAO);
//            thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO, null, null);
//            phieuNhapThuocService = new PhieuNhapThuocServiceImpl(
//                    phieuNhapThuocDAO, nhaCungCapDAO, nhanVienDAO, chiTietPhieuNhapThuocDAO, thuocDAO);
//            chiTietPhieuNhapThuocService = new ChiTietPhieuNhapThuocServiceImpl(
//                    chiTietPhieuNhapThuocDAO, phieuNhapThuocDAO, thuocDAO);
//
//            // Assign NhanVienDAO to the class field
//            this.nhanVienDAO = nhanVienDAO;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo dịch vụ: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createUI() {
        // Set layout for this panel
        setLayout(new BorderLayout());
        
        // Main panel
        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(pnlMain, BorderLayout.CENTER);
        
        // North panel - Title
        pnlNorth = new JPanel();
        lblTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP THUỐC");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle);
        pnlMain.add(pnlNorth, BorderLayout.NORTH);
        
        // Center panel - Tabbed pane
        pnlCenter = new JPanel(new BorderLayout(0, 10));
        tabPane = new JTabbedPane();
        pnlCenter.add(tabPane, BorderLayout.CENTER);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        
        // Create receipt management tab
        createReceiptPanel();
        
        // Create receipt details tab
        createDetailsPanel();
        
        // Add tabs to tabbed pane
        tabPane.addTab("Thông Tin Phiếu Nhập", pnlReceipt);
        tabPane.addTab("Chi Tiết Phiếu Nhập", pnlDetails);
        
        // Create south panel - Action buttons
        createButtonPanel();
    }
    
    private void createReceiptPanel() {
        pnlReceipt = new JPanel(new BorderLayout(10, 10));
        pnlReceipt.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông Tin Phiếu Nhập", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Receipt ID
        lblReceiptId = new JLabel("Mã Phiếu:");
        lblReceiptId.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlForm.add(lblReceiptId, gbc);
        
        JPanel pnlReceiptId = new JPanel(new BorderLayout(5, 0));
        txtReceiptId = new JTextField(15);
        btnGenerateId = new JButton("Tạo Mã");
        btnGenerateId.setFont(new Font("Arial", Font.PLAIN, 12));
        pnlReceiptId.add(txtReceiptId, BorderLayout.CENTER);
        pnlReceiptId.add(btnGenerateId, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlForm.add(pnlReceiptId, gbc);
        
        // Date
        lblReceiptDate = new JLabel("Ngày lập:");
        lblReceiptDate.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlForm.add(lblReceiptDate, gbc);
        
        JPanel pnlDate = new JPanel(new BorderLayout(5, 0));
        txtReceiptDate = new JTextField(15);
        btnCurrentDate = new JButton("Hiện Tại");
        btnCurrentDate.setFont(new Font("Arial", Font.PLAIN, 12));
        pnlDate.add(txtReceiptDate, BorderLayout.CENTER);
        pnlDate.add(btnCurrentDate, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlForm.add(pnlDate, gbc);
        
        // Supplier
        lblSupplier = new JLabel("Nhà Cung Cấp:");
        lblSupplier.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        pnlForm.add(lblSupplier, gbc);
        
        cboSupplier = new JComboBox<>();
        cboSupplier.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        pnlForm.add(cboSupplier, gbc);
        
        // Employee information
        JLabel lblEmployee = new JLabel("Nhân Viên:");
        lblEmployee.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        pnlForm.add(lblEmployee, gbc);
        
        // Display current employee info
        JTextField txtEmployee = new JTextField();
        txtEmployee.setEditable(false);
        txtEmployee.setBackground(new Color(240, 240, 240));
        txtEmployee.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Set employee name from login account
        if (loginAccount != null && loginAccount.getNhanVien() != null) {
            txtEmployee.setText(loginAccount.getNhanVien().getHoTen() + " (" + loginAccount.getNhanVien().getId() + ")");
        } else {
            txtEmployee.setText("Chưa xác định");
        }
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        pnlForm.add(txtEmployee, gbc);
        
        // Total Amount
        lblTongTien = new JLabel("Tổng Tiền:");
        lblTongTien.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        pnlForm.add(lblTongTien, gbc);
        
        JPanel pnlTongTien = new JPanel(new BorderLayout(5, 0));
        txtTongTien = new JTextField(15);
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        btnThanhToan = new JButton("Thanh Toán");
        btnThanhToan.setFont(new Font("Arial", Font.PLAIN, 14));
        btnThanhToan.addActionListener(this);
        pnlTongTien.add(txtTongTien, BorderLayout.CENTER);
//        pnlTongTien.add(btnThanhToan, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        pnlForm.add(pnlTongTien, gbc);
        
        // Search panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch = new JTextField(25);
        btnSearch = new JButton("Tìm");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        
        // Add form and search to north of receipt panel
        JPanel pnlReceiptNorth = new JPanel(new BorderLayout(0, 10));
        pnlReceiptNorth.add(pnlForm, BorderLayout.CENTER);
        pnlReceiptNorth.add(pnlSearch, BorderLayout.SOUTH);
        pnlReceipt.add(pnlReceiptNorth, BorderLayout.NORTH);
        
        // Table for receipts
        String[] receiptColumns = {"Mã Phiếu", "Ngày Lập", "Nhà Cung Cấp", "Tổng Tiền"};
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
        
        // Set column widths
        tblReceipts.getColumnModel().getColumn(0).setPreferredWidth(100);  // ID
        tblReceipts.getColumnModel().getColumn(1).setPreferredWidth(150);  // Date
        tblReceipts.getColumnModel().getColumn(2).setPreferredWidth(250);  // Supplier
        tblReceipts.getColumnModel().getColumn(3).setPreferredWidth(120);  // Total Amount
        
        JScrollPane scrollReceipts = new JScrollPane(tblReceipts);
        scrollReceipts.setBorder(BorderFactory.createEtchedBorder());
        pnlReceipt.add(scrollReceipts, BorderLayout.CENTER);
        
        // Add receipt table selection listener
        tblReceipts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblReceipts.getSelectedRow();
                if (selectedRow >= 0) {
                    String receiptId = tblReceipts.getValueAt(selectedRow, 0).toString();
                    displayReceiptInfo(receiptId);
                }
            }
        });
    }
    
    private void createDetailsPanel() {
        pnlDetails = new JPanel(new BorderLayout(10, 10));
        pnlDetails.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Form panel for adding details
        JPanel pnlDetailForm = new JPanel(new GridBagLayout());
        pnlDetailForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thêm Chi Tiết Phiếu Nhập", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Medicine
        JLabel lblMedicine = new JLabel("Thuốc:");
        lblMedicine.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlDetailForm.add(lblMedicine, gbc);
        
        cboMedicine = new JComboBox<>();
        cboMedicine.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlDetailForm.add(cboMedicine, gbc);
        
        // Quantity
        JLabel lblQuantity = new JLabel("Số Lượng:");
        lblQuantity.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlDetailForm.add(lblQuantity, gbc);
        
        txtQuantity = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlDetailForm.add(txtQuantity, gbc);
        
        // Unit Price
        JLabel lblUnitPrice = new JLabel("Đơn Giá:");
        lblUnitPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        pnlDetailForm.add(lblUnitPrice, gbc);
        
        txtUnitPrice = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        pnlDetailForm.add(txtUnitPrice, gbc);
        
        // Button panel
        JPanel pnlDetailButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddDetail = new JButton("Thêm Chi Tiết");
        btnAddDetail.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRemoveDetail = new JButton("Xóa Chi Tiết");
        btnRemoveDetail.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pnlDetailButtons.add(btnAddDetail);
        pnlDetailButtons.add(Box.createHorizontalStrut(10));
        pnlDetailButtons.add(btnRemoveDetail);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        pnlDetailForm.add(pnlDetailButtons, gbc);
        
        pnlDetails.add(pnlDetailForm, BorderLayout.NORTH);
        
        // Table for details
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
        
        // Set column widths
        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(300);  // Medicine
        tblDetails.getColumnModel().getColumn(1).setPreferredWidth(80);   // Quantity
        tblDetails.getColumnModel().getColumn(2).setPreferredWidth(100);  // Unit Price
        tblDetails.getColumnModel().getColumn(3).setPreferredWidth(120);  // Total
        
        JScrollPane scrollDetails = new JScrollPane(tblDetails);
        scrollDetails.setBorder(BorderFactory.createEtchedBorder());
        pnlDetails.add(scrollDetails, BorderLayout.CENTER);
        
        // Payment Section
        JPanel pnlPayment = new JPanel(new GridBagLayout());
        pnlPayment.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông Tin Thanh Toán", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
                
        GridBagConstraints gbcPayment = new GridBagConstraints();
        gbcPayment.insets = new Insets(5, 5, 5, 5);
        gbcPayment.fill = GridBagConstraints.HORIZONTAL;
        gbcPayment.anchor = GridBagConstraints.WEST;
        
        // Total Amount
        JLabel lblDetailTongTien = new JLabel("Tổng Tiền:");
        lblDetailTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        gbcPayment.gridx = 0;
        gbcPayment.gridy = 0;
        gbcPayment.weightx = 0;
        pnlPayment.add(lblDetailTongTien, gbcPayment);
        
        txtDetailTongTien = new JTextField(15);
        txtDetailTongTien.setEditable(false);
        txtDetailTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        txtDetailTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtDetailTongTien.setBackground(new Color(240, 240, 240));
        
        gbcPayment.gridx = 1;
        gbcPayment.gridy = 0;
        gbcPayment.weightx = 1.0;
        pnlPayment.add(txtDetailTongTien, gbcPayment);
        
        // Payment Button
        btnDetailThanhToan = new JButton("Thanh Toán");
        btnDetailThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
        btnDetailThanhToan.addActionListener(e -> saveReceipt());
        
        gbcPayment.gridx = 0;
        gbcPayment.gridy = 1;
        gbcPayment.gridwidth = 2;
        gbcPayment.weightx = 1.0;
        gbcPayment.fill = GridBagConstraints.NONE;
        gbcPayment.anchor = GridBagConstraints.CENTER;
        pnlPayment.add(btnDetailThanhToan, gbcPayment);
        
        // Add payment panel to bottom of details panel
        pnlDetails.add(pnlPayment, BorderLayout.SOUTH);
    }
    
    private void createButtonPanel() {
        pnlSouth = new JPanel();
        pnlSouth.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create buttons with uniform size and style
        Dimension buttonSize = new Dimension(150, 35);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        
        btnNewReceipt = new JButton("Tạo Phiếu Mới");
        btnNewReceipt.setFont(buttonFont);
        btnNewReceipt.setPreferredSize(buttonSize);
        
        btnClear = new JButton("Làm Mới");
        btnClear.setFont(buttonFont);
        btnClear.setPreferredSize(buttonSize);
        
        // Add buttons to panel with spacing
        pnlSouth.add(btnNewReceipt);
        pnlSouth.add(Box.createHorizontalStrut(10));
        pnlSouth.add(btnClear);
        
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);
        
        // Add action listeners
        btnGenerateId.addActionListener(this);
        btnCurrentDate.addActionListener(this);
        btnSearch.addActionListener(this);
        btnNewReceipt.addActionListener(this);
        btnClear.addActionListener(this);
        btnAddDetail.addActionListener(this);
        btnRemoveDetail.addActionListener(this);
    }
    
    private void loadComboBoxData() {
        try {
            // Load suppliers
            List<NhaCungCap> suppliers = nhaCungCapService.findAll();
            cboSupplier.removeAllItems();
            for (NhaCungCap supplier : suppliers) {
                cboSupplier.addItem(supplier);
            }
            
            // Load medicines
            List<Thuoc> medicines = thuocService.findAll();
            cboMedicine.removeAllItems();
            for (Thuoc medicine : medicines) {
                cboMedicine.addItem(medicine);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadReceiptData() {
        try {
            // Clear existing table data
            receiptTableModel.setRowCount(0);
            
            // Get all receipts
            List<PhieuNhapThuoc> receipts = phieuNhapThuocService.findAll();
            
            // Add receipts to table
            for (PhieuNhapThuoc receipt : receipts) {
                double totalAmount = 0;
                try {
                    totalAmount = chiTietPhieuNhapThuocService.calculateTotalAmount(receipt.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                String formattedDate = receipt.getThoiGian() != null 
                        ? receipt.getThoiGian().format(dateFormatter) 
                        : "";
                
                Object[] row = {
                    receipt.getId(),
                    formattedDate,
                    receipt.getNhaCungCap() != null ? receipt.getNhaCungCap().getTen() : "",
                    String.format("%,.0f", totalAmount)
                };
                receiptTableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu phiếu nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Display receipt information in the receipt panel without switching to details tab
     * Also loads and displays the receipt details in the details table
     */
    private void displayReceiptInfo(String receiptId) {
        try {
            // Load receipt information
            PhieuNhapThuoc receipt = phieuNhapThuocService.findById(receiptId);
            if (receipt == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu nhập");
                return;
            }
            
            // Set receipt info to form
            txtReceiptId.setText(receipt.getId());
            txtReceiptId.setEditable(false);  // Disable editing of existing receipt ID
            
            if (receipt.getThoiGian() != null) {
                txtReceiptDate.setText(receipt.getThoiGian().format(dateFormatter));
            }
            
            // Select supplier in combo box
            if (receipt.getNhaCungCap() != null) {
                for (int i = 0; i < cboSupplier.getItemCount(); i++) {
                    NhaCungCap supplier = cboSupplier.getItemAt(i);
                    if (supplier.getId().equals(receipt.getNhaCungCap().getId())) {
                        cboSupplier.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Store current receipt
            currentReceipt = receipt;
            
            // Load receipt details
            List<ChiTietPhieuNhapThuoc> details = 
                    chiTietPhieuNhapThuocService.findByPhieuNhapThuoc(receiptId);
            
            // Store current details
            currentDetails = new ArrayList<>(details);
            
            // Update details table
            updateDetailsTable();
            
            // Calculate and update total amount
            calculateAndUpdateTotalAmount();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin phiếu nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateDetailsTable() {
        // Clear existing details
        detailTableModel.setRowCount(0);
        
        // Add current details to table
        for (ChiTietPhieuNhapThuoc detail : currentDetails) {
            double total = detail.getSoLuong() * detail.getDonGia();
            
            Object[] row = {
                detail.getThuoc().getTen(),
                detail.getSoLuong(),
                String.format("%,.0f", detail.getDonGia()),
                String.format("%,.0f", total)
            };
            detailTableModel.addRow(row);
        }
    }
    
    private void addDetail() {
        // Validate current receipt
        if (currentReceipt == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng tạo hoặc chọn một phiếu nhập trước");
            return;
        }
        
        // Get selected medicine
        Thuoc medicine = (Thuoc) cboMedicine.getSelectedItem();
        if (medicine == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc");
            return;
        }
        
        // Validate quantity
        int quantity;
        try {
            quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ");
            return;
        }
        
        // Validate unit price
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(txtUnitPrice.getText().trim().replace(",", ""));
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá hợp lệ");
            return;
        }
        
        // Check if medicine already exists in the details
        for (int i = 0; i < currentDetails.size(); i++) {
            ChiTietPhieuNhapThuoc detail = currentDetails.get(i);
            if (detail.getThuoc().getId().equals(medicine.getId())) {
                // Update existing detail
                detail.setSoLuong(detail.getSoLuong() + quantity);
                detail.setDonGia(unitPrice);
                
                // Update table
                updateDetailsTable();
                
                // Calculate and update total amount
                calculateAndUpdateTotalAmount();
                
                // Clear input fields
                txtQuantity.setText("");
                txtUnitPrice.setText("");
                
                return;
            }
        }
        
        // Create new detail
        ChiTietPhieuNhapThuoc detail = new ChiTietPhieuNhapThuoc();
        detail.setPhieuNhapThuoc(currentReceipt);
        detail.setThuoc(medicine);
        detail.setSoLuong(quantity);
        detail.setDonGia(unitPrice);
        
        // Add to current details
        currentDetails.add(detail);
        
        // Update table
        updateDetailsTable();
        
        // Calculate and update total amount
        calculateAndUpdateTotalAmount();
        
        // Clear input fields
        txtQuantity.setText("");
        txtUnitPrice.setText("");
    }
    
    /**
     * Calculate the total amount from all details and update the display
     */
    private void calculateAndUpdateTotalAmount() {
        totalAmount = 0.0;
        for (ChiTietPhieuNhapThuoc detail : currentDetails) {
            totalAmount += detail.getSoLuong() * detail.getDonGia();
        }
        
        // Format and update total amount displays
        String formattedTotal = String.format("%,.0f VND", totalAmount);
        txtTongTien.setText(formattedTotal);
        txtDetailTongTien.setText(formattedTotal);
    }
    
    private void removeDetail() {
        int selectedRow = tblDetails.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết cần xóa");
            return;
        }
        
        // Remove from current details
        currentDetails.remove(selectedRow);
        
        // Update table
        updateDetailsTable();
        
        // Calculate and update total amount
        calculateAndUpdateTotalAmount();
    }
    
    private void newReceipt() {
        // Clear form
        clearForm();
        
        // Generate new ID
        try {
            String newId = phieuNhapThuocService.generateNewPhieuNhapId();
            txtReceiptId.setText(newId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo mã: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Set current date
        txtReceiptDate.setText(LocalDateTime.now().format(dateFormatter));
        
        // Create new receipt object
        currentReceipt = new PhieuNhapThuoc();
        currentReceipt.setId(txtReceiptId.getText().trim());
        
        // Clear details
        currentDetails = new ArrayList<>();
        updateDetailsTable();
        
        // Enable receipt ID field
        txtReceiptId.setEditable(true);
    }
    
    private void saveReceipt() {
        // Validate form
        if (txtReceiptId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phiếu nhập không được để trống");
            return;
        }
        
        if (txtReceiptDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ngày lập phiếu không được để trống");
            return;
        }
        
        if (cboSupplier.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp");
            return;
        }
        
        if (currentDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một chi tiết");
            return;
        }
        
        try {
            // Create or update receipt
            if (currentReceipt == null) {
                currentReceipt = new PhieuNhapThuoc();
            }
            
            currentReceipt.setId(txtReceiptId.getText().trim());
            
            // Parse date
            try {
                LocalDateTime date = LocalDateTime.parse(txtReceiptDate.getText().trim(), dateFormatter);
                currentReceipt.setThoiGian(date);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd HH:mm");
                return;
            }
            
            // Set supplier
            currentReceipt.setNhaCungCap((NhaCungCap) cboSupplier.getSelectedItem());
            
            // Set employee from logged in account
            if (loginAccount != null && loginAccount.getNhanVien() != null) {
                currentReceipt.setNhanVien(loginAccount.getNhanVien());
                System.out.println("Đang đặt nhân viên từ tài khoản đăng nhập: " + loginAccount.getNhanVien().getHoTen());
            } else {
                // Use the existing ADMIN employee record as fallback
                try {
                    NhanVien adminEmployee = nhanVienDAO.findById("ADMIN");
                    
                    if (adminEmployee != null) {
                        currentReceipt.setNhanVien(adminEmployee);
                        System.out.println("Đã đặt nhân viên là ADMIN: " + adminEmployee.getHoTen());
                    } else {
                        System.out.println("Cảnh báo: Không tìm thấy nhân viên ADMIN trong cơ sở dữ liệu");
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi khi đặt nhân viên mặc định: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Save receipt with details
            boolean saved = phieuNhapThuocService.addPhieuNhapWithDetails(currentReceipt, currentDetails);
            
            if (saved) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công");
                
                // Reload data
                loadReceiptData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thất bại");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void searchReceipts() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadReceiptData();
            return;
        }
        
        try {
            // Clear table
            receiptTableModel.setRowCount(0);
            
            // Get all receipts (no direct search method available)
            List<PhieuNhapThuoc> allReceipts = phieuNhapThuocService.findAll();
            
            // Filter based on keyword
            for (PhieuNhapThuoc receipt : allReceipts) {
                // Check if ID or supplier name contains keyword
                if (receipt.getId().toLowerCase().contains(keyword.toLowerCase()) ||
                    (receipt.getNhaCungCap() != null && 
                     receipt.getNhaCungCap().getTen().toLowerCase().contains(keyword.toLowerCase()))) {
                    
                    double totalAmount = 0;
                    try {
                        totalAmount = chiTietPhieuNhapThuocService.calculateTotalAmount(receipt.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    String formattedDate = receipt.getThoiGian() != null 
                            ? receipt.getThoiGian().format(dateFormatter) 
                            : "";
                    
                    Object[] row = {
                        receipt.getId(),
                        formattedDate,
                        receipt.getNhaCungCap() != null ? receipt.getNhaCungCap().getTen() : "",
                        String.format("%,.0f", totalAmount)
                    };
                    receiptTableModel.addRow(row);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phiếu nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        txtReceiptId.setText("");
        txtReceiptDate.setText("");
        cboSupplier.setSelectedIndex(cboSupplier.getItemCount() > 0 ? 0 : -1);
        txtQuantity.setText("");
        txtUnitPrice.setText("");
        txtSearch.setText("");
        
        // Clear total amounts
        txtTongTien.setText("");
        txtDetailTongTien.setText("");
        
        txtReceiptId.setEditable(true);
        
        currentReceipt = null;
        currentDetails = new ArrayList<>();
        
        detailTableModel.setRowCount(0);
        
        // Reload the receipt data to refresh the table
        loadReceiptData();
        
        tabPane.setSelectedIndex(0);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenerateId) {
            try {
                String newId = phieuNhapThuocService.generateNewPhieuNhapId();
                txtReceiptId.setText(newId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo mã: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnCurrentDate) {
            txtReceiptDate.setText(LocalDateTime.now().format(dateFormatter));
        } else if (e.getSource() == btnSearch) {
            searchReceipts();
        } else if (e.getSource() == btnNewReceipt) {
            newReceipt();
        } else if (e.getSource() == btnClear) {
            clearForm();
        } else if (e.getSource() == btnAddDetail) {
            addDetail();
        } else if (e.getSource() == btnRemoveDetail) {
            removeDetail();
        } else if (e.getSource() == btnThanhToan) {
            saveReceipt();
        }
    }
    
    // Custom renderer for combo boxes
    class MedicineRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Thuoc) {
                Thuoc medicine = (Thuoc) value;
                setText(medicine.getId() + " - " + medicine.getTen());
            }
            return this;
        }
    }
    
    class SupplierRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof NhaCungCap) {
                NhaCungCap supplier = (NhaCungCap) value;
                setText(supplier.getId() + " - " + supplier.getTen());
            }
            return this;
        }
    }
}
