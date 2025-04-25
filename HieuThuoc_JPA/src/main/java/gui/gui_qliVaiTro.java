package gui;

import dao.VaiTroDAO;
import dao.impl.VaiTroDAOImpl;
import entity.NhaSanXuat;
import entity.VaiTro;
import other.CustomButton;
import other.CustomTable;
import other.CustomTextField;
import service.VaiTroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class gui_qliVaiTro extends JPanel implements MouseListener {
    private CustomTextField txtMaVT, txtTenVT;
    private CustomButton btnThem, btnXoa, btnSua;
    private CustomTable table;
    private DefaultTableModel model;
    private VaiTroService VT_SERVICE;

    public gui_qliVaiTro() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry("172.20.10.12", 8989);
            VT_SERVICE = (VaiTroService) registry.lookup("VaiTroService");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        setLayout(new BorderLayout(5, 5));
        setSize(1200, 800);

        JLabel lblTitle = new JLabel("QUẢN LÝ VAI TRÒ", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.RED);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaNSX = new JLabel("Mã Vai Trò:");
        lblMaNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnInfo.add(lblMaNSX, gbc);

        txtMaVT = new CustomTextField(20);
        txtMaVT.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        txtMaVT.setPreferredSize(new Dimension((int) txtMaVT.getPreferredSize().getWidth(), 30));
        gbc.gridx = 1;
        pnInfo.add(txtMaVT, gbc);

        JLabel lblTenNSX = new JLabel("Tên Vai Trò:");
        lblTenNSX.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnInfo.add(lblTenNSX, gbc);

        txtTenVT = new CustomTextField(20);
        txtTenVT.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        txtTenVT.setPreferredSize(new Dimension((int) txtTenVT.getPreferredSize().getWidth(), 30));
        gbc.gridx = 1;
        pnInfo.add(txtTenVT, gbc);

        pnInfo.setBorder(BorderFactory.createTitledBorder("Thông Tin"));
        add(pnInfo, BorderLayout.WEST);

        JPanel pnButtons = new JPanel();

        btnThem = new CustomButton("Thêm Vai Trò");
        btnThem.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnThem.setPreferredSize(new Dimension(180, 40));
        btnXoa = new CustomButton("Xóa Vai Trò");
        btnXoa.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnXoa.setPreferredSize(new Dimension(170, 40));
        btnSua = new CustomButton("Sửa Vai Trò");
        btnSua.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnSua.setPreferredSize(new Dimension(170, 40));

        pnButtons.add(btnThem);
        pnButtons.add(btnXoa);
        pnButtons.add(btnSua);

        add(pnButtons, BorderLayout.SOUTH);

        String[] columnNames = {"Mã Vai Trò", "Tên Vai Trò"};
        model = new DefaultTableModel(columnNames, 0);
        table = new CustomTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Vai Trò"));
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
                    String maVT = txtMaVT.getText().trim();
                    String tenVT = txtTenVT.getText().trim();

                    if (maVT.isEmpty() || tenVT.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
                        return;
                    }
                    if (VT_SERVICE.findById(maVT) != null) {
                        JOptionPane.showMessageDialog(null, "Mã vai trò này đã tồn tại. Vui lòng nhập mã khác.");
                        return;
                    }

                    VaiTro vt = new VaiTro(maVT, tenVT);
                    try {
                        VT_SERVICE.save(vt);
                        model.addRow(new Object[]{vt.getId(), vt.getTenVaiTro()});
                        JOptionPane.showMessageDialog(null, "Đã thêm vai trò");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi thêm vai trò");
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
                    if (VT_SERVICE.delete(maNhaSanXuat)) {
                        model.removeRow(row);
                        JOptionPane.showMessageDialog(null, "Đã xóa vai trò");
                    } else {
                        JOptionPane.showMessageDialog(null, "Lỗi khi xóa vai trò");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi xóa vai trò: " + ex.getMessage());
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

                    String maVT = txtMaVT.getText().trim();
                    String tenVT = txtTenVT.getText().trim();

                    if (maVT.isEmpty() || tenVT.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
                        return;
                    }

                    VaiTro vt = new VaiTro(maVT, tenVT);

                    if (VT_SERVICE.update(vt)) {
                        model.setValueAt(maVT, row, 0);
                        model.setValueAt(tenVT, row, 1);
                        JOptionPane.showMessageDialog(null, "Đã cập nhật vai trò");
                    } else {
                        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật vai trò");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi cập nhật vai trò: " + ex.getMessage());
                }
            }
        });
    }

    private void DocDuLieuDatabaseVaoTable() throws RemoteException {
        ArrayList<VaiTro> list = new ArrayList<>(VT_SERVICE.findAll());
        model.setRowCount(0);
        for (VaiTro vt : list) {
            model.addRow(new Object[]{vt.getId(), vt.getTenVaiTro()});
        }
    }

    private void setEnableEdit(boolean enable){
        txtMaVT.setEnabled(enable);
        txtTenVT.setEnabled(enable);
        btnThem.setEnabled(enable);
        btnXoa.setEnabled(enable);
        btnSua.setEnabled(enable);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!table.equals(e.getSource())){
            setEnableEdit(true);
            table.getSelectionModel().clearSelection();
            if (e.getClickCount() == 2) {
                txtMaVT.setText(null);
                txtTenVT.setText(null);
            }
            return;
        }
        txtMaVT.setEnabled(false);
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaVT.setText(model.getValueAt(row, 0).toString());
            txtTenVT.setText(model.getValueAt(row, 1).toString());
            if(table.getValueAt(row, 0).toString().equals("admin")){
                setEnableEdit(false);
            }
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
