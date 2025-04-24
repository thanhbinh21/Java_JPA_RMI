package gui;


import dao.impl.*;
import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import other.MessageDialog;
import service.*;
import service.impl.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
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
import java.util.ArrayList;
import java.util.List;

public class gui_timKiemPhieuDatThuoc extends JPanel implements ActionListener {
    private JPanel pnlMain;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;


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


    private JTable tblReceipts;
    private DefaultTableModel receiptTableModel;
    private JScrollPane scrollReceipts;


    private JPanel pnlDetails;
    private JTable tblDetails;
    private DefaultTableModel detailTableModel;
    private JScrollPane scrollDetails;


    private ThuocService THUOC_SEVICE;
    private KhachHangService KHACH_HANG_SEVICE;
    private DatThuocSevice DAT_THUOC_SEVICE;
    private PhieuDatThuocService PHEU_DAT_THUOC_SEVICE;
    private NhanVienService NHAN_VIEN_SECICE;


    public gui_timKiemPhieuDatThuoc() {

        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            THUOC_SEVICE = (ThuocService) registry.lookup("ThuocService");
            KHACH_HANG_SEVICE = (KhachHangService) registry.lookup("KhachHangService");
            DAT_THUOC_SEVICE = (DatThuocSevice) registry.lookup("DatThuocService");
            PHEU_DAT_THUOC_SEVICE = (PhieuDatThuocService) registry.lookup("PhieuDatThuocService");

        } catch (Exception e) {
            MessageDialog.error(this, "Khởi tạo dịch vụ thất bại: " + e.getMessage());
            e.printStackTrace();
        }

        createUI();

