package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

    public gui_qliNhaSX() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            NSX_SERVICE = (NhaSanXuatService) registry.lookup("NhaSanXuatService");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        setLayout(new BorderLayout(5, 5));
        setSize(1200, 800);

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ SẢN XUẤT", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.RED);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaNSX = new JLabel("Mã Nhà Sản Xuất:");
        lblMaNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnInfo.add(lblMaNSX, gbc);

        txtMaNSX = new CustomTextField(20);
        txtMaNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        txtMaNSX.setPreferredSize(new Dimension((int) txtMaNSX.getPreferredSize().getWidth(), 30));
        gbc.gridx = 1;
        pnInfo.add(txtMaNSX, gbc);

        JLabel lblTenNSX = new JLabel("Tên Nhà Sản Xuất:");
        lblTenNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnInfo.add(lblTenNSX, gbc);

        txtTenNSX = new CustomTextField(20);
        txtTenNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        txtTenNSX.setPreferredSize(new Dimension((int) txtTenNSX.getPreferredSize().getWidth(), 30));
        gbc.gridx = 1;
        pnInfo.add(txtTenNSX, gbc);

        pnInfo.setBorder(BorderFactory.createTitledBorder("Thông Tin"));
        add(pnInfo, BorderLayout.WEST);

        JPanel pnButtons = new JPanel(); 

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

        add(pnButtons, BorderLayout.SOUTH);

        String[] columnNames = {"Mã Nhà Sản Xuất", "Tên Nhà Sản Xuất"};
        model = new DefaultTableModel(columnNames, 0);
        table = new CustomTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Nhà Sản Xuất"));
        add(scrollPane, BorderLayout.CENTER);

        DocDuLieuDatabaseVaoTable();

        table.addMouseListener(this);
        pnInfo.addMouseListener(this);
        pnButtons.addMouseListener(this);
        scrollPane.addMouseListener(this);

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
                    if (!maNSX.matches("NSX\\d+")) {
                        JOptionPane.showMessageDialog(null, "Mã nhà sản xuất phải bắt đầu bằng kí tự NSX theo sau là các kí tự số!");
                        return;
                    }
                    if (NSX_SERVICE.findById(maNSX) != null) {
                        JOptionPane.showMessageDialog(null, "Mã nhà sản xuất này đã tồn tại. Vui lòng nhập mã khác.");
                        return;
                    }

                    NhaSanXuat nsx = new NhaSanXuat(maNSX, tenNSX);
                    try {
                        NSX_SERVICE.save(nsx);
                        model.addRow(new Object[]{nsx.getId(), nsx.getTen()});
                        JOptionPane.showMessageDialog(null, "Đã thêm nhà sản xuất");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi thêm nhà sản xuất");
                        ex.printStackTrace();
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng để xóa");
                        return;
                    }
                    String maNhaSanXuat = (String) model.getValueAt(row, 0);
                    if (NSX_SERVICE.delete(maNhaSanXuat)) {
                        model.removeRow(row);
                        txtMaNSX.setEnabled(true);
                        txtMaNSX.setText(null);
                        txtTenNSX.setText(null);
                        JOptionPane.showMessageDialog(null, "Đã xóa nhà sản xuất");
                    } else {
                        JOptionPane.showMessageDialog(null, "Lỗi khi xóa nhà sản xuất");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi xóa nhà sản xuất: " + ex.getMessage());
                }
            }
        });

        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để sửa");
                        return;
                    }

                    String maNSX = txtMaNSX.getText().trim();
                    String tenNSX = txtTenNSX.getText().trim();

                    if (maNSX.isEmpty() || tenNSX.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
                        return;
                    }

                    NhaSanXuat nsx = new NhaSanXuat(maNSX, tenNSX);

                    if (NSX_SERVICE.update(nsx)) {
                        model.setValueAt(maNSX, row, 0);
                        model.setValueAt(tenNSX, row, 1);
                        JOptionPane.showMessageDialog(null, "Đã cập nhật nhà sản xuất");
                    } else {
                        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật nhà sản xuất");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi cập nhật nhà sản xuất: " + ex.getMessage());
                }
            }
        });
    }

    private void DocDuLieuDatabaseVaoTable() throws RemoteException {
        ArrayList<NhaSanXuat> list = new ArrayList<>(NSX_SERVICE.findAll());
        model.setRowCount(0);
        for (NhaSanXuat nsx : list) {
            model.addRow(new Object[]{nsx.getId(), nsx.getTen()});
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!table.equals(e.getSource())) {
            txtMaNSX.setEnabled(true);
            table.getSelectionModel().clearSelection();
            if(e.getClickCount() == 2) {
                txtMaNSX.setText(null);
                txtTenNSX.setText(null);
            }
            return;
        }
        txtMaNSX.setEnabled(false);
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
