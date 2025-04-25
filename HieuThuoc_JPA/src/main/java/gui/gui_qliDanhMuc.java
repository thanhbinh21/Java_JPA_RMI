package gui;

import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import dao.impl.DanhMucDAOImpl;
import entity.DanhMuc;
import other.RandomMa;
import service.DanhMucService;
import service.impl.DanhMucServiceImpl;

public class gui_qliDanhMuc extends JPanel implements MouseListener {

    private JTextField txtMaDanhMuc, txtTenDanhMuc, txtViTriKe;
    private JButton btnThem, btnXoa, btnSua, btnLamMoi, btnThoat;
    private JTable table;
    private DefaultTableModel model;
    private DanhMucService DANH_MUC_SERVICE;

    // Define colors and fonts for styling
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color TITLE_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Danh Mục");
            gui_qliDanhMuc panel = new gui_qliDanhMuc();
            frame.setContentPane(panel);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public gui_qliDanhMuc() {
        try {
            Registry registry = LocateRegistry.getRegistry("172.20.10.12", 8989);

            DANH_MUC_SERVICE = (DanhMucService) registry.lookup("DanhMucService");
        } catch (RemoteException | NotBoundException e) {
           // DANH_MUC_SERVICE = new DanhMucServiceImpl(new DanhMucDAOImpl());
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
        pnInfo.setPreferredSize(new Dimension(400, 250));
        pnCenter.add(pnInfo, BorderLayout.NORTH);

        // Button Panel
        JPanel pnButtons = createButtonPanel();
        pnCenter.add(pnButtons, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.EAST);

        // Table Panel
        JScrollPane scrollPane = createTablePanel();
        scrollPane.setPreferredSize(new Dimension(600, 500));
        add(scrollPane, BorderLayout.CENTER);

        loadDataToTable();
        table.addMouseListener(this);
        attachButtonListeners();
        
        // Generate an ID initially
        generateDanhMucId();
    }

    private JPanel createTitlePanel() {
        JPanel pnTitle = new JPanel();
        pnTitle.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC", JLabel.CENTER);
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
                new LineBorder(Color.GRAY), "Thông Tin Danh Mục",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                LABEL_FONT, TITLE_COLOR));

        GroupLayout layout = new GroupLayout(pnInfo);
        pnInfo.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Labels
        JLabel lblMaDanhMuc = createLabel("Mã Danh Mục:");
        JLabel lblTenDanhMuc = createLabel("Tên Danh Mục:");
        JLabel lblViTriKe = createLabel("Vị Trí Kệ:");

        // Input fields
        txtMaDanhMuc = new JTextField(10);
        txtMaDanhMuc.setEditable(false); // ID is auto-generated
        txtTenDanhMuc = new JTextField(20);
        txtViTriKe = new JTextField(15);

        // Horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(lblMaDanhMuc)
                        .addComponent(lblTenDanhMuc)
                        .addComponent(lblViTriKe))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtMaDanhMuc)
                        .addComponent(txtTenDanhMuc)
                        .addComponent(txtViTriKe))
        );

        // Vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMaDanhMuc)
                        .addComponent(txtMaDanhMuc))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTenDanhMuc)
                        .addComponent(txtTenDanhMuc))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblViTriKe)
                        .addComponent(txtViTriKe))
        );

        return pnInfo;
    }

    private JPanel createButtonPanel() {
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnButtons.setBackground(BACKGROUND_COLOR);

        btnThem = createButton("Thêm");
        btnXoa = createButton("Xóa");
        btnSua = createButton("Sửa");
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
        String[] columnNames = {"Mã Danh Mục", "Tên Danh Mục", "Vị Trí Kệ"};
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
                new LineBorder(Color.GRAY), "Danh Sách Danh Mục",
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
        btnThem.addActionListener(e -> themDanhMuc());
        btnXoa.addActionListener(e -> xoaDanhMuc());
        btnSua.addActionListener(e -> suaDanhMuc());
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

    private void themDanhMuc() {
        try {
            // Get values from form
            String maDanhMuc = txtMaDanhMuc.getText().trim();
            String tenDanhMuc = txtTenDanhMuc.getText().trim();
            String viTriKe = txtViTriKe.getText().trim();
            
            // Validate required fields
            if (maDanhMuc.isEmpty() || tenDanhMuc.isEmpty() || viTriKe.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            // Create DanhMuc object
            DanhMuc danhMuc = new DanhMuc(maDanhMuc, tenDanhMuc, viTriKe);
            
            // Save to database
            if (DANH_MUC_SERVICE.save(danhMuc)) {
                loadDataToTable();
                JOptionPane.showMessageDialog(null, "Thêm danh mục thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm danh mục thất bại!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm danh mục: " + e.getMessage());
        }
    }
    
    private void suaDanhMuc() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa");
                return;
            }

            String maDanhMuc = txtMaDanhMuc.getText().trim();
            String tenDanhMuc = txtTenDanhMuc.getText().trim();
            String viTriKe = txtViTriKe.getText().trim();
            
            // Validate required fields
            if (maDanhMuc.isEmpty() || tenDanhMuc.isEmpty() || viTriKe.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            // Create DanhMuc object
            DanhMuc danhMuc = new DanhMuc(maDanhMuc, tenDanhMuc, viTriKe);

            // Update database
            if (DANH_MUC_SERVICE.update(danhMuc)) {
                loadDataToTable();
                JOptionPane.showMessageDialog(null, "Cập nhật danh mục thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật danh mục thất bại!");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + ex.getMessage());
        }
    }

    private void xoaDanhMuc() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn một dòng để xóa!");
                return;
            }

            String maDanhMuc = model.getValueAt(row, 0).toString();
            String tenDanhMuc = model.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa danh mục '" + tenDanhMuc + "' không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (DANH_MUC_SERVICE.delete(maDanhMuc)) {
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
        generateDanhMucId();
        txtTenDanhMuc.setText("");
        txtViTriKe.setText("");
        table.clearSelection();
        txtTenDanhMuc.requestFocus();
    }

    private void generateDanhMucId() {
        txtMaDanhMuc.setText("DM" + String.format("%03d", (int) (Math.random() * 1000)));
    }

    private void loadDataToTable() {
        try {
            model.setRowCount(0);
            List<DanhMuc> listDanhMuc = DANH_MUC_SERVICE.findAll();
            for (DanhMuc danhMuc : listDanhMuc) {
                model.addRow(new Object[]{
                    danhMuc.getId(),
                    danhMuc.getTen(),
                    danhMuc.getViTriKe()
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
            txtMaDanhMuc.setText(model.getValueAt(row, 0).toString());
            txtTenDanhMuc.setText(model.getValueAt(row, 1).toString());
            txtViTriKe.setText(model.getValueAt(row, 2).toString());
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
