package gui;

import entity.DanhMuc;
import entity.NhaSanXuat;
import entity.Thuoc;
import service.DanhMucService;
import service.NhaSanXuatService;
import service.ThuocService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class gui_timKiemThuoc extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboFilterType;
    private JTable tblResults;
    private DefaultTableModel tableModel;
    private JPanel pnlFilter;
    private JComboBox<DanhMuc> cboDanhMuc;
    private JComboBox<NhaSanXuat> cboNhaSanXuat;

    // Services
    private ThuocService thuocService;
    private DanhMucService danhMucService;
    private NhaSanXuatService nhaSanXuatService;

    // Formatters
    private final DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // UI Constants
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Light blue
    private static final Color TITLE_COLOR = new Color(0, 102, 204); // Dark blue
    private static final Color BUTTON_BG = new Color(100, 149, 237); // Cornflower blue
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public gui_timKiemThuoc() {
        // Initialize services
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            thuocService = (ThuocService) registry.lookup("ThuocService");
            danhMucService = (DanhMucService) registry.lookup("DanhMucService");
            nhaSanXuatService = (NhaSanXuatService) registry.lookup("NhaSanXuatService");
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
        JLabel lblTitle = new JLabel("TÌM KIẾM THUỐC");
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
                "Tất cả", "Mã thuốc", "Tên thuốc", "Thành phần"
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Bộ lọc bổ sung",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                LABEL_FONT,
                TITLE_COLOR
        ));

        JLabel lblCategory = new JLabel("Danh mục:");
        lblCategory.setFont(LABEL_FONT);

        // Initialize with "All" option first
        cboDanhMuc = new JComboBox<>();
        cboDanhMuc.addItem(null); // Null represents "All"
        cboDanhMuc.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "Tất cả";
                } else {
                    value = ((DanhMuc) value).getTen();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        cboDanhMuc.setFont(LABEL_FONT);
        cboDanhMuc.setPreferredSize(new Dimension(180, 30));
        cboDanhMuc.addActionListener(e -> loadData());

        JLabel lblManufacturer = new JLabel("Nhà sản xuất:");
        lblManufacturer.setFont(LABEL_FONT);

        cboNhaSanXuat = new JComboBox<>();
        cboNhaSanXuat.addItem(null); // Null represents "All"
        cboNhaSanXuat.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "Tất cả";
                } else {
                    value = ((NhaSanXuat) value).getTen();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        cboNhaSanXuat.setFont(LABEL_FONT);
        cboNhaSanXuat.setPreferredSize(new Dimension(180, 30));
        cboNhaSanXuat.addActionListener(e -> loadData());

        // Load data for comboboxes
        loadCategories();
        loadManufacturers();

        panel.add(lblCategory);
        panel.add(cboDanhMuc);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(lblManufacturer);
        panel.add(cboNhaSanXuat);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);

        // Table columns
        String[] columns = {
                "STT", "Mã thuốc", "Tên thuốc", "Đơn vị tính", "Danh mục", "Nhà sản xuất",
                "Số lượng tồn", "Đơn giá", "Hạn sử dụng"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 6) return Integer.class;
                if (columnIndex == 7) return Double.class;
                return String.class;
            }
        };

        tblResults = new JTable(tableModel);
        tblResults.setFont(new Font("Arial", Font.PLAIN, 14));
        tblResults.setRowHeight(30);
        tblResults.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblResults.getTableHeader().setReorderingAllowed(false);
        tblResults.getTableHeader().setBackground(BUTTON_BG);
        tblResults.getTableHeader().setForeground(Color.WHITE);

        // Set column widths
        tblResults.getColumnModel().getColumn(0).setMaxWidth(60);
        tblResults.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblResults.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblResults.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblResults.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblResults.getColumnModel().getColumn(5).setPreferredWidth(150);
        tblResults.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblResults.getColumnModel().getColumn(7).setPreferredWidth(120);
        tblResults.getColumnModel().getColumn(8).setPreferredWidth(120);

        // Add double click listener for details
        tblResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showMedicineDetails();
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
        btnViewDetails.addActionListener(e -> showMedicineDetails());

        JButton btnAdd = new JButton("Thêm thuốc mới");
        styleButton(btnAdd);
        btnAdd.addActionListener(e -> addNewMedicine());

        JButton btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cboFilterType.setSelectedIndex(0);
            cboDanhMuc.setSelectedIndex(0);
            cboNhaSanXuat.setSelectedIndex(0);
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

    private void loadCategories() {
        try {
            List<DanhMuc> categories = danhMucService.findAll();
            for (DanhMuc category : categories) {
                cboDanhMuc.addItem(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh mục: " + e.getMessage());
        }
    }

    private void loadManufacturers() {
        try {
            List<NhaSanXuat> manufacturers = nhaSanXuatService.findAll();
            for (NhaSanXuat manufacturer : manufacturers) {
                cboNhaSanXuat.addItem(manufacturer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải nhà sản xuất: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Thuoc> medicines = getFilteredMedicines();

            if (medicines == null) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thuốc từ hệ thống");
                return;
            }

            if (medicines.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc nào phù hợp với điều kiện");
                return;
            }

            int stt = 1;
            for (Thuoc thuoc : medicines) {
                String expiryDate = thuoc.getHanSuDung() != null ? dateFormat.format(thuoc.getHanSuDung()) : "N/A";
                String danhMuc = (thuoc.getDanhMuc() != null) ? thuoc.getDanhMuc().getTen() : "";
                String nhaSanXuat = (thuoc.getNhaSanXuat() != null) ? thuoc.getNhaSanXuat().getTen() : "";

                // Handle potential null price
                Double price = thuoc.getDonGia();
                Object priceValue = (price != null) ? price : 0.0;  // Or you could use null if your table allows it
                // Or, to format here for display (less ideal, but works):
                // String priceValue = (price != null) ? currencyFormat.format(price) : "N/A";


                tableModel.addRow(new Object[] {
                        stt++,
                        thuoc.getId(),
                        thuoc.getTen(),
                        thuoc.getDonViTinh(),
                        danhMuc,
                        nhaSanXuat,
                        thuoc.getSoLuongTon(),
                        priceValue,  // Add the Double or formatted String
                        expiryDate
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    private List<Thuoc> getFilteredMedicines() {
        try {
            DanhMuc selectedCategory = (DanhMuc) cboDanhMuc.getSelectedItem();
            NhaSanXuat selectedManufacturer = (NhaSanXuat) cboNhaSanXuat.getSelectedItem();

            List<Thuoc> result;

            if (selectedCategory == null && selectedManufacturer == null) {
                // No filters
                result = thuocService.findAll();
            } else if (selectedCategory != null && selectedManufacturer == null) {
                // Filter by category only
                result = thuocService.findByCategory(selectedCategory.getId());
            } else if (selectedCategory == null && selectedManufacturer != null) {
                // Filter by manufacturer only
                result = thuocService.findByManufacturer(selectedManufacturer.getId());
            } else {
                // Filter by both category and manufacturer
                result = thuocService.findByCategoryAndManufacturer(selectedCategory.getId(), selectedManufacturer.getId());
            }

            return result;
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
            List<Thuoc> results = null;

            switch (searchType) {
                case "Tất cả":
                    results = thuocService.findAll();
                    break;
                case "Mã thuốc":
                    Thuoc thuoc = thuocService.findById(searchValue);
                    if (thuoc != null) {
                        results = List.of(thuoc);
                    } else {
                        results = List.of(); // Empty list if not found
                    }
                    break;
                case "Tên thuốc":
                    results = thuocService.findByName(searchValue);
                    break;
                case "Thành phần":
                    results = thuocService.findByIngredient(searchValue);
                    break;
            }

            if (results == null) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thuốc từ hệ thống");
                return;
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc nào phù hợp với điều kiện tìm kiếm");
                return;
            }

            // Apply category and manufacturer filters
            DanhMuc selectedCategory = (DanhMuc) cboDanhMuc.getSelectedItem();
            NhaSanXuat selectedManufacturer = (NhaSanXuat) cboNhaSanXuat.getSelectedItem();

            if (selectedCategory != null) {
                results = results.stream()
                        .filter(t -> t.getDanhMuc() != null && t.getDanhMuc().getId().equals(selectedCategory.getId()))
                        .toList();
            }

            if (selectedManufacturer != null) {
                results = results.stream()
                        .filter(t -> t.getNhaSanXuat() != null && t.getNhaSanXuat().getId().equals(selectedManufacturer.getId()))
                        .toList();
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc nào phù hợp với điều kiện tìm kiếm và bộ lọc");
                return;
            }

            int stt = 1;
            for (Thuoc thuoc : results) {
                String expiryDate = thuoc.getHanSuDung() != null ? dateFormat.format(thuoc.getHanSuDung()) : "N/A";
                String danhMuc = (thuoc.getDanhMuc() != null) ? thuoc.getDanhMuc().getTen() : "";
                String nhaSanXuat = (thuoc.getNhaSanXuat() != null) ? thuoc.getNhaSanXuat().getTen() : "";

                // Handle potential null price
                Double price = thuoc.getDonGia();
                Object priceValue = (price != null) ? price : 0.0;  // Or you could use null if your table allows it
                // Or, to format here for display (less ideal, but works):
                // String priceValue = (price != null) ? currencyFormat.format(price) : "N/A";


                tableModel.addRow(new Object[] {
                        stt++,
                        thuoc.getId(),
                        thuoc.getTen(),
                        thuoc.getDonViTinh(),
                        danhMuc,
                        nhaSanXuat,
                        thuoc.getSoLuongTon(),
                        priceValue,  // Add the Double or formatted String
                        expiryDate
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }

    private void showMedicineDetails() {
        int row = tblResults.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thuốc để xem chi tiết");
            return;
        }

        String medicineId = tableModel.getValueAt(row, 1).toString();

        try {
            Thuoc thuoc = thuocService.findById(medicineId);
            if (thuoc != null) {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết thuốc", true);
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(this);

                // Detail panel
                JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
                detailPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

                // Left panel for image and basic info
                JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
                leftPanel.setPreferredSize(new Dimension(300, 0));

                // Image panel
                JPanel imagePanel = new JPanel(new BorderLayout());
                imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                imagePanel.setPreferredSize(new Dimension(300, 300));

                // Load and display image
                String baseResourcePath = "/product-image/"; // Assuming this is where images are stored
                String imageName = thuoc.getHinhAnh();
                ImageIcon imageIcon = null;
                String defaultImagePath = "/product-image/default_image.png"; // Path to a default image

                if (imageName != null && !imageName.trim().isEmpty()) {
                    java.net.URL imageUrl = getClass().getResource(baseResourcePath + imageName);

                    if (imageUrl != null) {
                        imageIcon = new ImageIcon(imageUrl);
                    } else {
                        // Attempt to load from file system (less common in web apps, careful with paths)
                        File imageFile = new File("src/main/resources/product-image/" + imageName);
                        if (imageFile.exists()) {
                            imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                        } else {
                            // Load default image from resources
                            imageIcon = new ImageIcon(getClass().getResource(defaultImagePath));
                        }
                    }
                } else {
                    // Load default image from resources
                    imageIcon = new ImageIcon(getClass().getResource(defaultImagePath));
                }

                if (imageIcon != null && imageIcon.getImage() != null) {
                    Image scaledImage = imageIcon.getImage()
                            .getScaledInstance(280, 280, Image.SCALE_SMOOTH); // Fixed size for display
                    JLabel lblImage = new JLabel(new ImageIcon(scaledImage));
                    lblImage.setHorizontalAlignment(JLabel.CENTER);
                    imagePanel.add(lblImage, BorderLayout.CENTER);
                } else {
                    // If even the default image fails, show a text message
                    JLabel lblNoImage = new JLabel("Không thể tải hình ảnh");
                    lblNoImage.setHorizontalAlignment(JLabel.CENTER);
                    imagePanel.add(lblNoImage, BorderLayout.CENTER);
                }

                leftPanel.add(imagePanel, BorderLayout.CENTER);

                // Right panel for detailed info
                JPanel rightPanel = new JPanel(new BorderLayout());

                // Form panel for medicine details
                JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                // Add medicine details fields
                addDetailField(formPanel, "Mã thuốc:", thuoc.getId());
                addDetailField(formPanel, "Tên thuốc:", thuoc.getTen());
                addDetailField(formPanel, "Đơn vị tính:", thuoc.getDonViTinh());
                addDetailField(formPanel, "Thành phần:", thuoc.getThanhPhan());
                addDetailField(formPanel, "Danh mục:", thuoc.getDanhMuc() != null ? thuoc.getDanhMuc().getTen() : "");
                addDetailField(formPanel, "Nhà sản xuất:", thuoc.getNhaSanXuat() != null ? thuoc.getNhaSanXuat().getTen() : "");
                addDetailField(formPanel, "Số lượng tồn:", String.valueOf(thuoc.getSoLuongTon()));
                addDetailField(formPanel, "Đơn giá:", currencyFormat.format(thuoc.getDonGia()) + " VND");
                addDetailField(formPanel, "Hạn sử dụng:", thuoc.getHanSuDung() != null ? dateFormat.format(thuoc.getHanSuDung()) : "N/A");

                rightPanel.add(formPanel, BorderLayout.CENTER);

                // Add panels to detail panel
                detailPanel.add(leftPanel, BorderLayout.WEST);
                detailPanel.add(rightPanel, BorderLayout.CENTER);

                // Add buttons panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

                JButton btnEdit = new JButton("Sửa thông tin");
                styleButton(btnEdit);
                btnEdit.setPreferredSize(new Dimension(130, 35));

                JButton btnClose = new JButton("Đóng");
                styleButton(btnClose);
                btnClose.setPreferredSize(new Dimension(100, 35));
                btnClose.addActionListener(e -> dialog.dispose());


                buttonPanel.add(btnClose);

                detailPanel.add(buttonPanel, BorderLayout.SOUTH);

                dialog.add(detailPanel);
                dialog.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị chi tiết thuốc: " + ex.getMessage());
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

    private void addNewMedicine() {
        // You would implement this to open a dialog for adding a new medicine
        // This is just a placeholder
        JOptionPane.showMessageDialog(this, "Chức năng thêm thuốc đang được phát triển");
    }
}