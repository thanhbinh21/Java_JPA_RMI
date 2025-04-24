package gui;

import entity.KhachHang;
import service.KhachHangService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class gui_timKiemKhachHang extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboFilterType;
    private JTable tblResults;
    private DefaultTableModel tableModel;
    private JPanel pnlFilter;
    private JComboBox<String> cboGenderFilter;
    
    // Services
    private KhachHangService khachHangService;

    // UI Constants
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Light blue
    private static final Color TITLE_COLOR = new Color(0, 102, 204); // Dark blue
    private static final Color BUTTON_BG = new Color(100, 149, 237); // Cornflower blue
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    
    public gui_timKiemKhachHang() {
        // Initialize service
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            khachHangService = (KhachHangService) registry.lookup("KhachHangService");
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
        JLabel lblTitle = new JLabel("TÌM KIẾM KHÁCH HÀNG");
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
            "Tất cả", "Mã khách hàng", "Tên khách hàng", "Số điện thoại"
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
        
        JLabel lblGenderFilter = new JLabel("Giới tính:");
        lblGenderFilter.setFont(LABEL_FONT);
        
        cboGenderFilter = new JComboBox<>(new String[]{
            "Tất cả", "Nam", "Nữ"
        });
        cboGenderFilter.setFont(LABEL_FONT);
        cboGenderFilter.setPreferredSize(new Dimension(100, 30));
        cboGenderFilter.addActionListener(e -> loadData());
        
        panel.add(lblGenderFilter);
        panel.add(cboGenderFilter);
        
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Table columns
        String[] columns = {
            "STT", "Mã khách hàng", "Họ tên", "Số điện thoại", "Giới tính", "Ngày tham gia"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblResults = new JTable(tableModel);
        tblResults.setFont(new Font("Arial", Font.PLAIN, 14));
        tblResults.setRowHeight(30);
        tblResults.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblResults.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        tblResults.getColumnModel().getColumn(0).setMaxWidth(60);
        tblResults.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblResults.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblResults.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblResults.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblResults.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        // Add double click listener for details
        tblResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showCustomerDetails();
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
        btnViewDetails.addActionListener(e -> showCustomerDetails());
        
        JButton btnAdd = new JButton("Thêm khách hàng");
        styleButton(btnAdd);
        btnAdd.addActionListener(e -> addNewCustomer());
        
        JButton btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cboFilterType.setSelectedIndex(0);
            cboGenderFilter.setSelectedIndex(0);
            loadData();
        });
        
        panel.add(btnViewDetails);
        panel.add(btnAdd);
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
            List<KhachHang> customers = getFilteredCustomers();
            
            if (customers == null) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khách hàng từ hệ thống");
                return;
            }
            
            if (customers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp với điều kiện");
                return;
            }
            
            int stt = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (KhachHang khachHang : customers) {
                String gender = khachHang.isGioiTinh() ? "Nam" : "Nữ";
                String joinDate = khachHang.getNgayThamGia().format(formatter);
                
                tableModel.addRow(new Object[] {
                    stt++,
                    khachHang.getId(),
                    khachHang.getHoTen(),
                    khachHang.getSoDienThoai(),
                    gender,
                    joinDate
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    private List<KhachHang> getFilteredCustomers() {
        try {
            String genderFilterValue = (String) cboGenderFilter.getSelectedItem();
            
            if (genderFilterValue == null || genderFilterValue.equals("Tất cả")) {
                return khachHangService.findAll();
            } else {
                boolean genderFilter = genderFilterValue.equals("Nam");
                return khachHangService.findByGender(genderFilter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void performSearch() {
        try {
            String searchType = (String) cboFilterType.getSelectedItem();
            String searchValue = txtSearch.getText().trim();
            
            if (searchType == null || searchValue.isEmpty()) {
                loadData();
                return;
            }
            
            tableModel.setRowCount(0);
            List<KhachHang> results = null;
            
            switch (searchType) {
                case "Tất cả":
                    results = khachHangService.findAll();
                    break;
                case "Mã khách hàng":
                    KhachHang khachHang = khachHangService.findById(searchValue);
                    if (khachHang != null) {
                        results = List.of(khachHang);
                    } else {
                        results = List.of(); // Empty list if not found
                    }
                    break;
                case "Tên khách hàng":
                    results = khachHangService.findByName(searchValue);
                    break;
                case "Số điện thoại":
                    results = khachHangService.findByPhone(searchValue);
                    break;
            }
            
            if (results == null) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khách hàng từ hệ thống");
                return;
            }
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp với điều kiện tìm kiếm");
                return;
            }
            
            // Apply gender filter to results if needed
            String genderFilter = (String) cboGenderFilter.getSelectedItem();
            if (genderFilter != null && !genderFilter.equals("Tất cả")) {
                boolean genderValue = genderFilter.equals("Nam");
                results = results.stream()
                    .filter(kh -> kh.isGioiTinh() == genderValue)
                    .toList();
                
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp với điều kiện tìm kiếm và giới tính");
                    return;
                }
            }
            
            int stt = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (KhachHang khachHang : results) {
                String gender = khachHang.isGioiTinh() ? "Nam" : "Nữ";
                String joinDate = khachHang.getNgayThamGia().format(formatter);
                
                tableModel.addRow(new Object[] {
                    stt++,
                    khachHang.getId(),
                    khachHang.getHoTen(),
                    khachHang.getSoDienThoai(),
                    gender,
                    joinDate
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }

    private void showCustomerDetails() {
        int row = tblResults.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xem chi tiết");
            return;
        }
        
        String customerId = tableModel.getValueAt(row, 1).toString();
        
        try {
            KhachHang khachHang = khachHangService.findById(customerId);
            if (khachHang != null) {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết khách hàng", true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);
                
                // You would replace this with your actual detail view
                JPanel detailPanel = new JPanel(new BorderLayout());
                detailPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                
                // Create a form panel for customer details
                JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                
                // Add customer details fields
                addDetailField(formPanel, "Mã khách hàng:", khachHang.getId());
                addDetailField(formPanel, "Họ tên:", khachHang.getHoTen());
                addDetailField(formPanel, "Số điện thoại:", khachHang.getSoDienThoai());
                addDetailField(formPanel, "Giới tính:", khachHang.isGioiTinh() ? "Nam" : "Nữ");
                addDetailField(formPanel, "Ngày tham gia:", khachHang.getNgayThamGia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                
                detailPanel.add(formPanel, BorderLayout.CENTER);
                
                // Add buttons panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                
                JButton btnEdit = new JButton("Sửa thông tin");
                styleButton(btnEdit);
                btnEdit.setPreferredSize(new Dimension(130, 35));
                
                JButton btnClose = new JButton("Đóng");
                styleButton(btnClose);
                btnClose.setPreferredSize(new Dimension(100, 35));
                btnClose.addActionListener(e -> dialog.dispose());
                
                //buttonPanel.add(btnEdit);
                buttonPanel.add(btnClose);
                
                detailPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                dialog.add(detailPanel);
                dialog.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị chi tiết khách hàng: " + ex.getMessage());
        }
    }
    
    private void addDetailField(JPanel panel, String label, String value) {
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JTextField txtValue = new JTextField(value);
        txtValue.setFont(new Font("Arial", Font.PLAIN, 14));
        txtValue.setEditable(false);
        txtValue.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        panel.add(lblTitle);
        panel.add(txtValue);
    }

    private void addNewCustomer() {
        // You would implement this to open a dialog for adding a new customer
        // This is just a placeholder
        JOptionPane.showMessageDialog(this, "Chức năng thêm khách hàng đang được phát triển");
    }
}
