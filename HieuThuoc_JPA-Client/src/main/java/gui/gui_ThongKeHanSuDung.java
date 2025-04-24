package gui;

import entity.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import service.ThuocService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class gui_ThongKeHanSuDung extends JPanel {

    private JTable tableExpired, tableNearlyExpired;
    private DefaultTableModel modelExpired, modelNearlyExpired;
    private JLabel lblTotalExpired, lblTotalNearlyExpired;
    private JSpinner spinnerDaysThreshold;
    private JButton btnRefresh, btnExport, btnBack;
    private ThuocService THUOC_SERVICE;
    private static TaiKhoan loginNV = null;
    private ChartPanel pieChartPanel;

    // Define colors and fonts for styling
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color TITLE_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Color WARNING_COLOR = new Color(255, 99, 71);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font TITLE_FONT = new Font("Times New Roman", Font.BOLD, 28);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thống Kê Thuốc Hết Hạn");
            gui_ThongKeHanSuDung panel = new gui_ThongKeHanSuDung(loginNV);
            frame.setContentPane(panel);
            frame.setSize(1400, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public gui_ThongKeHanSuDung(TaiKhoan login) {
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

        // Main content panel with statistics and tables
        JPanel pnMain = new JPanel(new BorderLayout(10, 10));
        pnMain.setBackground(BACKGROUND_COLOR);

        // Dashboard panel with statistics
        JPanel pnDashboard = createDashboardPanel();
        pnMain.add(pnDashboard, BorderLayout.NORTH);

        // Split pane for expired and nearly expired tables
        JSplitPane splitPaneTables = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneTables.setDividerLocation(680);
        splitPaneTables.setBackground(BACKGROUND_COLOR);

        // Expired medicines table
        JScrollPane scrollExpired = createExpiredTablePanel();
        splitPaneTables.setLeftComponent(scrollExpired);

        // Nearly expired medicines table
        JScrollPane scrollNearlyExpired = createNearlyExpiredTablePanel();
        splitPaneTables.setRightComponent(scrollNearlyExpired);

        pnMain.add(splitPaneTables, BorderLayout.CENTER);
        add(pnMain, BorderLayout.CENTER);

        // Button Panel
        JPanel pnButtons = createButtonPanel();
        add(pnButtons, BorderLayout.SOUTH);

        loadData();
        attachButtonListeners();
        loginNV = login;
    }

    private JPanel createTitlePanel() {
        JPanel pnTitle = new JPanel();
        pnTitle.setBackground(BACKGROUND_COLOR);
        JLabel lblTitle = new JLabel("THỐNG KÊ THUỐC HẾT HẠN SỬ DỤNG", JLabel.CENTER);
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(TITLE_COLOR);
        pnTitle.add(lblTitle);
        pnTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return pnTitle;
    }

    private JPanel createDashboardPanel() {
        JPanel pnDashboard = new JPanel(new BorderLayout(20, 10));
        pnDashboard.setBackground(BACKGROUND_COLOR);
        pnDashboard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Stats panel (left side)
        JPanel pnStats = new JPanel(new GridLayout(3, 1, 5, 15));
        pnStats.setBackground(BACKGROUND_COLOR);
        pnStats.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thống Kê Tổng Quan",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                HEADER_FONT,
                TITLE_COLOR));

        // Total expired medicines
        JPanel pnExpired = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnExpired.setBackground(BACKGROUND_COLOR);
        JLabel lblExpiredTitle = new JLabel("Tổng Số Thuốc Đã Hết Hạn: ");
        lblExpiredTitle.setFont(LABEL_FONT);
        lblTotalExpired = new JLabel("0");
        lblTotalExpired.setFont(HEADER_FONT);
        lblTotalExpired.setForeground(WARNING_COLOR);
        pnExpired.add(lblExpiredTitle);
        pnExpired.add(lblTotalExpired);
        pnStats.add(pnExpired);

        // Days threshold panel
        JPanel pnThreshold = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnThreshold.setBackground(BACKGROUND_COLOR);
        JLabel lblThreshold = new JLabel("Số Ngày Cảnh Báo Sắp Hết Hạn: ");
        lblThreshold.setFont(LABEL_FONT);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(30, 1, 365, 1);
        spinnerDaysThreshold = new JSpinner(spinnerModel);
        spinnerDaysThreshold.setPreferredSize(new Dimension(70, 25));
        spinnerDaysThreshold.addChangeListener(e -> loadData());
        pnThreshold.add(lblThreshold);
        pnThreshold.add(spinnerDaysThreshold);
        pnStats.add(pnThreshold);

        // Total nearly expired medicines
        JPanel pnNearlyExpired = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnNearlyExpired.setBackground(BACKGROUND_COLOR);
        JLabel lblNearlyExpiredTitle = new JLabel("Tổng Số Thuốc Sắp Hết Hạn: ");
        lblNearlyExpiredTitle.setFont(LABEL_FONT);
        lblTotalNearlyExpired = new JLabel("0");
        lblTotalNearlyExpired.setFont(HEADER_FONT);
        lblTotalNearlyExpired.setForeground(new Color(255, 165, 0)); // Orange
        pnNearlyExpired.add(lblNearlyExpiredTitle);
        pnNearlyExpired.add(lblTotalNearlyExpired);
        pnStats.add(pnNearlyExpired);

        // Pie chart panel (right side)
        pieChartPanel = createPieChartPanel();
        pieChartPanel.setPreferredSize(new Dimension(400, 200));

        // Combine stats and chart
        pnDashboard.add(pnStats, BorderLayout.WEST);
        pnDashboard.add(pieChartPanel, BorderLayout.EAST);

        return pnDashboard;
    }

    private ChartPanel createPieChartPanel() {
        // Default empty dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Đã Hết Hạn", 0);
        dataset.setValue("Sắp Hết Hạn", 0);
        dataset.setValue("Còn Hạn", 100);

        JFreeChart chart = ChartFactory.createPieChart(
                "Tỷ Lệ Thuốc Theo Hạn Sử Dụng",
                dataset,
                true, // Show legend
                true, // Generate tooltips
                false // Generate URLs
        );

        // Customize pie chart appearance
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(BACKGROUND_COLOR);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setSectionPaint("Đã Hết Hạn", WARNING_COLOR);
        plot.setSectionPaint("Sắp Hết Hạn", new Color(255, 165, 0)); // Orange
        plot.setSectionPaint("Còn Hạn", new Color(60, 179, 113)); // Medium sea green

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return chartPanel;
    }

    private JScrollPane createExpiredTablePanel() {
        String[] columnNames = {"Mã Thuốc", "Tên Thuốc", "Hạn Sử Dụng", "Danh Mục", "Số Lượng Tồn", "Đơn Giá"};
        modelExpired = new DefaultTableModel(columnNames, 0);
        tableExpired = new JTable(modelExpired) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableExpired.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableExpired.setRowHeight(25);
        tableExpired.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tableExpired.getTableHeader().setReorderingAllowed(false);
        tableExpired.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(tableExpired);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Danh Sách Thuốc Đã Hết Hạn",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                HEADER_FONT, WARNING_COLOR));

        return scrollPane;
    }

    private JScrollPane createNearlyExpiredTablePanel() {
        String[] columnNames = {"Mã Thuốc", "Tên Thuốc", "Hạn Sử Dụng", "Danh Mục", "Số Ngày Còn Lại", "Số Lượng Tồn"};
        modelNearlyExpired = new DefaultTableModel(columnNames, 0);
        tableNearlyExpired = new JTable(modelNearlyExpired) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableNearlyExpired.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNearlyExpired.setRowHeight(25);
        tableNearlyExpired.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tableNearlyExpired.getTableHeader().setReorderingAllowed(false);
        tableNearlyExpired.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(tableNearlyExpired);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Danh Sách Thuốc Sắp Hết Hạn",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                HEADER_FONT, new Color(255, 165, 0))); // Orange

        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnButtons.setBackground(BACKGROUND_COLOR);

        btnRefresh = createButton("Cập Nhật Dữ Liệu");
        btnExport = createButton("Xuất Báo Cáo");
        btnBack = createButton("Quay Lại");

        pnButtons.add(btnRefresh);
        pnButtons.add(btnExport);
        pnButtons.add(btnBack);

        pnButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return pnButtons;
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
        btnRefresh.addActionListener(e -> loadData());
        btnExport.addActionListener(e -> exportReport());
        btnBack.addActionListener(e -> {
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

    private void loadData() {
        try {
            int daysThreshold = (Integer) spinnerDaysThreshold.getValue();

            // Load expired medicines
            List<Thuoc> expiredMedicines = new ArrayList<>();
            try {
                expiredMedicines = THUOC_SERVICE.findExpiredMedicines();
            } catch (RemoteException e) {
                System.err.println("Error calling findExpiredMedicines: " + e.getMessage());
                // Try to get all medicines and filter locally as fallback
                try {
                    Date currentDate = new Date();
                    expiredMedicines = THUOC_SERVICE.findAll().stream()
                            .filter(thuoc -> thuoc.getHanSuDung() != null && thuoc.getHanSuDung().before(currentDate))
                            .collect(Collectors.toList());
                } catch (Exception ex) {
                    System.err.println("Fallback method also failed: " + ex.getMessage());
                }
            }
            updateExpiredTable(expiredMedicines);

            // Load nearly expired medicines
            List<Thuoc> nearlyExpiredMedicines = new ArrayList<>();
            try {
                nearlyExpiredMedicines = THUOC_SERVICE.findNearlyExpiredMedicines(daysThreshold);
            } catch (RemoteException e) {
                System.err.println("Error calling findNearlyExpiredMedicines: " + e.getMessage());
                // Try to get all medicines and filter locally as fallback
                try {
                    Date currentDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DAY_OF_MONTH, daysThreshold);
                    Date thresholdDate = calendar.getTime();

                    nearlyExpiredMedicines = THUOC_SERVICE.findAll().stream()
                            .filter(thuoc -> {
                                if (thuoc.getHanSuDung() == null) {
                                    return false;
                                }
                                return thuoc.getHanSuDung().after(currentDate) &&
                                        thuoc.getHanSuDung().before(thresholdDate);
                            })
                            .collect(Collectors.toList());
                } catch (Exception ex) {
                    System.err.println("Fallback method also failed: " + ex.getMessage());
                }
            }
            updateNearlyExpiredTable(nearlyExpiredMedicines, daysThreshold);

            // Update stats
            int totalExpired = expiredMedicines.size();
            int totalNearlyExpired = nearlyExpiredMedicines.size();
            lblTotalExpired.setText(String.valueOf(totalExpired));
            lblTotalNearlyExpired.setText(String.valueOf(totalNearlyExpired));

            // Update pie chart
            updatePieChart(totalExpired, totalNearlyExpired);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateExpiredTable(List<Thuoc> expiredMedicines) {
        modelExpired.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Thuoc thuoc : expiredMedicines) {
            String categoryName = thuoc.getDanhMuc() != null ?
                    (thuoc.getDanhMuc().getTen() != null ? thuoc.getDanhMuc().getTen() : thuoc.getDanhMuc().getId()) :
                    "N/A";

            modelExpired.addRow(new Object[]{
                    thuoc.getId(),
                    thuoc.getTen(),
                    dateFormat.format(thuoc.getHanSuDung()),
                    categoryName,
                    thuoc.getSoLuongTon(),
                    thuoc.getDonGia()
            });
        }
    }

    private void updateNearlyExpiredTable(List<Thuoc> nearlyExpiredMedicines, int daysThreshold) {
        modelNearlyExpired.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();

        for (Thuoc thuoc : nearlyExpiredMedicines) {
            String categoryName = thuoc.getDanhMuc() != null ?
                    (thuoc.getDanhMuc().getTen() != null ? thuoc.getDanhMuc().getTen() : thuoc.getDanhMuc().getId()) :
                    "N/A";

            // Calculate days remaining
            long daysRemaining = (thuoc.getHanSuDung().getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24);

            modelNearlyExpired.addRow(new Object[]{
                    thuoc.getId(),
                    thuoc.getTen(),
                    dateFormat.format(thuoc.getHanSuDung()),
                    categoryName,
                    daysRemaining,
                    thuoc.getSoLuongTon()
            });
        }
    }

    private void updatePieChart(int totalExpired, int totalNearlyExpired) {
        try {
            // Get total count of medicines
            int totalMedicines = 0;
            try {
                totalMedicines = THUOC_SERVICE.findAll().size();
            } catch (RemoteException e) {
                System.err.println("Error getting total medicines: " + e.getMessage());
                // Use a reasonable default
                totalMedicines = Math.max(100, totalExpired + totalNearlyExpired + 10);
            }

            int regularMedicines = totalMedicines - totalExpired - totalNearlyExpired;

            // Calculate percentages (avoid division by zero)
            double expiredPercentage = totalMedicines > 0 ? (double) totalExpired / totalMedicines * 100 : 0;
            double nearlyExpiredPercentage = totalMedicines > 0 ? (double) totalNearlyExpired / totalMedicines * 100 : 0;
            double regularPercentage = totalMedicines > 0 ? (double) regularMedicines / totalMedicines * 100 : 0;

            // Update dataset
            DefaultPieDataset dataset = new DefaultPieDataset();
            if (totalExpired > 0) {
                dataset.setValue("Đã Hết Hạn (" + totalExpired + ")", expiredPercentage);
            }
            if (totalNearlyExpired > 0) {
                dataset.setValue("Sắp Hết Hạn (" + totalNearlyExpired + ")", nearlyExpiredPercentage);
            }
            if (regularMedicines > 0) {
                dataset.setValue("Còn Hạn (" + regularMedicines + ")", regularPercentage);
            }

            // Create new chart
            JFreeChart chart = ChartFactory.createPieChart(
                    "Tỷ Lệ Thuốc Theo Hạn Sử Dụng",
                    dataset,
                    true,
                    true,
                    false
            );

            // Customize chart appearance
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setBackgroundPaint(BACKGROUND_COLOR);
            plot.setOutlineVisible(false);
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setSectionPaint("Đã Hết Hạn (" + totalExpired + ")", WARNING_COLOR);
            plot.setSectionPaint("Sắp Hết Hạn (" + totalNearlyExpired + ")", new Color(255, 165, 0));
            plot.setSectionPaint("Còn Hạn (" + regularMedicines + ")", new Color(60, 179, 113));

            // Update chart panel
            pieChartPanel.setChart(chart);
            pieChartPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportReport() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu Báo Cáo");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new File("BaoCaoThuocHetHan.txt"));

            int result = fileChooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile = fileChooser.getSelectedFile();
            StringBuilder report = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            report.append("BÁO CÁO THUỐC HẾT HẠN VÀ SẮP HẾT HẠN\n");
            report.append("Ngày tạo báo cáo: ").append(dateFormat.format(new Date())).append("\n\n");

            // Add summary
            report.append("TỔNG QUAN:\n");
            report.append("- Tổng số thuốc đã hết hạn: ").append(lblTotalExpired.getText()).append("\n");
            report.append("- Số ngày cảnh báo sắp hết hạn: ").append(spinnerDaysThreshold.getValue()).append("\n");
            report.append("- Tổng số thuốc sắp hết hạn: ").append(lblTotalNearlyExpired.getText()).append("\n\n");

            // Add expired medicines
            report.append("DANH SÁCH THUỐC ĐÃ HẾT HẠN:\n");
            report.append(formatTableForReport(tableExpired)).append("\n\n");

            // Add nearly expired medicines
            report.append("DANH SÁCH THUỐC SẮP HẾT HẠN:\n");
            report.append(formatTableForReport(tableNearlyExpired)).append("\n\n");

            // Get category statistics
            Map<String, Integer> expiredByCategory = new HashMap<>();
            Map<String, Integer> nearlyExpiredByCategory = new HashMap<>();

            try {
                expiredByCategory = THUOC_SERVICE.getExpiredMedicinesByCategory();
            } catch (RemoteException e) {
                System.err.println("Error calling getExpiredMedicinesByCategory: " + e.getMessage());
                // Calculate locally as fallback
                int rowCount = tableExpired.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    String category = tableExpired.getValueAt(i, 3).toString();
                    expiredByCategory.put(category, expiredByCategory.getOrDefault(category, 0) + 1);
                }
            }

            try {
                nearlyExpiredByCategory = THUOC_SERVICE.getNearlyExpiredMedicinesByCategory((Integer) spinnerDaysThreshold.getValue());
            } catch (RemoteException e) {
                System.err.println("Error calling getNearlyExpiredMedicinesByCategory: " + e.getMessage());
                // Calculate locally as fallback
                int rowCount = tableNearlyExpired.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    String category = tableNearlyExpired.getValueAt(i, 3).toString();
                    nearlyExpiredByCategory.put(category, nearlyExpiredByCategory.getOrDefault(category, 0) + 1);
                }
            }

            // Add category statistics
            report.append("THỐNG KÊ THEO DANH MỤC:\n");
            report.append("Danh mục thuốc đã hết hạn:\n");
            for (Map.Entry<String, Integer> entry : expiredByCategory.entrySet()) {
                report.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            report.append("\nDanh mục thuốc sắp hết hạn:\n");
            for (Map.Entry<String, Integer> entry : nearlyExpiredByCategory.entrySet()) {
                report.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            // Write report to file
            java.io.FileWriter writer = new java.io.FileWriter(selectedFile);
            writer.write(report.toString());
            writer.close();

            JOptionPane.showMessageDialog(this, "Báo cáo đã được lưu thành công tại:\n" + selectedFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatTableForReport(JTable table) {
        StringBuilder tableStr = new StringBuilder();

        // Get column names
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            tableStr.append(table.getColumnName(i));
            if (i < columnCount - 1) {
                tableStr.append(" | ");
            }
        }
        tableStr.append("\n");

        // Separator line
        for (int i = 0; i < columnCount; i++) {
            int colWidth = table.getColumnName(i).length();
            tableStr.append("-".repeat(colWidth));
            if (i < columnCount - 1) {
                tableStr.append("-|-");
            }
        }
        tableStr.append("\n");

        // Table data
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < columnCount; col++) {
                Object value = table.getValueAt(row, col);
                tableStr.append(value != null ? value.toString() : "");
                if (col < columnCount - 1) {
                    tableStr.append(" | ");
                }
            }
            tableStr.append("\n");
        }

        return tableStr.toString();
    }
} 