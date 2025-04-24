package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.impl.ThuocDAOImpl;
import entity.*;
import other.RandomMa;
import service.ThuocService;
import service.impl.ThuocServiceImpl;

public class gui_qliThuoc extends JPanel implements MouseListener {

    private JTextField txtMaThuoc, txtTenThuoc, txtDonViTinh, txtThanhPhan, txtSoLuongTon, txtDonGia, txtHinhAnh, txtNhaSanXuat, txtDanhMuc, txtKhuyenMai;
    private JButton btnThem, btnXoa, btnSua, btnThoat, btnLamMoi, btnChonAnh, btnThongKeHetHan;
    private JDateChooser dateChooserHanSuDung;
    private JTable table;
    private DefaultTableModel model;
    private ThuocService THUOC_SERVICE;
    private static TaiKhoan loginNV = null;
    private JLabel lblHinhAnhPreview; // Added for image preview

    // Define colors and fonts for styling (You can adjust these)
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color TITLE_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Thuốc");
            gui_qliThuoc panel = new gui_qliThuoc(loginNV);
            frame.setContentPane(panel);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public gui_qliThuoc(TaiKhoan login) {
        try {
            Registry registry = LocateRegistry.getRegistry(8989);
            THUOC_SERVICE = (ThuocService) registry.lookup("ThuocService");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo service: " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

        this.setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel pnTitle = createTitlePanel();
        add(pnTitle, BorderLayout.NORTH);

        // Center Panel with info and image panel side by side
        JPanel pnCenter = new JPanel(new BorderLayout(10, 0));
        pnCenter.setBackground(BACKGROUND_COLOR);

        // Info Panel
        JPanel pnInfo = createInfoPanel();
        pnInfo.setPreferredSize(new Dimension(400, 350));

        // Image Preview Panel
        JPanel pnImagePreview = createImagePreviewPanel();
        pnImagePreview.setPreferredSize(new Dimension(200, 350));

        // Combine info and image preview horizontally
        JPanel pnInfoAndImage = new JPanel(new BorderLayout(10, 0));
        pnInfoAndImage.setBackground(BACKGROUND_COLOR);
        pnInfoAndImage.add(pnInfo, BorderLayout.CENTER);
        pnInfoAndImage.add(pnImagePreview, BorderLayout.EAST);

        pnCenter.add(pnInfoAndImage, BorderLayout.NORTH);

        // Button Panel
        JPanel pnButtons = createButtonPanel();
        pnCenter.add(pnButtons, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.EAST);

        // Table Panel
        JScrollPane scrollPane = createTablePanel();
        scrollPane.setPreferredSize(new Dimension(700, 500));
        add(scrollPane, BorderLayout.CENTER);

        docDuLieuDatabaseVaoTable();
        table.addMouseListener(this);
        attachButtonListeners();

        generateRandomThuocId(); // Generate initial ID
        loginNV = login;
    }

    private JPanel createImagePreviewPanel() {
        JPanel pnImagePreview = new JPanel(new BorderLayout(5, 10));
        pnImagePreview.setBackground(BACKGROUND_COLOR);
        pnImagePreview.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Xem Trước Hình Ảnh",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                LABEL_FONT, TITLE_COLOR));

        // Create image preview label
        lblHinhAnhPreview = new JLabel();
        lblHinhAnhPreview.setHorizontalAlignment(JLabel.CENTER);
        lblHinhAnhPreview.setPreferredSize(new Dimension(180, 180));
        lblHinhAnhPreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblHinhAnhPreview.setBackground(Color.WHITE);
        lblHinhAnhPreview.setOpaque(true);

        // Default image
        ImageIcon defaultIcon = new ImageIcon(new ImageIcon(getClass().getResource("/product-image/default-drug.png"))
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        lblHinhAnhPreview.setIcon(defaultIcon);

        // Button to choose image
        btnChonAnh = createButton("Chọn Ảnh");
        btnChonAnh.setPreferredSize(new Dimension(120, 30));
        btnChonAnh.addActionListener(e -> chonAnhThuoc());

        JPanel pnBtnChonAnh = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnBtnChonAnh.setBackground(BACKGROUND_COLOR);
        pnBtnChonAnh.add(btnChonAnh);

        pnImagePreview.add(lblHinhAnhPreview, BorderLayout.CENTER);
        pnImagePreview.add(pnBtnChonAnh, BorderLayout.SOUTH);

        return pnImagePreview;
    }

    private void chonAnhThuoc() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn Hình Ảnh Thuốc");

        // Set filter for image files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Hình ảnh (*.jpg, *.png, *.gif)", "jpg", "png", "gif");
        fileChooser.setFileFilter(filter);

        // Starting directory - you might want to set a specific path for product images
        fileChooser.setCurrentDirectory(new File("product-image"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getPath();

            // Update the text field with the image path
            txtHinhAnh.setText(selectedFile.getName());

            // Update the preview
            try {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblHinhAnhPreview.setIcon(new ImageIcon(image));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể tải hình ảnh: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Update the displaySelectedImage method to be called when loading data
    private void displaySelectedImage(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            // Set default image if no image path is provided
            ImageIcon defaultIcon = new ImageIcon(new ImageIcon(getClass().getResource("/product-image/default-drug.png"))
                    .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            lblHinhAnhPreview.setIcon(defaultIcon);
            return;
        }

        try {
            // First try to load from product-image folder in resources
            ImageIcon icon = new ImageIcon(getClass().getResource("/product-image/" + imageName));
            if (icon.getIconWidth() <= 0) {
                // If not found in resources, try direct file path
                File file = new File("product-image/" + imageName);
                if (file.exists()) {
                    icon = new ImageIcon(file.getAbsolutePath());
                } else {
                    // Try with full path if provided
                    file = new File(imageName);
                    if (file.exists()) {
                        icon = new ImageIcon(file.getAbsolutePath());
                    } else {
                        throw new Exception("Image not found");
                    }
                }
            }

            Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblHinhAnhPreview.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            // Set default image if error loading
            ImageIcon defaultIcon = new ImageIcon(new ImageIcon(getClass().getResource("/icon/no-image.png"))
                    .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            lblHinhAnhPreview.setIcon(defaultIcon);
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    private JPanel createTitlePanel() {
        JPanel pnTitle = new JPanel();
        pnTitle.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("QUẢN LÝ THUỐC", JLabel.CENTER);
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
                new LineBorder(Color.GRAY), "Thông Tin Thuốc",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                LABEL_FONT, TITLE_COLOR));

        GroupLayout layout = new GroupLayout(pnInfo);
        pnInfo.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Labels
        JLabel lblMaThuoc = createLabel("Mã Thuốc:");
        JLabel lblTenThuoc = createLabel("Tên Thuốc:");
        JLabel lblDonViTinh = createLabel("Đơn Vị Tính:");
        JLabel lblThanhPhan = createLabel("Thành Phần:");
        JLabel lblSoLuongTon = createLabel("Số Lượng Tồn:");
        JLabel lblDonGia = createLabel("Đơn Giá:");
        JLabel lblHanSuDung = createLabel("Hạn Sử Dụng:");
        JLabel lblHinhAnh = createLabel("Tên File Ảnh:");
        JLabel lblNhaSanXuat = createLabel("Nhà Sản Xuất:");
        JLabel lblDanhMuc = createLabel("Danh Mục:");
        JLabel lblKhuyenMai = createLabel("Khuyến Mãi:");

        // Input fields
        txtMaThuoc = new JTextField(10);
        txtMaThuoc.setEditable(false); // ID is auto-generated
        txtTenThuoc = new JTextField(20);
        txtDonViTinh = new JTextField(10);
        txtThanhPhan = new JTextField(20);
        txtSoLuongTon = new JTextField(10);
        txtDonGia = new JTextField(10);
        dateChooserHanSuDung = new JDateChooser();
        dateChooserHanSuDung.setDateFormatString("dd/MM/yyyy");
        txtHinhAnh = new JTextField(20);
        txtHinhAnh.setEditable(false); // Make it readonly, use button to set
        txtNhaSanXuat = new JTextField(20);
        txtDanhMuc = new JTextField(20);
        txtKhuyenMai = new JTextField(20);

        // Horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(lblMaThuoc)
                        .addComponent(lblTenThuoc)
                        .addComponent(lblDonViTinh)
                        .addComponent(lblThanhPhan)
                        .addComponent(lblSoLuongTon)
                        .addComponent(lblDonGia)
                        .addComponent(lblHanSuDung)
                        .addComponent(lblHinhAnh)
                        .addComponent(lblNhaSanXuat)
                        .addComponent(lblDanhMuc)
                        .addComponent(lblKhuyenMai))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtMaThuoc)
                        .addComponent(txtTenThuoc)
                        .addComponent(txtDonViTinh)
                        .addComponent(txtThanhPhan)
                        .addComponent(txtSoLuongTon)
                        .addComponent(txtDonGia)
                        .addComponent(dateChooserHanSuDung)
                        .addComponent(txtHinhAnh)
                        .addComponent(txtNhaSanXuat)
                        .addComponent(txtDanhMuc)
                        .addComponent(txtKhuyenMai))
        );

        // Vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMaThuoc)
                        .addComponent(txtMaThuoc))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTenThuoc)
                        .addComponent(txtTenThuoc))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDonViTinh)
                        .addComponent(txtDonViTinh))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblThanhPhan)
                        .addComponent(txtThanhPhan))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSoLuongTon)
                        .addComponent(txtSoLuongTon))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDonGia)
                        .addComponent(txtDonGia))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblHanSuDung)
                        .addComponent(dateChooserHanSuDung))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblHinhAnh)
                        .addComponent(txtHinhAnh))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNhaSanXuat)
                        .addComponent(txtNhaSanXuat))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDanhMuc)
                        .addComponent(txtDanhMuc))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblKhuyenMai)
                        .addComponent(txtKhuyenMai))
        );

        return pnInfo;
    }

    private JPanel createButtonPanel() {
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnButtons.setBackground(BACKGROUND_COLOR);

        btnThem = createButton("Thêm Thuốc");
        btnXoa = createButton("Xóa Thuốc");
        btnSua = createButton("Sửa Thuốc");
        btnLamMoi = createButton("Làm Mới");
        btnThongKeHetHan = createButton("Thống Kê Hết Hạn");
        btnThoat = createButton("Thoát");

        pnButtons.add(btnThem);
        pnButtons.add(btnXoa);
        pnButtons.add(btnSua);
        pnButtons.add(btnLamMoi);
        pnButtons.add(btnThongKeHetHan);
        pnButtons.add(btnThoat);

        pnButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return pnButtons;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Mã Thuốc", "Tên Thuốc", "Đơn Vị Tính", "Thành Phần", "Số Lượng Tồn", "Đơn Giá", "Hạn Sử Dụng", "Hình Ảnh", "Nhà Sản Xuất", "Danh Mục", "Khuyến Mãi"};
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
                new LineBorder(Color.GRAY), "Danh Sách Thuốc",
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
        btnThem.addActionListener(e -> themThuoc());
        btnXoa.addActionListener(e -> xoaThuoc());
        btnSua.addActionListener(e -> suaThuoc());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnThongKeHetHan.addActionListener(e -> moThongKeHetHan());
        btnThoat.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }

            gui_TrangChu mainFrame = null;
            try {
                mainFrame = new gui_TrangChu(loginNV);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            mainFrame.setVisible(true);
        });
    }

    private void themThuoc() {
        try {
            // Get values from form
            String maThuoc = txtMaThuoc.getText().trim();
            String tenThuoc = txtTenThuoc.getText().trim();
            String donViTinh = txtDonViTinh.getText().trim();
            String thanhPhan = txtThanhPhan.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();
            
            // Validate required fields
            if (maThuoc.isEmpty() || tenThuoc.isEmpty() || donViTinh.isEmpty() || thanhPhan.isEmpty() || 
                dateChooserHanSuDung.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin bắt buộc!");
                return;
            }
            
            // Parse numeric values
            double donGia = 0;
            int soLuongTon = 0;
            
            try {
                donGia = Double.parseDouble(txtDonGia.getText().trim());
                soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
                
                if (donGia < 0 || soLuongTon < 0) {
                    JOptionPane.showMessageDialog(null, "Giá trị không được âm!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Giá trị số không hợp lệ!");
                return;
            }
            
            // Parse date
            Date hanSuDungDate = dateChooserHanSuDung.getDate();
            java.sql.Date hanSuDung = new java.sql.Date(hanSuDungDate.getTime());
            
            // Get reference values for related entities
            String maNhaSX = txtNhaSanXuat.getText().trim();
            String maDanhMuc = txtDanhMuc.getText().trim();
            String maKhuyenMai = txtKhuyenMai.getText().trim();
            
            if (maNhaSX.isEmpty() || maDanhMuc.isEmpty() || maKhuyenMai.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập mã nhà sản xuất, danh mục và khuyến mãi!");
                return;
            }
            
            // Create Thuoc object
            Thuoc thuoc = new Thuoc();
            thuoc.setId(maThuoc);
            thuoc.setTen(tenThuoc);
            thuoc.setDonViTinh(donViTinh);
            thuoc.setThanhPhan(thanhPhan);
            thuoc.setSoLuongTon(soLuongTon);
            thuoc.setDonGia(donGia);
            thuoc.setHanSuDung(hanSuDung);
            thuoc.setHinhAnh(hinhAnh);
            
            // Set relationships using simple IDs without fetching the objects
            NhaSanXuat nsx = new NhaSanXuat(maNhaSX); // Use constructor with just ID
            DanhMuc dm = new DanhMuc(maDanhMuc, null, null); // Use constructor with ID only
            KhuyenMai km = new KhuyenMai();
            km.setId(maKhuyenMai);
            
            thuoc.setNhaSanXuat(nsx);
            thuoc.setDanhMuc(dm);
            thuoc.setKhuyenMai(km);
            
            // Save to database
            if (THUOC_SERVICE.save(thuoc)) {
                docDuLieuDatabaseVaoTable();
                JOptionPane.showMessageDialog(null, "Thêm thuốc thành công!");
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm thuốc thất bại!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm thuốc: " + e.getMessage());
        }
    }
    
    private void suaThuoc() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa");
                return;
            }

            String maThuoc = txtMaThuoc.getText().trim();
            String tenThuoc = txtTenThuoc.getText().trim();
            String donViTinh = txtDonViTinh.getText().trim();
            String thanhPhan = txtThanhPhan.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();
            
            // Validate required fields
            if (maThuoc.isEmpty() || tenThuoc.isEmpty() || donViTinh.isEmpty() || thanhPhan.isEmpty() || 
                dateChooserHanSuDung.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin bắt buộc!");
                return;
            }
            
            // Parse numeric values
            double donGia = 0;
            int soLuongTon = 0;
            
            try {
                donGia = Double.parseDouble(txtDonGia.getText().trim());
                soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
                
                if (donGia < 0 || soLuongTon < 0) {
                    JOptionPane.showMessageDialog(null, "Giá trị không được âm!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Giá trị số không hợp lệ!");
                return;
            }
            
            // Parse date
            Date hanSuDungDate = dateChooserHanSuDung.getDate();
            java.sql.Date hanSuDung = new java.sql.Date(hanSuDungDate.getTime());
            
            // Get reference values for related entities
            String maNhaSX = txtNhaSanXuat.getText().trim();
            String maDanhMuc = txtDanhMuc.getText().trim();
            String maKhuyenMai = txtKhuyenMai.getText().trim();
            
            if (maNhaSX.isEmpty() || maDanhMuc.isEmpty() || maKhuyenMai.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập mã nhà sản xuất, danh mục và khuyến mãi!");
                return;
            }
            
            // Create Thuoc object
            Thuoc thuoc = new Thuoc();
            thuoc.setId(maThuoc);
            thuoc.setTen(tenThuoc);
            thuoc.setDonViTinh(donViTinh);
            thuoc.setThanhPhan(thanhPhan);
            thuoc.setSoLuongTon(soLuongTon);
            thuoc.setDonGia(donGia);
            thuoc.setHanSuDung(hanSuDung);
            thuoc.setHinhAnh(hinhAnh);
            
            // Set relationships using simple IDs without fetching the objects
            NhaSanXuat nsx = new NhaSanXuat(maNhaSX); // Use constructor with just ID
            DanhMuc dm = new DanhMuc(maDanhMuc, null, null); // Use constructor with ID only
            KhuyenMai km = new KhuyenMai();
            km.setId(maKhuyenMai);
            
            thuoc.setNhaSanXuat(nsx);
            thuoc.setDanhMuc(dm);
            thuoc.setKhuyenMai(km);

            // Update database
            if (THUOC_SERVICE.update(thuoc)) {
                docDuLieuDatabaseVaoTable();
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi định dạng số (Số lượng tồn hoặc Đơn giá)!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + ex.getMessage());
        }
    }

    private void xoaThuoc() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn một dòng để xóa!");
                return;
            }

            String maThuoc = model.getValueAt(row, 0).toString();
            String tenThuoc = model.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn xóa thuốc '" + tenThuoc + "' không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (THUOC_SERVICE.delete(maThuoc)) {
                docDuLieuDatabaseVaoTable();
                lamMoiForm();
                JOptionPane.showMessageDialog(null, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Xóa thất bại!");
            }
        } catch (Exception ex) {
            //logger.error("Error deleting thuốc: ", ex);
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + ex.getMessage());
        }
    }

    private void lamMoiForm() {
        generateRandomThuocId();
        txtTenThuoc.setText("");
        txtDonViTinh.setText("");
        txtThanhPhan.setText("");
        txtSoLuongTon.setText("");
        txtDonGia.setText("");
        dateChooserHanSuDung.setDate(new Date());
        txtHinhAnh.setText("");
        txtNhaSanXuat.setText("");
        txtDanhMuc.setText("");
        txtKhuyenMai.setText("");
        table.clearSelection();
        txtTenThuoc.requestFocus();
        displaySelectedImage(null); // Reset image preview
    }

    private void generateRandomThuocId() {
        txtMaThuoc.setText(RandomMa.maThuocAuto());
    }

    private void docDuLieuDatabaseVaoTable() {
        try {
            model.setRowCount(0);
            List<Thuoc> listThuoc = THUOC_SERVICE.findAll();
            for (Thuoc thuoc : listThuoc) {
                model.addRow(new Object[]{
                        thuoc.getId(),
                        thuoc.getTen(),
                        thuoc.getDonViTinh(),
                        thuoc.getThanhPhan(),
                        thuoc.getSoLuongTon(),
                        thuoc.getDonGia(),
                        thuoc.getHanSuDung(),
                        thuoc.getHinhAnh(),
                        // You might need to display names instead of IDs for related entities
                        thuoc.getNhaSanXuat() != null ? thuoc.getNhaSanXuat().getId() : "",
                        thuoc.getDanhMuc() != null ? thuoc.getDanhMuc().getId() : "",
                        thuoc.getKhuyenMai() != null ? thuoc.getKhuyenMai().getId() : ""
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
            txtMaThuoc.setText(model.getValueAt(row, 0).toString());
            txtTenThuoc.setText(model.getValueAt(row, 1).toString());
            txtDonViTinh.setText(model.getValueAt(row, 2).toString());
            txtThanhPhan.setText(model.getValueAt(row, 3).toString());
            txtSoLuongTon.setText(model.getValueAt(row, 4).toString());
            txtDonGia.setText(model.getValueAt(row, 5).toString());
            Date hanSuDung = (Date) model.getValueAt(row, 6);
            dateChooserHanSuDung.setDate(hanSuDung);

            String imageName = model.getValueAt(row, 7).toString();
            txtHinhAnh.setText(imageName);
            displaySelectedImage(imageName); // Display the image when row is selected

            txtNhaSanXuat.setText(model.getValueAt(row, 8).toString());
            txtDanhMuc.setText(model.getValueAt(row, 9).toString());
            txtKhuyenMai.setText(model.getValueAt(row, 10).toString());
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

    private void moThongKeHetHan() {
        try {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }
            
            JFrame frame = new JFrame("Thống Kê Thuốc Hết Hạn");
            gui_ThongKeHanSuDung panel = new gui_ThongKeHanSuDung(loginNV);
            frame.setContentPane(panel);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở giao diện thống kê: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}