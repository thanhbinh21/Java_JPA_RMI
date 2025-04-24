package gui;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.Thuoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A panel that displays the details of an invoice (HoaDon).
 * This panel shows the invoice header information and line items.
 */
public class gui_chiTietHoaDon extends JPanel {
    private HoaDon hoaDon;
    private JTable tblChiTiet;
    private DefaultTableModel tableModel;
    
    // Format for currency values
    private final DecimalFormat currencyFormat = new DecimalFormat("#,###.## VNĐ");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // UI Constants
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);  // Light blue background
    private static final Color TITLE_COLOR = new Color(0, 102, 204);        // Dark blue for titles
    private static final Color HEADER_BG = new Color(100, 149, 237);        // Blue for headers
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Arial", Font.BOLD, 14);
    
    /**
     * Constructs a new gui_chiTietHoaDon panel with the specified invoice.
     *
     * @param hoaDon the invoice to display details for
     */
    public gui_chiTietHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
        
        // Set up the panel
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND_COLOR);
        
        // Create the components
        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();
        JPanel footerPanel = createFooterPanel();
        
        // Add components to the panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates and returns the header panel containing the invoice title.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Title label
        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(TITLE_COLOR);
        
        // Add title to panel
        panel.add(lblTitle, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates and returns the content panel containing the invoice information and line items.
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Invoice information panel
        JPanel pnlInvoiceInfo = createInvoiceInfoPanel();
        
        // Line items panel
        JPanel pnlLineItems = createLineItemsPanel();
        
        // Add components to panel
        panel.add(pnlInvoiceInfo, BorderLayout.NORTH);
        panel.add(pnlLineItems, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates and returns the footer panel containing the totals and action buttons.
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Totals panel (right-aligned)
        JPanel pnlTotals = createTotalsPanel();
        panel.add(pnlTotals, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates and returns the invoice information panel.
     */
    private JPanel createInvoiceInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Thông tin hóa đơn",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        HEADER_FONT,
                        TITLE_COLOR),
                new EmptyBorder(10, 10, 10, 10)));
        
        // GridBag constraints
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.anchor = GridBagConstraints.WEST;
        labelGbc.insets = new Insets(5, 5, 5, 15);
        
        GridBagConstraints valueGbc = new GridBagConstraints();
        valueGbc.gridx = 1;
        valueGbc.gridy = 0;
        valueGbc.weightx = 1.0;
        valueGbc.fill = GridBagConstraints.HORIZONTAL;
        valueGbc.insets = new Insets(5, 5, 5, 15);
        
        GridBagConstraints labelGbc2 = new GridBagConstraints();
        labelGbc2.gridx = 2;
        labelGbc2.gridy = 0;
        labelGbc2.anchor = GridBagConstraints.WEST;
        labelGbc2.insets = new Insets(5, 15, 5, 15);
        
        GridBagConstraints valueGbc2 = new GridBagConstraints();
        valueGbc2.gridx = 3;
        valueGbc2.gridy = 0;
        valueGbc2.weightx = 1.0;
        valueGbc2.fill = GridBagConstraints.HORIZONTAL;
        valueGbc2.insets = new Insets(5, 5, 5, 5);
        
        // Invoice ID
        addLabelAndValue(panel, "Mã hóa đơn:", hoaDon.getId(), labelGbc, valueGbc);
        labelGbc.gridy++;
        valueGbc.gridy++;
        
        // Invoice date
        String dateStr = "N/A";
        if (hoaDon.getThoiGian() != null) {
            dateStr = dateTimeFormat.format(hoaDon.getThoiGian());
        }
        addLabelAndValue(panel, "Thời gian lập:", dateStr, labelGbc, valueGbc);
        labelGbc.gridy++;
        valueGbc.gridy++;
        
        // Employee info
        String employeeName = "N/A";
        if (hoaDon.getNhanVien() != null) {
            employeeName = hoaDon.getNhanVien().getHoTen();
        }
        addLabelAndValue(panel, "Nhân viên:", employeeName, labelGbc, valueGbc);
        
        // Customer info (right column)
        KhachHang khachHang = hoaDon.getKhachHang();
        String customerName = khachHang != null ? khachHang.getHoTen() : "Khách lẻ";
        String customerPhone = khachHang != null ? khachHang.getSoDienThoai() : "N/A";
        
        addLabelAndValue(panel, "Khách hàng:", customerName, labelGbc2, valueGbc2);
        labelGbc2.gridy++;
        valueGbc2.gridy++;
        
        addLabelAndValue(panel, "Điện thoại:", customerPhone, labelGbc2, valueGbc2);
        labelGbc2.gridy++;
        valueGbc2.gridy++;
        
        // Payment status
        String paymentStatus = hoaDon.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán";
        addLabelAndValue(panel, "Trạng thái:", paymentStatus, labelGbc2, valueGbc2);
        
        return panel;
    }
    
    /**
     * Helper method to add a label and value to a panel using the specified GridBagConstraints.
     */
    private void addLabelAndValue(JPanel panel, String labelText, String valueText, 
                                 GridBagConstraints labelGbc, GridBagConstraints valueGbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        
        JLabel value = new JLabel(valueText);
        value.setFont(VALUE_FONT);
        
        panel.add(label, labelGbc);
        panel.add(value, valueGbc);
    }
    
    /**
     * Creates and returns the line items panel containing the table of products.
     */
    private JPanel createLineItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Danh sách sản phẩm",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        HEADER_FONT,
                        TITLE_COLOR),
                new EmptyBorder(10, 10, 10, 10)));
        
        // Create table model with columns
        String[] columns = {
            "STT", "Mã thuốc", "Tên thuốc", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make the table non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;  // STT
                if (columnIndex == 4) return Integer.class;  // Quantity
                if (columnIndex == 5 || columnIndex == 6) return Double.class;  // Prices
                return String.class;
            }
        };
        
        // Create and configure the table
        tblChiTiet = new JTable(tableModel);
        tblChiTiet.setFont(VALUE_FONT);
        tblChiTiet.setRowHeight(25);
        tblChiTiet.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        tblChiTiet.setAutoCreateRowSorter(true);
        
        // Set column widths
        tblChiTiet.getColumnModel().getColumn(0).setMaxWidth(50);      // STT
        tblChiTiet.getColumnModel().getColumn(1).setPreferredWidth(80);  // Product ID
        tblChiTiet.getColumnModel().getColumn(2).setPreferredWidth(200); // Product Name
        tblChiTiet.getColumnModel().getColumn(3).setPreferredWidth(80);  // Unit
        tblChiTiet.getColumnModel().getColumn(4).setPreferredWidth(80);  // Quantity
        tblChiTiet.getColumnModel().getColumn(5).setPreferredWidth(100); // Price
        tblChiTiet.getColumnModel().getColumn(6).setPreferredWidth(120); // Total
        
        // Align numbers to the right
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblChiTiet.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tblChiTiet.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        tblChiTiet.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        // Load the table data
        loadTableData();
        
        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Add the scroll pane to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates and returns the totals panel containing the invoice totals.
     */
    private JPanel createTotalsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                new EmptyBorder(10, 10, 10, 10)));
        
        // GridBag constraints
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridx = 0;
        labelGbc.gridy = 0;
        labelGbc.anchor = GridBagConstraints.EAST;
        labelGbc.insets = new Insets(5, 5, 5, 15);
        
        GridBagConstraints valueGbc = new GridBagConstraints();
        valueGbc.gridx = 1;
        valueGbc.gridy = 0;
        valueGbc.anchor = GridBagConstraints.EAST;
        valueGbc.insets = new Insets(5, 5, 5, 5);
        
        // Calculate total quantity and amount
        int totalQuantity = 0;
        double totalAmount = 0;
        
        if (hoaDon.getChiTietHoaDons() != null) {
            for (ChiTietHoaDon chiTiet : hoaDon.getChiTietHoaDons()) {
                totalQuantity += chiTiet.getSoLuong();
                totalAmount += chiTiet.getSoLuong() * chiTiet.getDonGia();
            }
        }
        
        // Total quantity
        JLabel lblTotalQuantity = new JLabel("Tổng số lượng:");
        lblTotalQuantity.setFont(LABEL_FONT);
        JLabel valueTotalQuantity = new JLabel(String.valueOf(totalQuantity));
        valueTotalQuantity.setFont(VALUE_FONT);
        
        panel.add(lblTotalQuantity, labelGbc);
        panel.add(valueTotalQuantity, valueGbc);
        
        labelGbc.gridy++;
        valueGbc.gridy++;
        
        // Subtotal
        JLabel lblSubtotal = new JLabel("Tổng tiền hàng:");
        lblSubtotal.setFont(LABEL_FONT);
        JLabel valueSubtotal = new JLabel(currencyFormat.format(totalAmount));
        valueSubtotal.setFont(VALUE_FONT);
        
        panel.add(lblSubtotal, labelGbc);
        panel.add(valueSubtotal, valueGbc);
        
        labelGbc.gridy++;
        valueGbc.gridy++;
        
        // Line separator
        JSeparator separator = new JSeparator();
        GridBagConstraints sepGbc = new GridBagConstraints();
        sepGbc.gridx = 0;
        sepGbc.gridy = labelGbc.gridy;
        sepGbc.gridwidth = 2;
        sepGbc.fill = GridBagConstraints.HORIZONTAL;
        sepGbc.insets = new Insets(5, 0, 5, 0);
        
        panel.add(separator, sepGbc);
        
        labelGbc.gridy++;
        valueGbc.gridy++;
        
        // Total amount
        JLabel lblTotal = new JLabel("Tổng thanh toán:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel valueTotal = new JLabel(currencyFormat.format(totalAmount));
        valueTotal.setFont(new Font("Arial", Font.BOLD, 16));
        valueTotal.setForeground(new Color(220, 20, 60));  // Crimson color for emphasis
        
        panel.add(lblTotal, labelGbc);
        panel.add(valueTotal, valueGbc);
        
        return panel;
    }
    
    /**
     * Loads the line items data into the table.
     */
    private void loadTableData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get line items
        Set<ChiTietHoaDon> chiTietSet = hoaDon.getChiTietHoaDons();
        if (chiTietSet == null || chiTietSet.isEmpty()) {
            return;
        }
        
        // Convert to list and sort by product name
        List<ChiTietHoaDon> sortedItems = chiTietSet.stream()
                .sorted(Comparator.comparing(item -> {
                    Thuoc thuoc = item.getThuoc();
                    return thuoc != null ? thuoc.getTen() : "";
                }))
                .collect(Collectors.toList());
        
        // Add data to table model
        int stt = 1;
        for (ChiTietHoaDon chiTiet : sortedItems) {
            Thuoc thuoc = chiTiet.getThuoc();
            
            if (thuoc == null) {
                continue;
            }
            
            int quantity = chiTiet.getSoLuong();
            double unitPrice = chiTiet.getDonGia();
            double totalPrice = quantity * unitPrice;
            
            Object[] row = {
                stt++,
                thuoc.getId(),
                thuoc.getTen(),
                thuoc.getDonViTinh(),
                quantity,
                unitPrice,
                totalPrice
            };
            
            tableModel.addRow(row);
        }
    }
}