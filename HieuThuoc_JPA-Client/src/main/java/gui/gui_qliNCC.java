package gui;

import dao.NhaCungCapDAO;
import dao.impl.NhaCungCapDAOImpl;
import entity.NhaCungCap;
import service.NhaCungCapService;
import service.impl.NhaCungCapServiceImpl;

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
import java.util.List;
import java.util.regex.Pattern;

public class gui_qliNCC extends JPanel implements ActionListener {
    private JPanel pnlMain;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;
    
    // Form components
    private JLabel lblTitle;
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblAddress;
    private JLabel lblPhone;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtSearch;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;
    
    // Table
    private JTable tblSuppliers;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    // Service
    private NhaCungCapService nhaCungCapService;
    
    public gui_qliNCC() {
        // Initialize service
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            nhaCungCapService = (NhaCungCapService) registry.lookup("NhaCungCapService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing services: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create UI
        createUI();
        
        // Load data
        loadSupplierData();
    }
    
    private void createUI() {
        // Set layout for this panel
        setLayout(new BorderLayout());
        
        // Main panel with BorderLayout
        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
        add(pnlMain, BorderLayout.CENTER);
        
        // NORTH panel - Title
        pnlNorth = new JPanel();
        pnlNorth.setBorder(new EmptyBorder(0, 0, 10, 0));
        lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle);
        pnlMain.add(pnlNorth, BorderLayout.NORTH);
        
        // CENTER panel - Form and Table
        pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        
        // Form panel
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(), 
                new EmptyBorder(10, 10, 10, 10)));
        pnlForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Thông Tin Nhà Cung Cấp", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        lblId = new JLabel("Mã NCC:");
        lblId.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlForm.add(lblId, gbc);
        
        txtId = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlForm.add(txtId, gbc);
        
        // Name
        lblName = new JLabel("Tên:");
        lblName.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlForm.add(lblName, gbc);
        
        txtName = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlForm.add(txtName, gbc);
        
        // Address
        lblAddress = new JLabel("Địa Chỉ:");
        lblAddress.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        pnlForm.add(lblAddress, gbc);
        
        txtAddress = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        pnlForm.add(txtAddress, gbc);
        
        // Phone
        lblPhone = new JLabel("Điện Thoại:");
        lblPhone.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        pnlForm.add(lblPhone, gbc);
        
        txtPhone = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        pnlForm.add(txtPhone, gbc);
        
        // Search panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lblSearch = new JLabel("Tìm Kiếm:");
        lblSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch = new JTextField(25);
        
        // Add key listener to search textfield
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchSuppliers();
                }
            }
        });
        
        btnSearch = new JButton("Tìm");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Handle potential missing icons gracefully
        try {
            btnSearch.setIcon(new ImageIcon(getClass().getResource("/icons/search.png")));
        } catch (Exception ex) {
            // Icon not found, continue without it
        }
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        
        // Add form and search panel to center-north
        JPanel pnlCenterNorth = new JPanel(new BorderLayout(0, 5));
        pnlCenterNorth.add(pnlForm, BorderLayout.CENTER);
        pnlCenterNorth.add(pnlSearch, BorderLayout.SOUTH);
        pnlCenter.add(pnlCenterNorth, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Mã NCC", "Tên", "Địa Chỉ", "Điện Thoại"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        tblSuppliers = new JTable(tableModel);
        tblSuppliers.setFont(new Font("Arial", Font.PLAIN, 14));
        tblSuppliers.setRowHeight(25);
        tblSuppliers.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Set column widths
        tblSuppliers.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tblSuppliers.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        tblSuppliers.getColumnModel().getColumn(2).setPreferredWidth(350); // Address
        tblSuppliers.getColumnModel().getColumn(3).setPreferredWidth(120); // Phone
        
        scrollPane = new JScrollPane(tblSuppliers);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        
        // Add table selection listener
        tblSuppliers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblSuppliers.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tblSuppliers.getValueAt(selectedRow, 0).toString();
                    String name = tblSuppliers.getValueAt(selectedRow, 1).toString();
                    String address = tblSuppliers.getValueAt(selectedRow, 2) != null ? 
                                    tblSuppliers.getValueAt(selectedRow, 2).toString() : "";
                    String phone = tblSuppliers.getValueAt(selectedRow, 3) != null ? 
                                  tblSuppliers.getValueAt(selectedRow, 3).toString() : "";
                    
                    // Set values to text fields
                    txtId.setText(id);
                    txtName.setText(name);
                    txtAddress.setText(address);
                    txtPhone.setText(phone);
                    
                    // Disable ID field for updates
                    txtId.setEditable(false);
                }
            }
        });
        
        // SOUTH panel - Buttons
        pnlSouth = new JPanel();
        pnlSouth.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create buttons with uniform size and style
        Dimension buttonSize = new Dimension(120, 35);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        
        btnAdd = new JButton("Thêm");
        btnAdd.setFont(buttonFont);
        btnAdd.setPreferredSize(buttonSize);
        
        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setFont(buttonFont);
        btnUpdate.setPreferredSize(buttonSize);
        
        btnDelete = new JButton("Xóa");
        btnDelete.setFont(buttonFont);
        btnDelete.setPreferredSize(buttonSize);
        
        btnClear = new JButton("Làm Mới");
        btnClear.setFont(buttonFont);
        btnClear.setPreferredSize(buttonSize);
        
        // Handle potential missing icons gracefully
        try {
            btnAdd.setIcon(new ImageIcon(getClass().getResource("/icons/add.png")));
            btnUpdate.setIcon(new ImageIcon(getClass().getResource("/icons/update.png")));
            btnDelete.setIcon(new ImageIcon(getClass().getResource("/icons/delete.png")));
            btnClear.setIcon(new ImageIcon(getClass().getResource("/icons/clear.png")));
        } catch (Exception ex) {
            // Icons not found, continue without them
        }
        
        // Add buttons to panel with spacing
        pnlSouth.add(btnAdd);
        pnlSouth.add(Box.createHorizontalStrut(10));
        pnlSouth.add(btnUpdate);
        pnlSouth.add(Box.createHorizontalStrut(10));
        pnlSouth.add(btnDelete);
        pnlSouth.add(Box.createHorizontalStrut(10));
        pnlSouth.add(btnClear);
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);
        
        // Add action listeners
        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);
        btnSearch.addActionListener(this);
    }
    
    private void loadSupplierData() {
        try {
            // Clear existing table data
            tableModel.setRowCount(0);
            
            // Get all suppliers
            List<NhaCungCap> suppliers = nhaCungCapService.findAll();
            
            // Add suppliers to table
            for (NhaCungCap supplier : suppliers) {
                Object[] row = {
                    supplier.getId(),
                    supplier.getTen(),
                    supplier.getDiaChi(),
                    supplier.getSdt()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading supplier data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void searchSuppliers() {
        try {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadSupplierData();
                return;
            }
            
            // Clear existing table data
            tableModel.setRowCount(0);
            
            // Search suppliers
            List<NhaCungCap> suppliers = nhaCungCapService.searchSuppliers(keyword);
            
            if (suppliers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhà cung cấp nào phù hợp");
                return;
            }
            
            // Add suppliers to table
            for (NhaCungCap supplier : suppliers) {
                Object[] row = {
                    supplier.getId(),
                    supplier.getTen(),
                    supplier.getDiaChi(),
                    supplier.getSdt()
                };
                tableModel.addRow(row);
            }
            
            // Select first row
            if (tblSuppliers.getRowCount() > 0) {
                tblSuppliers.setRowSelectionInterval(0, 0);
                
                // Update form with selected supplier
                int selectedRow = 0;
                String id = tblSuppliers.getValueAt(selectedRow, 0).toString();
                String name = tblSuppliers.getValueAt(selectedRow, 1).toString();
                String address = tblSuppliers.getValueAt(selectedRow, 2) != null ? 
                                tblSuppliers.getValueAt(selectedRow, 2).toString() : "";
                String phone = tblSuppliers.getValueAt(selectedRow, 3) != null ? 
                              tblSuppliers.getValueAt(selectedRow, 3).toString() : "";
                
                // Set values to text fields
                txtId.setText(id);
                txtName.setText(name);
                txtAddress.setText(address);
                txtPhone.setText(phone);
                
                // Disable ID field for updates
                txtId.setEditable(false);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        // Clear input fields
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        
        // Make ID field editable again
        txtId.setEditable(true);
        
        // Clear search field but keep the table data
        txtSearch.setText("");
        
        // Clear table selection
        tblSuppliers.clearSelection();
        
        // Return focus to ID field
        txtId.requestFocus();
        
        // Reload all supplier data
        loadSupplierData();
    }
    
    private boolean validateForm() {
        // Check ID
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhà cung cấp không được để trống");
            txtId.requestFocus();
            return false;
        }
        
        // Check Name
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống");
            txtName.requestFocus();
            return false;
        }
        
        // Validate Phone (if provided)
        String phone = txtPhone.getText().trim();
        if (!phone.isEmpty()) {
            Pattern pattern = Pattern.compile("\\d{10,11}");
            if (!pattern.matcher(phone).matches()) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải từ 10-11 chữ số");
                txtPhone.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private void addSupplier() {
        if (!validateForm()) {
            return;
        }
        
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String address = txtAddress.getText().trim();
            String phone = txtPhone.getText().trim();
            
            // Check if supplier ID already exists
            NhaCungCap existingSupplier = nhaCungCapService.findById(id);
            if (existingSupplier != null) {
                JOptionPane.showMessageDialog(this, "Mã nhà cung cấp đã tồn tại");
                txtId.requestFocus();
                return;
            }
            
            // Create new supplier
            NhaCungCap supplier = new NhaCungCap();
            supplier.setId(id);
            supplier.setTen(name);
            supplier.setDiaChi(address);
            supplier.setSdt(phone);
            
            // Save supplier
            boolean success = nhaCungCapService.save(supplier);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công");
                clearForm();
                loadSupplierData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateSupplier() {
        if (!validateForm()) {
            return;
        }
        
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String address = txtAddress.getText().trim();
            String phone = txtPhone.getText().trim();
            
            // Check if supplier exists
            NhaCungCap existingSupplier = nhaCungCapService.findById(id);
            if (existingSupplier == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhà cung cấp");
                return;
            }
            
            // Update supplier
            existingSupplier.setTen(name);
            existingSupplier.setDiaChi(address);
            existingSupplier.setSdt(phone);
            
            // Save changes
            boolean success = nhaCungCapService.update(existingSupplier);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thành công");
                clearForm();
                loadSupplierData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteSupplier() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để xóa");
            return;
        }
        
        try {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa nhà cung cấp này?", 
                    "Xác Nhận Xóa", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = nhaCungCapService.delete(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công");
                    clearForm();
                    loadSupplierData();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa nhà cung cấp. Có thể đang được sử dụng bởi các dữ liệu khác.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addSupplier();
        } else if (e.getSource() == btnUpdate) {
            updateSupplier();
        } else if (e.getSource() == btnDelete) {
            deleteSupplier();
        } else if (e.getSource() == btnClear) {
            clearForm();
        } else if (e.getSource() == btnSearch) {
            searchSuppliers();
        }
    }
}
