package gui;

import entity.*;
import other.RandomMa;
import service.NhanVienService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.List;

public class gui_nhanVien extends JPanel implements ActionListener {

    private JTextField txtMaNV, txtTenNV, txtDienThoai, txtNamSinh, txtSearch;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnXoa, btnSua, btnLamMoi, btnSearch;
    private JTable table;
    private DefaultTableModel model;
    private NhanVienService NHAN_VIEN_SERVICE;
    private static TaiKhoan loginNV = null;

    // Main panels
    private JPanel pnlMain;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;
    private JScrollPane scrollPane;

    // Define colors and fonts for styling
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color TITLE_COLOR = new Color(0, 102, 204);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Nhân Viên");
            gui_nhanVien panel = null;
            try {
                panel = new gui_nhanVien();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            frame.setContentPane(panel);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public gui_nhanVien() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            NHAN_VIEN_SERVICE = (NhanVienService) registry.lookup("NhanVienService");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo service: " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

        // Create UI
        createUI();

        // Load initial data
        docDuLieuDatabaseVaoTable();
    }

    private void createUI() {
        // Set layout for this panel
        setLayout(new BorderLayout());

        // Main panel with BorderLayout
        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
        pnlMain.setBackground(BACKGROUND_COLOR);
        add(pnlMain, BorderLayout.CENTER);

        // NORTH panel - Title
        pnlNorth = new JPanel();
        pnlNorth.setBackground(BACKGROUND_COLOR);
        pnlNorth.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(TITLE_COLOR);
        pnlNorth.add(lblTitle);
        pnlMain.add(pnlNorth, BorderLayout.NORTH);

        // CENTER panel - Form and Table
        pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(BACKGROUND_COLOR);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);

        // Form panel
        JPanel pnlForm = createFormPanel();
        pnlCenter.add(pnlForm, BorderLayout.WEST);

        // Table panel
        JPanel pnlTable = createTablePanel();
        pnlCenter.add(pnlTable, BorderLayout.CENTER);

