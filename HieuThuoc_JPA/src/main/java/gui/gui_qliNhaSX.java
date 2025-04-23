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

import dao.NhaSanXuatDAO;
import dao.impl.NhaSanXuatDAOImpl;
import entity.NhaSanXuat;
import other.CustomButton;
import other.CustomTable;
import other.CustomTextField;
import service.NhaSanXuatService;

public class gui_qliNhaSX extends JPanel implements MouseListener {

    private CustomTextField txtMaNSX, txtTenNSX;
    private CustomButton btnThem, btnXoa, btnSua;
    private CustomTable table;
    private DefaultTableModel model;
    private NhaSanXuatService NSX_SERVICE;

    // Define colors and fonts for styling
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color TITLE_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Nhà Sản Xuất");
            gui_qliNhaSX panel = null;
            try {
                panel = new gui_qliNhaSX();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            frame.setContentPane(panel);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public gui_qliNhaSX() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            NSX_SERVICE = (NhaSanXuatService) registry.lookup("NhaSanXuatService");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(BACKGROUND_COLOR);

        // Title Panel
        JPanel pnTitle = createTitlePanel();
        add(pnTitle, BorderLayout.NORTH);

        // Center Panel with info form and buttons
        JPanel pnCenter = new JPanel(new BorderLayout(10, 0));
        pnCenter.setBackground(BACKGROUND_COLOR);

        // Info Panel
        JPanel pnInfo = createInfoPanel();
        pnCenter.add(pnInfo, BorderLayout.NORTH);

        // Buttons Panel
        JPanel pnButtons = createButtonPanel();
        pnCenter.add(pnButtons, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.EAST);

        // Table Panel
        JScrollPane scrollPane = createTablePanel();
        scrollPane.setPreferredSize(new Dimension(700, 500));
        add(scrollPane, BorderLayout.CENTER);

        DocDuLieuDatabaseVaoTable();
        attachListeners();
    }

    private JPanel createTitlePanel() {
        JPanel pnTitle = new JPanel();
        pnTitle.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ SẢN XUẤT", JLabel.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTitle.setForeground(TITLE_COLOR);
        pnTitle.add(lblTitle);
        pnTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return pnTitle;
    }

    private JPanel createInfoPanel() {
        JPanel pnInfo = new JPanel(new GridBagLayout());
        pnInfo.setBackground(BACKGROUND_COLOR);
        pnInfo.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Thông Tin Nhà Sản Xuất",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                LABEL_FONT, TITLE_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Mã NSX
        JLabel lblMaNSX = new JLabel("Mã Nhà Sản Xuất:");
        lblMaNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnInfo.add(lblMaNSX, gbc);

        txtMaNSX = new CustomTextField(20);
        txtMaNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        txtMaNSX.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnInfo.add(txtMaNSX, gbc);

        // Tên NSX
        JLabel lblTenNSX = new JLabel("Tên Nhà Sản Xuất:");
        lblTenNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnInfo.add(lblTenNSX, gbc);

        txtTenNSX = new CustomTextField(20);
        txtTenNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        txtTenNSX.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnInfo.add(txtTenNSX, gbc);

        return pnInfo;
    }

    private JPanel createButtonPanel() {
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnButtons.setBackground(BACKGROUND_COLOR);

        btnThem = new CustomButton("Thêm Nhà Sản Xuất");
        btnThem.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnThem.setPreferredSize(new Dimension(180, 40));

        btnXoa = new CustomButton("Xóa Nhà Sản Xuất");
        btnXoa.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnXoa.setPreferredSize(new Dimension(170, 40));

        btnSua = new CustomButton("Sửa Thông Tin");
        btnSua.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnSua.setPreferredSize(new Dimension(170, 40));

        pnButtons.add(btnThem);
        pnButtons.add(btnXoa);
        pnButtons.add(btnSua);

        pnButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return pnButtons;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Mã Nhà Sản Xuất", "Tên Nhà Sản Xuất"};
        model = new DefaultTableModel(columnNames, 0);
        table = new CustomTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Danh Sách Nhà Sản Xuất",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                LABEL_FONT, TITLE_COLOR));
        return scrollPane;
    }

    private void attachListeners() {
        table.addMouseListener(this);

        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String maNSX = txtMaNSX.getText().trim();
                    String tenNSX = txtTenNSX.getText().trim();

                    if (maNSX.isEmpty() || tenNSX.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
                        return;
                    }

                    NhaSanXuat nsx = new NhaSanXuat(maNSX, tenNSX);

                    if (NSX_SERVICE.save(nsx)) {
                        DocDuLieuDatabaseVaoTable();
                        JOptionPane.showMessageDialog(null, "Thêm nhà sản xuất thành công");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm nhà sản xuất thất bại");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                }
            }
        });

        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhà sản xuất để xóa");
                        return;
                    }

                    String maNSX = model.getValueAt(row, 0).toString();
                    String tenNSX = model.getValueAt(row, 1).toString();

                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc chắn muốn xóa nhà sản xuất " + tenNSX + "?",
                            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (NSX_SERVICE.delete(maNSX)) {
                            DocDuLieuDatabaseVaoTable();
                            clearFields();
                            JOptionPane.showMessageDialog(null, "Xóa nhà sản xuất thành công");
                        } else {
                            JOptionPane.showMessageDialog(null, "Xóa nhà sản xuất thất bại");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                }
            }
        });

        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhà sản xuất để sửa");
                        return;
                    }

                    String maNSX = txtMaNSX.getText().trim();
                    String tenNSX = txtTenNSX.getText().trim();

                    if (tenNSX.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Tên nhà sản xuất không được để trống");
                        return;
                    }

                    NhaSanXuat nsx = new NhaSanXuat(maNSX, tenNSX);

                    if (NSX_SERVICE.update(nsx)) {
                        DocDuLieuDatabaseVaoTable();
                        JOptionPane.showMessageDialog(null, "Cập nhật nhà sản xuất thành công");
                    } else {
                        JOptionPane.showMessageDialog(null, "Cập nhật nhà sản xuất thất bại");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                }
            }
        });
    }

    private void DocDuLieuDatabaseVaoTable() {
        try {
            model.setRowCount(0);
            List<NhaSanXuat> list = NSX_SERVICE.findAll();
            for (NhaSanXuat nsx : list) {
                model.addRow(new Object[]{nsx.getId(), nsx.getTen()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtMaNSX.setText("");
        txtTenNSX.setText("");
        table.clearSelection();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() != table) return;
        
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaNSX.setText(model.getValueAt(row, 0).toString());
            txtTenNSX.setText(model.getValueAt(row, 1).toString());
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
