package gui;

import com.toedter.calendar.JDateChooser;
import entity.KhuyenMai;
import service.KhuyenMaiService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class gui_qliKhuyenMai extends JPanel implements MouseListener {

    private JTextField txtMaKM, txtTenKM, txtPhanTramKM, txtMoTa;
    private JDateChooser dateNgayBD, dateNgayKT;
    private JButton btnThem, btnXoa, btnSua, btnLamMoi, btnThoat;
    private JTable table;
    private DefaultTableModel model;
    private KhuyenMaiService KHUYEN_MAI_SERVICE;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    // Define colors and fonts for styling
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color TITLE_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Khuyến Mãi");
            gui_qliKhuyenMai panel = new gui_qliKhuyenMai();
            frame.setContentPane(panel);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public gui_qliKhuyenMai() {
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            KHUYEN_MAI_SERVICE = (KhuyenMaiService) registry.lookup("KhuyenMaiService");
        } catch (RemoteException | NotBoundException e) {
           // KHUYEN_MAI_SERVICE = new KhuyenMaiServiceImpl(new KhuyenMaiDAOImpl());
            e.printStackTrace();
        }

        this.setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel pnTitle = createTitlePanel();
        add(pnTitle, BorderLayout.NORTH);

        // Center Panel with info panel
        JPanel pnCenter = new JPanel(new BorderLayout(10, 0));
        pnCenter.setBackground(BACKGROUND_COLOR);

        // Info Panel
        JPanel pnInfo = createInfoPanel();
        pnInfo.setPreferredSize(new Dimension(400, 350));

        pnCenter.add(pnInfo, BorderLayout.NORTH);

        // Button Panel
        JPanel pnButtons = createButtonPanel();
        pnCenter.add(pnButtons, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.EAST);

        // Table Panel
        JScrollPane scrollPane = createTablePanel();
        scrollPane.setPreferredSize(new Dimension(700, 500));
        add(scrollPane, BorderLayout.CENTER);

        loadDataToTable();
        table.addMouseListener(this);
        attachButtonListeners();
        
        generateRandomKhuyenMaiId();
    }

    private JPanel createTitlePanel() {
        JPanel pnTitle = new JPanel();
        pnTitle.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("QUẢN LÝ KHUYẾN MÃI", JLabel.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTitle.setForeground(TITLE_COLOR);
        pnTitle.add(lblTitle);
        pnTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return pnTitle;
    }

    private JPanel createInfoPanel() {
        JPanel pnInfo = new JPanel();
        pnInfo.setBackground(BACKGROUND_COLOR);
        pnInfo.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Thông Tin Khuyến Mãi",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                LABEL_FONT, TITLE_COLOR));

        GroupLayout layout = new GroupLayout(pnInfo);
        pnInfo.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Labels
        JLabel lblMaKM = createLabel("Mã Khuyến Mãi:");
        JLabel lblTenKM = createLabel("Tên Khuyến Mãi:");
        JLabel lblPhanTramKM = createLabel("Phần Trăm KM:");
        JLabel lblNgayBD = createLabel("Ngày Bắt Đầu:");
        JLabel lblNgayKT = createLabel("Ngày Kết Thúc:");
        JLabel lblMoTa = createLabel("Mô Tả:");

        // Input fields
        txtMaKM = new JTextField(10);
        txtMaKM.setEditable(false); // ID is auto-generated
        txtTenKM = new JTextField(20);
        txtPhanTramKM = new JTextField(10);
        dateNgayBD = new JDateChooser();
        dateNgayBD.setDateFormatString("dd/MM/yyyy");
        dateNgayBD.setDate(new Date());
        dateNgayKT = new JDateChooser();
        dateNgayKT.setDateFormatString("dd/MM/yyyy");
        dateNgayKT.setDate(new Date());
        txtMoTa = new JTextField(30);

        // Horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(lblMaKM)
                        .addComponent(lblTenKM)
                        .addComponent(lblPhanTramKM)
                        .addComponent(lblNgayBD)
                        .addComponent(lblNgayKT)
                        .addComponent(lblMoTa))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtMaKM)
                        .addComponent(txtTenKM)
                        .addComponent(txtPhanTramKM)
                        .addComponent(dateNgayBD)
                        .addComponent(dateNgayKT)
                        .addComponent(txtMoTa))
        );

        // Vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMaKM)
                        .addComponent(txtMaKM))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTenKM)
                        .addComponent(txtTenKM))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPhanTramKM)
                        .addComponent(txtPhanTramKM))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNgayBD)
                        .addComponent(dateNgayBD))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNgayKT)
                        .addComponent(dateNgayKT))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMoTa)
                        .addComponent(txtMoTa))
        );

        return pnInfo;
    }

    private JPanel createButtonPanel() {
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnButtons.setBackground(BACKGROUND_COLOR);

        btnThem = createButton("Thêm Khuyến Mãi");
        btnXoa = createButton("Xóa Khuyến Mãi");
        btnSua = createButton("Sửa Thông Tin");
        btnLamMoi = createButton("Làm Mới");
        btnThoat = createButton("Thoát");

        pnButtons.add(btnThem);
        pnButtons.add(btnXoa);
        pnButtons.add(btnSua);
        pnButtons.add(btnLamMoi);
        pnButtons.add(btnThoat);

        pnButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return pnButtons;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Mã KM", "Tên Khuyến Mãi", "Phần Trăm KM", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Mô Tả"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Danh Sách Khuyến Mãi",
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
        button.setFocusPainted(false);
        button.setBorderPainted(false);
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

        return button;
    }

    private void attachButtonListeners() {
        btnThem.addActionListener(e -> themKhuyenMai());
        btnXoa.addActionListener(e -> xoaKhuyenMai());
        btnSua.addActionListener(e -> suaKhuyenMai());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnThoat.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }
            
            try {
                gui_TrangChu mainFrame = new gui_TrangChu(null);
                mainFrame.setVisible(true);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi mở trang chủ: " + ex.getMessage());
            }
        });
    }

    private void themKhuyenMai() {
        try {
            // Get values from form
            String maKM = txtMaKM.getText().trim();
            String tenKM = txtTenKM.getText().trim();
            String moTa = txtMoTa.getText().trim();
            
            // Validate required fields
            if (maKM.isEmpty() || tenKM.isEmpty() || dateNgayBD.getDate() == null || 
                dateNgayKT.getDate() == null || txtPhanTramKM.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            // Parse numeric values
            double phanTramGiamGia;
            try {
                phanTramGiamGia = Double.parseDouble(txtPhanTramKM.getText().trim());
                if (phanTramGiamGia < 0 || phanTramGiamGia > 100) {
                    JOptionPane.showMessageDialog(null, "Phần trăm khuyến mãi phải từ 0-100!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Phần trăm khuyến mãi không hợp lệ!");
                return;
            }
            
            // Parse dates
            Date ngayBD = dateNgayBD.getDate();
            Date ngayKT = dateNgayKT.getDate();
            
            if (ngayBD.after(ngayKT)) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }
            
            // Convert to LocalDate for entity
            LocalDate thoiGianBatDau = ngayBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate thoiGianKetThuc = ngayKT.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Create KhuyenMai object
            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setId(maKM);
            khuyenMai.setTen(tenKM);
            khuyenMai.setPhanTramGiamGia(phanTramGiamGia);
            khuyenMai.setThoiGianBatDau(thoiGianBatDau);
            khuyenMai.setThoiGianKetThuc(thoiGianKetThuc);
            
            // Save to database
            if (KHUYEN_MAI_SERVICE.save(khuyenMai)) {
                loadDataToTable();
                JOptionPane.showMessageDialog(null, "Thêm khuyến mãi thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm khuyến mãi thất bại!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm khuyến mãi: " + e.getMessage());
        }
    }
    
    private void suaKhuyenMai() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa");
                return;
            }

            String maKM = txtMaKM.getText().trim();
            String tenKM = txtTenKM.getText().trim();
            String moTa = txtMoTa.getText().trim();
            
            // Validate required fields
            if (maKM.isEmpty() || tenKM.isEmpty() || dateNgayBD.getDate() == null || 
                dateNgayKT.getDate() == null || txtPhanTramKM.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            // Parse numeric values
            double phanTramGiamGia;
            try {
                phanTramGiamGia = Double.parseDouble(txtPhanTramKM.getText().trim());
                if (phanTramGiamGia < 0 || phanTramGiamGia > 100) {
                    JOptionPane.showMessageDialog(null, "Phần trăm khuyến mãi phải từ 0-100!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Phần trăm khuyến mãi không hợp lệ!");
                return;
            }
            
            // Parse dates
            Date ngayBD = dateNgayBD.getDate();
            Date ngayKT = dateNgayKT.getDate();
            
            if (ngayBD.after(ngayKT)) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }
            
            // Convert to LocalDate for entity
            LocalDate thoiGianBatDau = ngayBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate thoiGianKetThuc = ngayKT.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Create KhuyenMai object
            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setId(maKM);
            khuyenMai.setTen(tenKM);
            khuyenMai.setPhanTramGiamGia(phanTramGiamGia);
            khuyenMai.setThoiGianBatDau(thoiGianBatDau);
            khuyenMai.setThoiGianKetThuc(thoiGianKetThuc);

            // Update database
            if (KHUYEN_MAI_SERVICE.update(khuyenMai)) {
                loadDataToTable();
                JOptionPane.showMessageDialog(null, "Cập nhật khuyến mãi thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật khuyến mãi thất bại!");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + ex.getMessage());
        }
    }

    private void xoaKhuyenMai() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn một dòng để xóa!");
                return;
            }

            String maKM = model.getValueAt(row, 0).toString();
            String tenKM = model.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn xóa khuyến mãi '" + tenKM + "' không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (KHUYEN_MAI_SERVICE.delete(maKM)) {
                loadDataToTable();
                lamMoiForm();
                JOptionPane.showMessageDialog(null, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Xóa thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + ex.getMessage());
        }
    }

    private void lamMoiForm() {
        generateRandomKhuyenMaiId();
        txtTenKM.setText("");
        txtPhanTramKM.setText("");
        txtMoTa.setText("");
        dateNgayBD.setDate(new Date());
        dateNgayKT.setDate(new Date());
        table.clearSelection();
        txtTenKM.requestFocus();
    }

    private void generateRandomKhuyenMaiId() {
        txtMaKM.setText("KM" + String.format("%03d", (int) (Math.random() * 1000)));
    }

    private void loadDataToTable() {
        try {
            model.setRowCount(0);
            List<KhuyenMai> listKhuyenMai = KHUYEN_MAI_SERVICE.findAll();
            for (KhuyenMai khuyenMai : listKhuyenMai) {
                model.addRow(new Object[]{
                        khuyenMai.getId(),
                        khuyenMai.getTen(),
                        khuyenMai.getPhanTramGiamGia(),
                        khuyenMai.getThoiGianBatDau(),
                        khuyenMai.getThoiGianKetThuc(),
                        ""  // Empty for Mô Tả since it's not in the entity
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
            txtMaKM.setText(model.getValueAt(row, 0).toString());
            txtTenKM.setText(model.getValueAt(row, 1).toString());
            txtPhanTramKM.setText(model.getValueAt(row, 2).toString());
            
            // Handle dates from table
            LocalDate ngayBD = (LocalDate) model.getValueAt(row, 3);
            LocalDate ngayKT = (LocalDate) model.getValueAt(row, 4);
            
            Date ngayBatDau = Date.from(ngayBD.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date ngayKetThuc = Date.from(ngayKT.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            dateNgayBD.setDate(ngayBatDau);
            dateNgayKT.setDate(ngayKetThuc);
            
            txtMoTa.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
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