        // SOUTH panel - Buttons
        pnlSouth = createButtonPanel();
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);

        // Add action listeners
        attachButtonListeners();

        // Generate initial ID
        generateRandomNhanVienId();
    }

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new BorderLayout(0, 10));
        pnlForm.setBackground(BACKGROUND_COLOR);

        // Main form with employee information
        JPanel pnlInfo = new JPanel(new GridBagLayout());
        pnlInfo.setBackground(BACKGROUND_COLOR);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Thông Tin Nhân Viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                LABEL_FONT,
                TITLE_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Mã Nhân Viên
        JLabel lblMaNV = new JLabel("Mã Nhân Viên:");
        lblMaNV.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInfo.add(lblMaNV, gbc);

        txtMaNV = new JTextField(20);
        txtMaNV.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlInfo.add(txtMaNV, gbc);

        // Tên Nhân Viên
        JLabel lblTenNV = new JLabel("Tên Nhân Viên:");
        lblTenNV.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlInfo.add(lblTenNV, gbc);

        txtTenNV = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlInfo.add(txtTenNV, gbc);

        // Điện Thoại
        JLabel lblDienThoai = new JLabel("Điện Thoại:");
        lblDienThoai.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        pnlInfo.add(lblDienThoai, gbc);

        txtDienThoai = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        pnlInfo.add(txtDienThoai, gbc);

        // Giới Tính
        JLabel lblGioiTinh = new JLabel("Giới Tính:");
        lblGioiTinh.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        pnlInfo.add(lblGioiTinh, gbc);

        cboGioiTinh = new JComboBox<>(new String[]{"Nữ", "Nam"});
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        pnlInfo.add(cboGioiTinh, gbc);

        // Năm Sinh
        JLabel lblNamSinh = new JLabel("Năm Sinh:");
        lblNamSinh.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        pnlInfo.add(lblNamSinh, gbc);

        txtNamSinh = new JTextField(20);
        txtNamSinh.setToolTipText("Nhập năm sinh (vd: 1995)");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        pnlInfo.add(txtNamSinh, gbc);

        // Search panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(BACKGROUND_COLOR);
        pnlSearch.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblSearch = new JLabel("Tìm Kiếm:");
        lblSearch.setFont(LABEL_FONT);
        txtSearch = new JTextField(20);

        btnSearch = new JButton("Tìm");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        // Add search field key listener
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    timKiemNhanVien();
                }
            }
        });

        // Add form and search panel to the container
        pnlForm.add(pnlInfo, BorderLayout.CENTER);
        pnlForm.add(pnlSearch, BorderLayout.SOUTH);

        return pnlForm;
    }

    private JPanel createTablePanel() {
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(BACKGROUND_COLOR);

        String[] columnNames = {"Mã NV", "Tên Nhân Viên", "Điện Thoại", "Giới Tính", "Năm Sinh"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);   // Mã NV
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên NV
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Điện Thoại
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Giới Tính
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Năm Sinh

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Danh Sách Nhân Viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                LABEL_FONT,
                TITLE_COLOR));

        pnlTable.add(scrollPane, BorderLayout.CENTER);

        // Add table selection listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(model.getValueAt(row, 0).toString());
                    txtTenNV.setText(model.getValueAt(row, 1).toString());
                    txtDienThoai.setText(model.getValueAt(row, 2).toString());

                    String gioiTinh = model.getValueAt(row, 3).toString();
                    cboGioiTinh.setSelectedItem(gioiTinh);

                    txtNamSinh.setText(model.getValueAt(row, 4).toString());

                    // Disable ID field for updates
                    txtMaNV.setEditable(false);
                }
            }
        });

        return pnlTable;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(BACKGROUND_COLOR);
        pnlButtons.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Create buttons with uniform size and styling
        Dimension buttonSize = new Dimension(150, 40);

        btnThem = new JButton("Thêm Nhân Viên");
        styleButton(btnThem, buttonSize);

        btnXoa = new JButton("Xóa Nhân Viên");
        styleButton(btnXoa, buttonSize);

        btnSua = new JButton("Sửa Thông Tin");
        styleButton(btnSua, buttonSize);

        btnLamMoi = new JButton("Làm Mới");
        styleButton(btnLamMoi, buttonSize);

        // Add buttons to panel with spacing
        pnlButtons.add(btnThem);
        pnlButtons.add(Box.createHorizontalStrut(10));
        pnlButtons.add(btnXoa);
        pnlButtons.add(Box.createHorizontalStrut(10));
        pnlButtons.add(btnSua);
        pnlButtons.add(Box.createHorizontalStrut(10));
        pnlButtons.add(btnLamMoi);

        return pnlButtons;
    }

    private void styleButton(JButton button, Dimension size) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(size);
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

    private void attachButtonListeners() {
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSearch.addActionListener(this);
    }

    private void themNhanVien() {
        try {
            // Get values from form
            String maNV = txtMaNV.getText().trim();
            String tenNV = txtTenNV.getText().trim();
            String dienThoai = txtDienThoai.getText().trim();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            boolean gioiTinhBoolean = gioiTinh.equals("Nữ");

            // Validate required fields
            if (maNV.isEmpty() || tenNV.isEmpty() || dienThoai.isEmpty() || txtNamSinh.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin bắt buộc!");
                return;
            }

            // Validate phone number
            if (!dienThoai.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
                return;
            }

            // Validate and parse year
            int namSinh;
            try {
                namSinh = Integer.parseInt(txtNamSinh.getText().trim());
                int currentYear = LocalDate.now().getYear();
                if (namSinh < 1900 || namSinh > currentYear) {
                    JOptionPane.showMessageDialog(null, "Năm sinh không hợp lệ. Phải từ 1900 đến " + currentYear);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Năm sinh phải là một số!");
                return;
            }

            // Create current date for ngayVaoLam
            LocalDate ngayVaoLam = LocalDate.now();

            // Create NhanVien object
            NhanVien nhanVien = new NhanVien();
            nhanVien.setId(maNV);
            nhanVien.setHoTen(tenNV);
            nhanVien.setSoDienThoai(dienThoai);
            nhanVien.setGioiTinh(gioiTinhBoolean);
            nhanVien.setNamSinh(namSinh);
            nhanVien.setNgayVaoLam(ngayVaoLam);

            // Save to database
            if (NHAN_VIEN_SERVICE.save(nhanVien)) {
                docDuLieuDatabaseVaoTable();
                JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm nhân viên thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }

    private void suaNhanVien() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa");
                return;
            }

            String maNV = txtMaNV.getText().trim();
            String tenNV = txtTenNV.getText().trim();
            String dienThoai = txtDienThoai.getText().trim();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            boolean gioiTinhBoolean = gioiTinh.equals("Nữ");

            // Validate required fields
            if (maNV.isEmpty() || tenNV.isEmpty() || dienThoai.isEmpty() || txtNamSinh.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin bắt buộc!");
                return;
            }

            // Validate phone number
            if (!dienThoai.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
                return;
            }

            // Validate and parse year
            int namSinh;
            try {
                namSinh = Integer.parseInt(txtNamSinh.getText().trim());
                int currentYear = LocalDate.now().getYear();
                if (namSinh < 1900 || namSinh > currentYear) {
                    JOptionPane.showMessageDialog(null, "Năm sinh không hợp lệ. Phải từ 1900 đến " + currentYear);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Năm sinh phải là một số!");
                return;
            }

            // Create NhanVien object
            NhanVien nhanVien = new NhanVien();
            nhanVien.setId(maNV);
            nhanVien.setHoTen(tenNV);
            nhanVien.setSoDienThoai(dienThoai);
            nhanVien.setGioiTinh(gioiTinhBoolean);
            nhanVien.setNamSinh(namSinh);

            // Update database
            if (NHAN_VIEN_SERVICE.update(nhanVien)) {
                docDuLieuDatabaseVaoTable();
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + ex.getMessage());
        }
    }

    private void xoaNhanVien() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn một dòng để xóa!");
                return;
            }

            String maNV = model.getValueAt(row, 0).toString();
            String tenNV = model.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn xóa nhân viên '" + tenNV + "' không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (NHAN_VIEN_SERVICE.delete(maNV)) {
                docDuLieuDatabaseVaoTable();
                lamMoiForm();
                JOptionPane.showMessageDialog(null, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Xóa thất bại! Nhân viên có thể đang được sử dụng bởi dữ liệu khác.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + ex.getMessage());
        }
    }

    private void timKiemNhanVien() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            docDuLieuDatabaseVaoTable();
            return;
        }

        try {
            // Implement search functionality here
            // This would need a new method in your service
            // For now, we'll just show a message
            JOptionPane.showMessageDialog(null, "Chức năng tìm kiếm đang được phát triển");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

    private void lamMoiForm() {
        generateRandomNhanVienId();
        txtTenNV.setText("");
        txtDienThoai.setText("");
        txtNamSinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtSearch.setText("");
        table.clearSelection();
        txtTenNV.requestFocus();
        txtMaNV.setEditable(false);
    }

    private void generateRandomNhanVienId() {
        txtMaNV.setText(RandomMa.maNVAuto());
    }

    private void docDuLieuDatabaseVaoTable() {
        try {
            model.setRowCount(0);
            List<NhanVien> listNhanVien = NHAN_VIEN_SERVICE.findAll();
            for (NhanVien nhanVien : listNhanVien) {
                model.addRow(new Object[]{
                        nhanVien.getId(),
                        nhanVien.getHoTen(),
                        nhanVien.getSoDienThoai(),
                        nhanVien.isGioiTinh() ? "Nữ" : "Nam",
                        nhanVien.getNamSinh()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) {
            themNhanVien();
        } else if (e.getSource() == btnXoa) {
            xoaNhanVien();
        } else if (e.getSource() == btnSua) {
            suaNhanVien();
        } else if (e.getSource() == btnLamMoi) {
            lamMoiForm();
        } else if (e.getSource() == btnSearch) {
            timKiemNhanVien();
        }
    }
}