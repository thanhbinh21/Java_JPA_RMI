package gui;

import com.toedter.calendar.JDateChooser;
import entity.HoaDon;
import entity.NhanVien;
import entity.TaiKhoan;
import other.*;
import service.ChiTietHoaDonService;
import service.HoaDonService;
import service.NhanVienService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class gui_thongKeDoanhThu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField txt_sumHD;
	private JTextField txt_sumTra;
	private JTextField txt_SumDoanhThu;
	private DefaultTableModel modal;
	private List<HoaDon> listHoaDon;

	// RMI Services
	private HoaDonService hoaDonService;
	private ChiTietHoaDonService chiTietHoaDonService;
	private NhanVienService nhanVienService;

	private TableRowSorter<DefaultTableModel> sorter;
	JComboBox<String> comboBox = new JComboBox<>();
	JDateChooser txThoigianFrom, txThoigianTo;
	private TaiKhoan tk;
	
	/**
	 * Create the panel.
	 */
	public gui_thongKeDoanhThu(TaiKhoan login) {
		tk = login;
		initRMIServices();
		setSize(1400,800);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("THỐNG KÊ DOANH THU");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBackground(Color.RED);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 35));
		lblNewLabel.setBounds(529, 10, 440, 57);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Từ Ngày :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(39, 87, 77, 25);
		add(lblNewLabel_1);
		
		txThoigianFrom = new JDateChooser();
		txThoigianFrom.setBounds(136, 89, 148, 21);
		add(txThoigianFrom);
		
		JLabel lblNewLabel_1_1 = new JLabel("Đến Ngày :");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(38, 154, 98, 25);
		add(lblNewLabel_1_1);
		
		txThoigianTo = new JDateChooser();
		txThoigianTo.setBounds(133, 157, 148, 21);
		add(txThoigianTo);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panel_1.setBounds(414, 87, 903, 540);
		add(panel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		scrollPane.setBounds(21, 38, 872, 474);
		panel_1.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"STT", "M\u00E3 h\u00F3a \u0111\u01A1n", "T\u00EAn kh\u00E1ch h\u00E0ng", "Nh\u00E2n vi\u00EAn l\u1EADp", "Th\u1EDDi gian", "T\u1ED5ng ti\u1EC1n"
			}
		));
		
		scrollPane.setViewportView(table);
		
		try {
			listHoaDon = hoaDonService.findAll();
			loadTable(listHoaDon);
		} catch (Exception ex) {
			ex.printStackTrace();
			MessageDialog.error(this, "Lỗi khi tải dữ liệu hóa đơn: " + ex.getMessage());
		}
		
		JLabel lblNewLabel_1_2 = new JLabel("Hóa đơn đã thanh toán");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_2.setBounds(21, 10, 185, 20);
		panel_1.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("Do nhân viên lập");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_3.setBounds(122, 225, 135, 25);
		add(lblNewLabel_1_3);
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 15));
	
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Tất cả"}));
		comboBox.setBounds(76, 260, 234, 21);
		add(comboBox);
		
		JButton btnXem = new JButton();
		btnXem.setIcon(new ImageIcon(gui_thongKeDoanhThu.class.getResource("/icon/revenue.png")));
		btnXem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thongKe();
			}
		});
		btnXem.setText("XEM THỐNG KÊ");
		btnXem.setPreferredSize(new Dimension(120, 40));
		btnXem.setForeground(SystemColor.desktop);
		btnXem.setFont(new Font("Dialog", Font.BOLD, 16));
		btnXem.setFocusable(false);
		btnXem.setFocusPainted(false);
		btnXem.setBorderPainted(false);
		btnXem.setBackground(SystemColor.activeCaptionBorder);
		btnXem.setBounds(106, 317, 192, 36);
		add(btnXem);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(39, 399, 344, 228);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1_4 = new JLabel("Số hóa đơn đã lập");
		lblNewLabel_1_4.setBounds(10, 24, 165, 25);
		panel.add(lblNewLabel_1_4);
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		txt_sumHD = new JTextField();
		txt_sumHD.setFont(new Font("Tahoma", Font.BOLD, 15));
		txt_sumHD.setBounds(197, 29, 96, 19);
		panel.add(txt_sumHD);
		txt_sumHD.setColumns(10);
		
		JLabel lblNewLabel_1_4_1 = new JLabel("Số hóa đơn đổi/trả");
		lblNewLabel_1_4_1.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblNewLabel_1_4_1.setBounds(45, 59, 138, 25);
		panel.add(lblNewLabel_1_4_1);
		
		txt_sumTra = new JTextField();
		txt_sumTra.setForeground(new Color(255, 0, 0));
		txt_sumTra.setFont(new Font("Tahoma", Font.BOLD, 15));
		txt_sumTra.setColumns(10);
		txt_sumTra.setBounds(197, 64, 96, 19);
		panel.add(txt_sumTra);
		
		JLabel lblNewLabel_1_4_2 = new JLabel("TỔNG DOANH THU");
		lblNewLabel_1_4_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_4_2.setBounds(94, 131, 165, 25);
		panel.add(lblNewLabel_1_4_2);
		
		txt_SumDoanhThu = new JTextField();
		txt_SumDoanhThu.setHorizontalAlignment(SwingConstants.CENTER);
		txt_SumDoanhThu.setFont(new Font("Tahoma", Font.BOLD, 20));
		txt_SumDoanhThu.setColumns(10);
		txt_SumDoanhThu.setBounds(34, 166, 259, 46);
		panel.add(txt_SumDoanhThu);
		
		JButton btn_Xuat = new JButton();
		btn_Xuat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					NhanVien nhanVien = nhanVienService.findById(login.getNhanVien().getId());
					JTableExporter.exportJTableToExcel(table, new Timestamp(System.currentTimeMillis()), nhanVien.getHoTen());
				} catch (Exception ex) {
					ex.printStackTrace();
					MessageDialog.error(gui_thongKeDoanhThu.this, "Lỗi khi xuất thống kê: " + ex.getMessage());
				}
			}
		});
		btn_Xuat.setText("Xuất Thống Kê");
		btn_Xuat.setPreferredSize(new Dimension(120, 40));
		btn_Xuat.setForeground(SystemColor.desktop);
		btn_Xuat.setFont(new Font("Arial", Font.BOLD, 16));
		btn_Xuat.setFocusable(false);
		btn_Xuat.setFocusPainted(false);
		btn_Xuat.setBorderPainted(false);
		btn_Xuat.setBackground(SystemColor.activeCaptionBorder);
		btn_Xuat.setBounds(631, 658, 206, 57);
		add(btn_Xuat);
		
		// Setup table sorter
		sorter = new TableRowSorter<>(modal);
        table.setRowSorter(sorter);
		table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint()); // Cột được nhấp
                if (column == 5) { 
                    sorter.setComparator(5, (o1, o2) -> {
                        double value1 = other.Formatter.unformatVND(o1.toString());
                        double value2 = other.Formatter.unformatVND(o2.toString());
                        return Double.compare(value1, value2);
                    });
                    sorter.toggleSortOrder(5); 
                }
            }
        });
		
		loadCBB();
		details();
	}
	
	private void initRMIServices() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8989);
            
            // Lookup RMI services
            hoaDonService = (HoaDonService) registry.lookup("HoaDonService");
            chiTietHoaDonService = (ChiTietHoaDonService) registry.lookup("ChiTietHoaDonService");
            nhanVienService = (NhanVienService) registry.lookup("NhanVienService");
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.error(this, "Lỗi kết nối đến RMI Server: " + e.getMessage());
        }
    }
	
    public void loadTable(List<HoaDon> list) {
        String[] header = new String[]{"STT", "Mã hóa đơn", "Tên khách hàng", "Tên nhân viên lập", "Thời gian", "Tổng tiền"};
        modal = new DefaultTableModel();
        modal.setColumnIdentifiers(header);
        table.setModel(modal);
        modal.setRowCount(0);
        listHoaDon = list;
        int stt = 1;
        
        try {
            for (HoaDon e : list) { 
                double tongTien = 0.0;
                try {
                    // Sử dụng RMI service để lấy tổng tiền hóa đơn
                    tongTien = hoaDonService.calculateTotalAmount(e.getId());
                } catch (Exception ex) {
                    System.err.println("Lỗi khi tính tổng tiền cho hóa đơn " + e.getId() + ": " + ex.getMessage());
                }
                
                modal.addRow(new Object[]{
                    String.valueOf(stt), 
                    e.getId(),
                    e.getKhachHang() != null ? e.getKhachHang().getHoTen() : "Khách lẻ", 
                    e.getNhanVien().getHoTen(),
                    e.getThoiGian(),
                    other.Formatter.FormatVND(tongTien)
                });
                stt++;
            }
            
            // Cập nhật sorter sau khi đã tạo model mới
            sorter = new TableRowSorter<>(modal);
            table.setRowSorter(sorter);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageDialog.error(this, "Lỗi khi hiển thị dữ liệu: " + ex.getMessage());
        }
    }

    private void loadCBB() {
        try {
            comboBox.removeAllItems(); // Xóa dữ liệu cũ
            comboBox.addItem("Tất cả"); // Thêm tùy chọn mặc định
            
            // Sử dụng RMI service để lấy danh sách nhân viên
            List<NhanVien> nhanVienList = nhanVienService.findAll();
            for (NhanVien nv : nhanVienList) {
                comboBox.addItem(nv.getHoTen());
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.error(this, "Lỗi khi tải danh sách nhân viên: " + e.getMessage());
        }
    }

    private void thongKe() {
    	List<HoaDon> arr = new ArrayList<>(); 
        try {
            Date fromDate = txThoigianFrom.getDate();
            Date toDate = txThoigianTo.getDate();
            String selectedNhanVien = comboBox.getSelectedItem().toString();

            if (fromDate == null || toDate == null) {
                MessageDialog.error(this, "Vui lòng chọn đầy đủ khoảng thời gian!");
                return;
            }

            if (fromDate.after(toDate)) {
                MessageDialog.error(this, "Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                return;
            }

            if (listHoaDon == null || listHoaDon.isEmpty()) {
                MessageDialog.error(this, "Không có dữ liệu hóa đơn!");
                return;
            }
            
            // Chuyển đổi Date sang LocalDate để sử dụng với RMI service
            LocalDate startLocalDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Sử dụng RMI service để lấy hóa đơn theo khoảng thời gian
            List<HoaDon> filteredByDate = hoaDonService.findByDateRange(startLocalDate, endLocalDate);
            
            int sum = 0;
            for (HoaDon hd : filteredByDate) {
                boolean byNhanVien = selectedNhanVien.equals("Tất cả") || 
                                     hd.getNhanVien().getHoTen().equals(selectedNhanVien);

                if (byNhanVien) {
                    arr.add(hd);
                    sum++;
                }
            }
            
            double tongTien = 0;
            int k = 0;
            modal.setRowCount(0);
            int stt = 1;
            
            for (HoaDon e : arr) {
                double tt = 0;
                try {
                    tt = hoaDonService.calculateTotalAmount(e.getId());
                    tongTien += tt;
                } catch (Exception ex) {
                    System.err.println("Lỗi khi tính tổng tiền cho hóa đơn " + e.getId() + ": " + ex.getMessage());
                }
                
                modal.addRow(new Object[]{
                    String.valueOf(stt++), 
                    e.getId(),
                    e.getKhachHang() != null ? e.getKhachHang().getHoTen() : "Khách lẻ", 
                    e.getNhanVien().getHoTen(),
                    e.getThoiGian(),
                    other.Formatter.FormatVND(tt)
                });
                
                if(tt == 0.0) {
                    k++;
                }
            }

            txt_sumHD.setText(String.valueOf(sum));
            txt_sumTra.setText(String.valueOf(k));
            txt_SumDoanhThu.setText(other.Formatter.FormatVND(tongTien));
            
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageDialog.error(this, "Đã xảy ra lỗi khi thống kê: " + ex.getMessage());
        }
    }
    
    private void details() {
        try {
            // Lấy ngày hiện tại
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date todayStart = calendar.getTime();
            txThoigianFrom.setDate(todayStart);

            calendar.add(Calendar.DAY_OF_YEAR, 0); 
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            Date tomorrowEnd = calendar.getTime();
            txThoigianTo.setDate(tomorrowEnd);
            
            // Sử dụng RMI service để lấy thông tin nhân viên
            if (tk != null && tk.getNhanVien() != null) {
                NhanVien nv = nhanVienService.findById(tk.getNhanVien().getId());
                if (nv != null) {
                    comboBox.setSelectedItem(nv.getHoTen());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.error(this, "Đã xảy ra lỗi khi thiết lập chi tiết mặc định: " + e.getMessage());
        }
    }
}
