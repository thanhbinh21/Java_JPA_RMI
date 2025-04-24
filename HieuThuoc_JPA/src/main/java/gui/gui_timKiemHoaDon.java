package gui;

import entity.HoaDon;
import other.Formatter;
import service.HoaDonService;
import service.impl.HoaDonServiceImpl;
import dao.impl.HoaDonDAOImpl;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class gui_timKiemHoaDon extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboFilterType;
    private JTable tblResults;
    private DefaultTableModel tableModel;
    private JPanel pnlFilter;
    private JComboBox<String> cboDateFilter;
    
    // Date filtering
    private JPanel dateRangePanel;
    private JLabel lblFromDate, lblToDate;
    private JSpinner dateFrom, dateTo;
    
    // Services
    private HoaDonService hoaDonService;

    // UI Constants
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Light blue
    private static final Color TITLE_COLOR = new Color(0, 102, 204); // Dark blue
    private static final Color BUTTON_BG = new Color(100, 149, 237); // Cornflower blue
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    // Date format
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat moneyFormat = new DecimalFormat("#,###");
    
    public gui_timKiemHoaDon() {
        // Initialize service
        try {
//            hoaDonService = new HoaDonServiceImpl(new HoaDonDAOImpl());
            Registry registry = LocateRegistry.getRegistry(8989);
            hoaDonService = (HoaDonService) registry.lookup("HoaDonService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo dịch vụ: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Set up UI
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND_COLOR);
        
        // Title Panel
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("TÌM KIẾM HÓA ĐƠN");
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(TITLE_COLOR);
        pnlTitle.add(lblTitle);
        
        // Search Panel
        JPanel pnlSearch = createSearchPanel();
        
        // Filter Panel
        pnlFilter = createFilterPanel();
        
        // Results Panel
        JPanel pnlResults = createResultsPanel();
        
        // Button Panel
        JPanel pnlButtons = createButtonPanel();
        
        // Add components to main panel
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(BACKGROUND_COLOR);
        pnlTop.add(pnlTitle, BorderLayout.NORTH);
        pnlTop.add(pnlSearch, BorderLayout.CENTER);
        pnlTop.add(pnlFilter, BorderLayout.SOUTH);
        
        add(pnlTop, BorderLayout.NORTH);
        add(pnlResults, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Load initial data
        loadData();
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel lblSearchBy = new JLabel("Tìm kiếm theo:");
        lblSearchBy.setFont(LABEL_FONT);
        
        cboFilterType = new JComboBox<>(new String[]{
            "Tất cả", "Mã hóa đơn", "Tên khách hàng", "Nhân viên bán", "Ngày lập"
        });
        cboFilterType.setFont(LABEL_FONT);
        cboFilterType.setPreferredSize(new Dimension(150, 30));
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(LABEL_FONT);
        txtSearch.setPreferredSize(new Dimension(250, 30));
        
        JButton btnSearch = new JButton("Tìm kiếm");
       btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(120, 30));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> performSearch());
        
        panel.add(lblSearchBy);
        panel.add(cboFilterType);
        panel.add(txtSearch);
        panel.add(btnSearch);
        
        // Add listener to show/hide date filter based on selection
        cboFilterType.addActionListener(e -> {
            String selected = (String) cboFilterType.getSelectedItem();
            if (selected != null && selected.equals("Ngày lập")) {
                txtSearch.setVisible(false);
                dateRangePanel.setVisible(true);
            } else {
                txtSearch.setVisible(true);
                dateRangePanel.setVisible(false);
            }
            panel.revalidate();
            panel.repaint();
        });
        
        // Date range panel
        dateRangePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dateRangePanel.setBackground(BACKGROUND_COLOR);
        dateRangePanel.setVisible(false);
        
        lblFromDate = new JLabel("Từ ngày:");
        lblFromDate.setFont(LABEL_FONT);
        
        lblToDate = new JLabel("Đến ngày:");
        lblToDate.setFont(LABEL_FONT);
        
        // Initialize with date spinners
        Date today = new Date();
        Date lastMonth = new Date(today.getTime() - 30L * 24 * 60 * 60 * 1000);
        
        SpinnerDateModel dateModelFrom = new SpinnerDateModel(lastMonth, null, today, Calendar.DAY_OF_MONTH);
        dateFrom = new JSpinner(dateModelFrom);
        JSpinner.DateEditor editorFrom = new JSpinner.DateEditor(dateFrom, "dd/MM/yyyy");
        dateFrom.setEditor(editorFrom);
        dateFrom.setPreferredSize(new Dimension(120, 30));
        
        SpinnerDateModel dateModelTo = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        dateTo = new JSpinner(dateModelTo);
        JSpinner.DateEditor editorTo = new JSpinner.DateEditor(dateTo, "dd/MM/yyyy");
        dateTo.setEditor(editorTo);
        dateTo.setPreferredSize(new Dimension(120, 30));
        
        dateRangePanel.add(lblFromDate);
        dateRangePanel.add(dateFrom);
        dateRangePanel.add(Box.createHorizontalStrut(20));
        dateRangePanel.add(lblToDate);
        dateRangePanel.add(dateTo);
        
        panel.add(dateRangePanel);
        
        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Bộ lọc bổ sung",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            LABEL_FONT,
            TITLE_COLOR
        ));
        
        JLabel lblDateFilter = new JLabel("Thời gian:");
        lblDateFilter.setFont(LABEL_FONT);
        
        cboDateFilter = new JComboBox<>(new String[]{
            "Tất cả", "Hôm nay", "Tuần này", "Tháng này", "Năm nay"
        });
        cboDateFilter.setFont(LABEL_FONT);
        cboDateFilter.setPreferredSize(new Dimension(150, 30));
        cboDateFilter.addActionListener(e -> loadData());
        
        panel.add(lblDateFilter);
        panel.add(cboDateFilter);
        
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Table columns
        String[] columns = {
            "STT", "Mã hóa đơn", "Khách hàng", "Nhân viên", "Ngày lập", "Tổng tiền", "Trạng thái"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 5) return Double.class;
                return String.class;
            }
        };
        
        tblResults = new JTable(tableModel);
        tblResults.setFont(new Font("Arial", Font.PLAIN, 14));
        tblResults.setRowHeight(30);
        tblResults.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblResults.getTableHeader().setReorderingAllowed(false);

        
        // Set column widths
        tblResults.getColumnModel().getColumn(0).setMaxWidth(60);
        tblResults.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblResults.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblResults.getColumnModel().getColumn(3).setPreferredWidth(180);
        tblResults.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblResults.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblResults.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Add double click listener for details
        tblResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showInvoiceDetails();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblResults);
        scrollPane.setBorder(BorderFactory.createLineBorder(TITLE_COLOR));
        
        panel.add(new JLabel("Kết quả tìm kiếm:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnViewDetails = new JButton("Xem chi tiết");
        styleButton(btnViewDetails);
        btnViewDetails.addActionListener(e -> showInvoiceDetails());
        
        JButton btnExport = new JButton("Xuất báo cáo");
        styleButton(btnExport);
        btnExport.addActionListener(e -> exportReport());
        
        JButton btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cboFilterType.setSelectedIndex(0);
            cboDateFilter.setSelectedIndex(0);
            loadData();
        });
        
        panel.add(btnViewDetails);
        panel.add(btnExport);
        panel.add(btnRefresh);
        
        return panel;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            
            // Get filtered data based on the date filter
            List<HoaDon> invoices = getFilteredInvoices();
            
            if (invoices == null) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu hóa đơn từ hệ thống");
                return;
            }
            
            if (invoices.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào phù hợp với điều kiện");
                return;
            }
            
            int stt = 1;
            for (HoaDon hoaDon : invoices) {
                String status = hoaDon.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán";
                
                // Get total from HoaDon object directly if available, or calculate safely
                double tongTien = 0;
                try {
                    // Try to get the total from a service method to avoid LazyInitializationException
                    tongTien = hoaDonService.calculateTotalAmount(hoaDon.getId());
                } catch (Exception e) {
                    // If this fails, we'll show 0 as the amount
                    System.err.println("Không thể tính tổng tiền cho hóa đơn " + hoaDon.getId() + ": " + e.getMessage());
                }
                
                tableModel.addRow(new Object[] {
                    stt++,
                    hoaDon.getId(),
                    hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getHoTen() : "Khách lẻ",
                    hoaDon.getNhanVien().getHoTen(),
                    Formatter.FormatDate(hoaDon.getThoiGian()),
                    tongTien,
                    status
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    private List<HoaDon> getFilteredInvoices() {
        try {
            String dateFilterValue = (String) cboDateFilter.getSelectedItem();
            
            if (dateFilterValue == null || dateFilterValue.equals("Tất cả")) {
                return hoaDonService.findAll();
            }
            
            LocalDate startDate = null;
            LocalDate endDate = LocalDate.now();
            
            switch (dateFilterValue) {
                case "Hôm nay":
                    startDate = LocalDate.now();
                    break;
                case "Tuần này":
                    startDate = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
                    break;
                case "Tháng này":
                    startDate = LocalDate.now().withDayOfMonth(1);
                    break;
                case "Năm nay":
                    startDate = LocalDate.now().withDayOfYear(1);
                    break;
                default:
                    return hoaDonService.findAll();
            }
            
            if (startDate != null) {
                return hoaDonService.findByDateRange(startDate, endDate);
            }
            
            return hoaDonService.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi lọc hóa đơn: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            // Return an empty list instead of null to avoid NullPointerException
            return List.of();
        }
    }

    private void performSearch() {
        try {
            String searchType = (String) cboFilterType.getSelectedItem();
            
            if (searchType == null) {
                loadData();
                return;
            }
            
            tableModel.setRowCount(0);
            List<HoaDon> results = null;
            
            switch (searchType) {
                case "Tất cả":
                    results = hoaDonService.findAll();
                    break;
                case "Mã hóa đơn":
                    String maHD = txtSearch.getText().trim();
                    HoaDon hoaDon = hoaDonService.findById(maHD);
                    if (hoaDon != null) {
                        results = List.of(hoaDon);
                    }
                    break;
                case "Tên khách hàng":
                    String khachHangName = txtSearch.getText().trim();
                    results = hoaDonService.findByKhachHangName(khachHangName);
                    break;
                case "Nhân viên bán":
                    String nhanVienName = txtSearch.getText().trim();
                    results = hoaDonService.findByNhanVienName(nhanVienName);
                    break;
                case "Ngày lập":
                    Date fromDate = (Date) dateFrom.getValue();
                    Date toDate = (Date) dateTo.getValue();
                    LocalDate startDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    results = hoaDonService.findByDateRange(startDate, endDate);
                    break;
            }
            
            if (results == null || results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào phù hợp");
                return;
            }
            
            int stt = 1;
            for (HoaDon hoaDon : results) {
                String status = hoaDon.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán";
                
                // Calculate total amount from line items
                double tongTien = 0;
                try {
                    // Use the service method which we've fixed to handle null collections safely
                    tongTien = hoaDonService.calculateTotalAmount(hoaDon.getId());
                } catch (Exception e) {
                    System.err.println("Error calculating total for invoice " + hoaDon.getId() + ": " + e.getMessage());
                    // Fallback to direct calculation only if service call fails
                    tongTien = hoaDon.getChiTietHoaDons().stream()
                        .mapToDouble(cthd -> cthd.getSoLuong() * cthd.getDonGia())
                        .sum();
                }
                
                tableModel.addRow(new Object[] {
                    stt++,
                    hoaDon.getId(),
                    hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getHoTen() : "Khách lẻ",
                    hoaDon.getNhanVien().getHoTen(),
                    Formatter.FormatDate(hoaDon.getThoiGian()),
                    tongTien,
                    status
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }

    private void showInvoiceDetails() {
        int row = tblResults.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xem chi tiết");
            return;
        }
        
        String maHD = tableModel.getValueAt(row, 1).toString();
        
        try {
            HoaDon hoaDon = hoaDonService.findById(maHD);
            if (hoaDon != null) {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(this);
                
                // Create detail panel - you would replace this with your actual detail view
                JPanel detailPanel = new gui_chiTietHoaDon(hoaDon);
                dialog.add(detailPanel);
                
                dialog.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị chi tiết hóa đơn: " + ex.getMessage());
        }
    }

    private void exportReport() {
        // Implement report export functionality
        JOptionPane.showMessageDialog(this, "Tính năng xuất báo cáo đang được phát triển");
    }
}