        loadSampleData();
    }

    private void createUI() {

        setLayout(new BorderLayout());


        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(pnlMain, BorderLayout.CENTER);

        createNorthPanel();
        createCenterPanel();

        createSouthPanel();
    }

    private void createNorthPanel() {
        pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel pnlTitle = new JPanel();
        lblTitle = new JLabel("TÌM KIẾM PHIẾU ĐẶT THUỐC");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTitle.add(lblTitle);
        pnlNorth.add(pnlTitle, BorderLayout.NORTH);

        // Search criteria panel
        JPanel pnlCriteria = new JPanel(new GridBagLayout());
        pnlCriteria.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Điều kiện tìm kiếm",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Receipt ID
        lblReceiptId = new JLabel("Mã phiếu đặt:");
        lblReceiptId.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlCriteria.add(lblReceiptId, gbc);

        txtReceiptId = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlCriteria.add(txtReceiptId, gbc);


//
        // Button panel
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setIcon(new ImageIcon(getClass().getResource("/icon/search.png")));
        btnSearch.addActionListener(this);

        btnClear = new JButton("Làm mới");
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
                BorderFactory.createEtchedBorder(), "Kết quả tìm kiếm",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        // Receipts table
        String[] receiptColumns = {"Mã phiếu", "Ngày lập", "Trạng thái", "Khách hàng", "Nhân vien"};
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
                BorderFactory.createEtchedBorder(), "Chi tiết phiếu đặt",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        // Details table
        String[] detailColumns = {"Thuốc", "Số Lượng", "Đơn Giá"};
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
//        tblDetails.getColumnModel().getColumn(3).setPreferredWidth(120);  // Total

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



            System.out.println("Tìm kiếm với điều kiện: Mã=" + receiptId );


            List<PhieuDatThuoc> receipts = new ArrayList<>();

            if (receiptId.isEmpty()) {
                System.out.println("Không có điều kiện, lấy tất cả phiếu đặt");

                receipts = PHEU_DAT_THUOC_SEVICE.findAll();
            } else {
                PhieuDatThuoc pdt = PHEU_DAT_THUOC_SEVICE.findById(receiptId);

                receipts.add(pdt);
            }

            System.out.println("Tìm thấy " + receipts.size() + " phiếu đặt thuốc");

            if (receipts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt thuốc phù hợp với điều kiện tìm kiếm");
                return;
            }

            // Add receipts to table
            for (PhieuDatThuoc receipt : receipts) {



                String formattedDate = receipt.getThoiGian() != null
                        ? receipt.getThoiGian().toString()
                        : "";
//                "Mã phiếu", "Ngày lập", "Trạng thái", "Khách hàng", "Nhân vien"
                Object[] row = {
                        receipt.getId(),
                        formattedDate,
                        receipt.isTrangThai() ? "Đã lấy":"Chưa lấy",
                        receipt.getKhachHang() != null ? receipt.getKhachHang().getHoTen() : "",
                        receipt.getNhanVien() != null ? receipt.getNhanVien().getHoTen() : ""
                };
                receiptTableModel.addRow(row);
                System.out.println("Đã thêm phiếu đặt thuốc: " + receipt.getId());
            }

            if (tblReceipts.getRowCount() > 0) {
                tblReceipts.setRowSelectionInterval(0, 0);
                String receiptIdToLoad = tblReceipts.getValueAt(0, 0).toString();
                System.out.println("Đang tải chi tiết cho phiếu đặt thuốc: " + receiptIdToLoad);
                loadReceiptDetails(receiptIdToLoad);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phiếu đặt thuốc: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadReceiptDetails(String receiptId) {
        try {
            System.out.println("Đang tải chi tiết cho phiếu đặt ID: " + receiptId);

            // Clear existing details
            detailTableModel.setRowCount(0);

            // Get receipt details
            List<ChiTietPhieuDatThuoc> details = PHEU_DAT_THUOC_SEVICE.findChiTietByPhieuDatThuoc(PHEU_DAT_THUOC_SEVICE.findById(receiptId));
            System.out.println("Tìm thấy " + details.size() + " chi tiết phiếu đặt thuốc");

            if (details.isEmpty()) {
                System.out.println("Không tìm thấy chi tiết cho phiếu đặt ID: " + receiptId);
                return;
            }

            // Add details to table
            double totalAmount = 0;
            for (ChiTietPhieuDatThuoc detail : details) {
                double total = detail.getSoLuong() * detail.getDonGia();
                totalAmount += total;

                String medicineName = detail.getThuoc() != null ? detail.getThuoc().getTen() : "Không xác định";
                System.out.println("Thêm chi tiết: " + medicineName + ", Số lượng: " + detail.getSoLuong());

                Object[] row = {
//                        {"Thuốc", "Số Lượng", "Đơn Giá"};
                        medicineName,
                        detail.getSoLuong(),
                        String.format("%,.0f", detail.getDonGia())
                };
                detailTableModel.addRow(row);
            }

            System.out.println("Tổng tiền phiếu đặt thuốc: " + totalAmount);

        } catch (Exception e) {
            System.err.println("Lỗi khi tải chi tiết phiếu đặt: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết phiếu đặt: " + e.getMessage());
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

        try {
            List<PhieuDatThuoc> allReceipts = PHEU_DAT_THUOC_SEVICE.findAll();

            if (allReceipts.size() > 0) {

            } else {

                loadSampleData();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải lại dữ liệu sau khi làm mới: " + e.getMessage());
            e.printStackTrace();

            loadSampleData();
        }
    }

    private void loadSampleData() {
        try {

            receiptTableModel.setRowCount(0);

            List<PhieuDatThuoc> allReceipts = PHEU_DAT_THUOC_SEVICE.findAll();
            if (allReceipts.size() > 0) {
                System.out.println("Found " + allReceipts.size() + " existing receipts in database");
                for (PhieuDatThuoc receipt : allReceipts) {
                    System.out.println("Receipt ID: " + receipt.getId() +
                            ", Supplier: " + (receipt.getKhachHang() != null ? receipt.getKhachHang().getHoTen() : "N/A"));
                }
                searchReceipts();
                return;
            }

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