package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dao.impl.TaiKhoanDAOImpl;
import entity.TaiKhoan;
import other.MessageDialog;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

import service.TaiKhoanService;
import service.impl.TaiKhoanServiceImpl;

public class gui_TrangChu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane, homePanel;
    private TaiKhoan loginAccount;
    private TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(new TaiKhoanDAOImpl());

    private static final Color PRIMARY_COLOR = new Color(31, 97, 141);// Dark blue
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240);// Light gray
    private static final Font MENU_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font MENU_ITEM_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private JMenuBar menuBar;

    public gui_TrangChu(TaiKhoan login) throws RemoteException {
        this.loginAccount = login;
        initializeUI();
        setupContentPane();
        setupMenuBar();
        applyAccessControl();
    }

    private void initializeUI() {
        setTitle("Hệ Thống Quản Lý Nhà Thuốc Yên Tâm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon/QuanLyNhaThuoc.png")));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupContentPane() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(0, 0, 0, 0));
        contentPane.setBorder(null);
        homePanel = createSimpleBackgroundPanel();
        setContentPane(contentPane);
        setPanel(homePanel);
    }

    private JPanel createSimpleBackgroundPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon/Nha thuoc.png"));
                Image img = imageIcon.getImage();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                g.drawImage(img, 0, -110, screenSize.width, screenSize.height, this);
            }
        };
        panel.setLayout(null);
        return panel;
    }

    private void setupMenuBar() throws RemoteException {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width, 60));

        menuBar.setBackground(new Color(0, 87, 183));
        menuBar.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        menuBar.setOpaque(true);

        JLabel logoLabel = new JLabel("YÊN TÂM");
        logoLabel.setForeground(Color.ORANGE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 15));
        menuBar.add(logoLabel);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 30));
        separator.setForeground(Color.WHITE);
        menuBar.add(separator);
        menuBar.add(Box.createHorizontalStrut(10));

        addSystemMenu();
        addCatalogMenu();
        addTransactionMenu();
        addSearchMenu();
        addStatisticsMenu();
        addUserProfile();

        setJMenuBar(menuBar);
    }

    private void addSystemMenu() {
        JMenu menu = createMenu("Hệ Thống", "/icon/control-system.png");

        addMenuItem(menu, "Trang Chủ", "/icon/home.png", e -> setPanel(homePanel));
        menu.addSeparator();

        addMenuItem(menu, "Phân quyền tài khoản", "/icon/decentralized.png", e -> {
            try {
                setPanel(new gui_taiKhoan());
            } catch (RemoteException ex) {
                handleError("Lỗi khi mở phân quyền tài khoản", ex);
            }
        }, loginAccount.getVaiTro().getId().equals("admin"));

        addMenuItem(menu, "Quản lý vai trò", "/icon/user.png", e -> {
            try {
                setPanel(new gui_qliVaiTro());
            } catch (RemoteException ex) {
                handleError("Lỗi khi mở quản lý vai trò", ex);
            }
        }, loginAccount.getVaiTro().getId().equals("admin"));

        addMenuItem(menu, "Hướng dẫn sử dụng", "/icon/symbols.png", null);
        menu.addSeparator();

        addMenuItem(menu, "Đăng xuất", "/icon/logout.png", this::handleLogout);

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalStrut(10));
    }

    private void addCatalogMenu() throws RemoteException {
        JMenu menu = createMenu("Danh Mục", "/icon/classified-document.png");

        addMenuItem(menu, "Khách hàng", "/icon/client.png", e -> setPanel(new gui_qliKhachHang()));
        addMenuItem(menu, "Thuốc", "/icon/medicine.png", e -> setPanel(new gui_qliThuoc()));
        addMenuItem(menu, "Nhà cung cấp", "/icon/supplier.png", e -> setPanel(new gui_qliNCC()));
        addMenuItem(menu, "Nhân viên", "/icon/employees.png", e -> {
            try {
                setPanel(new gui_nhanVien());
            } catch (RemoteException ex) {
                handleError("Lỗi khi mở quản lý nhân viên", ex);
            }
        }, loginAccount.getVaiTro().getId().equals("admin"));
        addMenuItem(menu, "Nhà sản xuất", "/icon/decentralized.png", e -> {
            try {
                setPanel(new gui_qliNhaSX());
            } catch (RemoteException ex) {
                handleError("Lỗi khi mở quản lý nhà sản xuất", ex);
            }
        });
        addMenuItem(menu, "Danh mục thuốc", "/icon/scheme.png", e -> {
            try {
                setPanel(new gui_qliDanhMuc());
            } catch (RemoteException ex) {
                handleError("Lỗi khi mở quản lý danh mục thuốc", ex);
            }
        });
        addMenuItem(menu, "Khuyến mãi", "/icon/tag.png", e -> {
            // Add implementation when available
        });

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalStrut(10));
    }

    private void addTransactionMenu() {
        JMenu menu = createMenu("Xử Lý", "/icon/profit.png");
        addMenuItem(menu, "Bán thuốc", "/icon/medicine.png", e -> setPanel(new gui_BanThuoc(loginAccount)));
        addMenuItem(menu, "Trả thuốc", "/icon/bill.png", e -> setPanel(new gui_TraThuoc(loginAccount)));
        addMenuItem(menu, "Đặt thuốc", "/icon/fulfillment.png", e -> {
        });
        addMenuItem(menu, "Nhập thuốc", "/icon/import.png", e -> setPanel(new gui_nhapThuoc(loginAccount)));
        addMenuItem(menu, "Tạo khuyến mãi", "/icon/tag.png", e -> {
            // Add implementation when available
        });

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalStrut(10));
    }

    private void addSearchMenu() {
        JMenu menu = createMenu("Tìm Kiếm", "/icon/search.png");

        addMenuItem(menu, "Khách hàng", "/icon/client.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Thuốc", "/icon/medicine.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Hóa đơn", "/icon/bill.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Phiếu đặt", "/icon/fulfillment.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Phiếu nhập", "/icon/import.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Nhà cung cấp", "/icon/supplier.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Khuyến mãi", "/icon/tag.png", e -> {
            // Add implementation when available
        });

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalStrut(10));
    }

    private void addStatisticsMenu() {
        JMenu menu = createMenu("Thống Kê", "/icon/bar-graph.png");

        addMenuItem(menu, "Khách hàng thân thiết", "/icon/reputation.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Doanh thu", "/icon/revenue.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Thuốc cận hạn", "/icon/expired.png", e -> {
            // Add implementation when available
        });
        addMenuItem(menu, "Nhà cung cấp uy tín", "/icon/supplier (1).png", e -> {
            // Add implementation when available
        });

        menuBar.add(menu);
        menuBar.add(Box.createHorizontalStrut(10));
    }

    private void addUserProfile() {
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(true);
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 15));

        JLabel lblUserName = new JLabel(loginAccount.getNhanVien().getHoTen());
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 15));

        lblUserName.setForeground(Color.BLACK);
        lblUserName.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblUserName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showUserProfileDialog();
            }
        });

        JLabel lblUserIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon/user.png"));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            lblUserIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load user icon");
        }

        lblUserIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblUserIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showUserProfileDialog();
            }
        });

        userPanel.add(lblUserName);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(lblUserIcon);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(userPanel);
    }

    private void showUserProfileDialog() {
        JDialog profileDialog = new JDialog(this, "Thông Tin Cá Nhân", true);
        profileDialog.setSize(600, 400);
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        profileDialog.add(new gui_thongtinlogin(loginAccount));
        profileDialog.setVisible(true);
    }

    private JMenu createMenu(String title, String iconPath) {
        JMenu menu = new JMenu(title);
        menu.setFont(MENU_FONT);
        menu.setForeground(Color.BLACK);
		menu.setOpaque(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            menu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load icon: " + iconPath);
        }

        return menu;
    }

    private void addMenuItem(JMenu menu, String text, String iconPath, ActionListener action) {
        addMenuItem(menu, text, iconPath, action, true);
    }

    private void addMenuItem(JMenu menu, String text, String iconPath, ActionListener action, boolean isActive) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(MENU_ITEM_FONT);
        item.setEnabled(isActive);


        item.setBackground(new Color(240, 248, 255)); // Light blue background
        item.setForeground(new Color(0, 51, 102)); // Dark blue text
        item.setOpaque(true);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(135, 206, 250)); // Sky blue when hovered
                item.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(240, 248, 255)); // Back to light blue
                item.setForeground(new Color(0, 51, 102)); // Back to dark blue text
            }
        });

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            item.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load icon: " + iconPath);
        }

        if (action != null) {
            item.addActionListener(action);
        }

        menu.add(item);
    }

    private void handleLogout(ActionEvent e) {
        if (MessageDialog.confirm(this, "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất")) {
            dispose();
            Login loginFrame = new Login();
            loginFrame.getFrmlogin().setVisible(true);
        }
    }

    private void applyAccessControl() {
        try {
            if (menuBar == null || menuBar.getMenuCount() == 0) {
                System.err.println("Warning: Menu bar is not initialized yet or has no menus");
                return;
            }

            boolean isAdmin = "admin".equals(loginAccount.getVaiTro().getId());

            // Find the System menu items and disable based on permissions
            JMenu systemMenu = menuBar.getMenu(0);
            if (systemMenu != null) {
                for (int i = 0; i < systemMenu.getItemCount(); i++) {
                    JMenuItem item = systemMenu.getItem(i);
                    if (item != null) {
                        String text = item.getText();
                        if (text != null && (text.equals("Phân quyền tài khoản") || text.equals("Quản lý vai trò"))) {
                            item.setEnabled(isAdmin);
                        }
                    }
                }
            }

            // Find the Catalog menu and disable employee management if not admin
            if (menuBar.getMenuCount() > 1) {
                JMenu catalogMenu = menuBar.getMenu(1);
                if (catalogMenu != null) {
                    for (int i = 0; i < catalogMenu.getItemCount(); i++) {
                        JMenuItem item = catalogMenu.getItem(i);
                        if (item != null && "Nhân viên".equals(item.getText())) {
                            item.setEnabled(isAdmin);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error applying access control: " + e.getMessage());
        }
    }

    public void setPanel(JPanel panel) {
        if (panel == null) return;
        
        contentPane.removeAll();
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void handleError(String message, Exception ex) {
        MessageDialog.error(this, message + ": " + ex.getMessage());
        ex.printStackTrace();
    }
}
