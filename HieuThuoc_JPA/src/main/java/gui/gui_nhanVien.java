package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.NhanVienDAO;
import dao.impl.NhanVienDAOImpl;
import entity.NhanVien;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.toedter.calendar.JDateChooser;
import other.CustomButton;
import other.CustomComboBox;
import other.CustomTable;
import other.CustomTextField;
import service.NhanVienService;

import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JSeparator;

public class gui_nhanVien extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private CustomTextField txt_maNV;
	private CustomTextField txt_tenNV;
	private CustomTextField txt_sDT;
	private CustomTextField txt_namSinh;
	private CustomTextField txt_ngayVaoLam;
	private CustomComboBox cbo;
	private DefaultTableModel model;
	private CustomTable table;
	private NhanVienService NV_SERVICE;
	private ArrayList<NhanVien> list;
	private JDateChooser datechooser;
	/**
	 * Create the panel.
	 */
	public gui_nhanVien() throws RemoteException {
		try {
			Registry registry = LocateRegistry.getRegistry(8989);
			NV_SERVICE = (NhanVienService) registry.lookup("NhanVienService");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Không thể kết nối đến server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		setSize(1400,800);
		setLayout(null);
		
		JLabel lbl_title = new JLabel("QUẢN LÝ NHÂN VIÊN");
		lbl_title.setFont(new Font("Tahoma", Font.BOLD, 35));
		lbl_title.setBounds(502, 0, 400, 40);
		add(lbl_title);
		
		JLabel lbl_1 = new JLabel("Mã nhân viên");
		lbl_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_1.setBounds(176, 85, 150, 35);
		add(lbl_1);
		
		txt_maNV = new CustomTextField();
		txt_maNV.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_maNV.setBounds(336, 85, 300, 35);
		add(txt_maNV);
		txt_maNV.setColumns(10);
		
		JLabel lbl_2 = new JLabel("Tên nhân viên");
		lbl_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_2.setBounds(175, 170, 150, 30);
		add(lbl_2);
		
		JLabel lbl_3 = new JLabel("Số điện thoại");
		lbl_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_3.setBounds(175, 255, 150, 35);
		add(lbl_3);
		
		JLabel lbl_4 = new JLabel("Giới tính");
		lbl_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_4.setBounds(752, 85, 150, 35);
		add(lbl_4);
		
		JLabel lbl_5 = new JLabel("Năm sinh");
		lbl_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_5.setBounds(752, 168, 150, 35);
		add(lbl_5);
		
		JLabel lbl_6 = new JLabel("Ngày vào làm");
		lbl_6.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_6.setBounds(752, 255, 150, 35);
		add(lbl_6);
		
		txt_tenNV = new CustomTextField();
		
		txt_tenNV.setColumns(10);
		txt_tenNV.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_tenNV.setBounds(336, 168, 300, 35);
		add(txt_tenNV);
		
		txt_sDT = new CustomTextField();
		txt_sDT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_sDT.setColumns(10);
		txt_sDT.setBounds(336, 255, 300, 35);
		add(txt_sDT);
		
		txt_namSinh = new CustomTextField();
		txt_namSinh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_namSinh.setColumns(10);
		txt_namSinh.setBounds(910, 168, 300, 35);
		add(txt_namSinh);
		
		
		
		datechooser = new JDateChooser();
		datechooser.setBounds(910, 255, 300, 35);
		add(datechooser);
		
		cbo = new CustomComboBox();
		cbo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbo.setModel(new DefaultComboBoxModel(new String[] {"Nam", "Nữ"}));
		cbo.setBounds(910, 85, 300, 35);
		add(cbo);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(45, 376, 1294, 2);
		add(separator);
		
		JLabel lbl_title2 = new JLabel("Danh sách nhân viên");
		lbl_title2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbl_title2.setBounds(45, 388, 200, 35);
		add(lbl_title2);
		
		String[] columnNames = { "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Giới tính", "Năm sinh", "Ngày vào làm" };
		model = new DefaultTableModel(columnNames,0);
		
		table = new CustomTable();
		table.setModel(model);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(222, 433, 902, 210);
		add(scrollPane);
		
		
		CustomButton btn_themNV = new CustomButton("Thêm Nhân Viên");
		btn_themNV.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_themNV.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        try {
		            //Lay du lieu
		            String maNV = txt_maNV.getText().trim();
		            String tenNV = txt_tenNV.getText().trim();
		            String sdt = txt_sDT.getText().trim();
		            String gioiTinh = cbo.getSelectedItem().toString(); 
		            String namSinhText = txt_namSinh.getText().trim();
		            LocalDate ngayVaoLamUtil = datechooser.getDate() == null? LocalDate.now() :
							datechooser.getDate().toInstant()
									.atZone(ZoneId.systemDefault())
									.toLocalDate();

		            // //kiem tra rong
		            if (maNV.isEmpty() || tenNV.isEmpty() || sdt.isEmpty() || namSinhText.isEmpty() || ngayVaoLamUtil == null) {
		                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
		                return;
		            }
		            //NVXXX
		            if (!maNV.matches("NV\\d{3}")) {
		                JOptionPane.showMessageDialog(null, "Mã nhân viên phải có định dạng NVXXX (ví dụ: NV001)!");
		                return;
		            }
		            //ho ten
		            if (!tenNV.matches("^[a-zA-ZÀ-ỹ\\s]{2,}$")) {
		                JOptionPane.showMessageDialog(null, "Họ tên chỉ được chứa chữ cái và khoảng trắng, tối thiểu 2 ký tự!");
		                return;
		            }
		            // nam sinh
		            int namSinh;
		            try {
		                namSinh = Integer.parseInt(namSinhText);
		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(null, "Năm sinh phải là một số nguyên hợp lệ!");
		                return;
		            }

		            // tuoi > 18
		            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
		            int age = currentYear - namSinh;
		            if (age < 18) {
		                JOptionPane.showMessageDialog(null, "Nhân viên phải trên 18 tuổi!");
		                return;
		            }

		            if (!ngayVaoLamUtil.isEqual(LocalDate.now())) {
		                JOptionPane.showMessageDialog(null, "Ngày vào làm phải là ngày hiện tại!");
		                return;
		            }
					LocalDate ngayVaoLam = ngayVaoLamUtil;

		            // sdt
		            if (!sdt.matches("^0\\d{9}$")) {
		                JOptionPane.showMessageDialog(null, "Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
		                return;
		            }

		            //kiem tra ma trung
		            ArrayList<NhanVien> dsNCC = new ArrayList<>(NV_SERVICE.findAll());
		            for (NhanVien nvExist : dsNCC) {
		                if (nvExist.getId().equals(maNV)) {
		                    JOptionPane.showMessageDialog(null, "Mã nhân viên đã tồn tại!");
		                    return;
		                }
		            }

		            
		            boolean genderBoolean = gioiTinh.equals("Nữ");

		           
		            NhanVien nv = new NhanVien(maNV, tenNV, sdt, genderBoolean, namSinh, ngayVaoLam);

		           
		            if (NV_SERVICE.save(nv)) {
		                JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
		                model.addRow(new Object[] { maNV, tenNV, sdt, (genderBoolean ? "Nữ" : "Nam"), namSinh, ngayVaoLam });
		                clearTextFields();
		            } else {
		                JOptionPane.showMessageDialog(null, "Thêm nhân viên thất bại!");
		            }
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
		        }
		    }
		});



		btn_themNV.setBounds(325, 314, 150, 35);
		add(btn_themNV);
		
		CustomButton btn_suaNV = new CustomButton("Sửa Thông Tin");
		btn_suaNV.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_suaNV.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        try {
					int row = table.getSelectedRow();
					if (row == -1) {
						JOptionPane.showMessageDialog(null, "Bạn chưa chọn nhân viên để sửa", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

		            String maNV = txt_maNV.getText().trim();
		            String tenNV = txt_tenNV.getText().trim();
		            String sdt = txt_sDT.getText().trim();
		            String gioiTinh = cbo.getSelectedItem().toString();
		            String namSinhText = txt_namSinh.getText().trim();
		            LocalDate ngayVaoLamUtil = datechooser.getDate() == null? null :
							datechooser.getDate()
									.toInstant()
									.atZone(ZoneId.systemDefault())
									.toLocalDate();

		            if (maNV.isEmpty() || tenNV.isEmpty() || sdt.isEmpty() || namSinhText.isEmpty() || ngayVaoLamUtil == null) {
		                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
		                return;
		            }

		            // Kiểm tra định dạng mã nhân viên
		            if (!maNV.matches("NV\\d{3}")) {
		                JOptionPane.showMessageDialog(null, "Mã nhân viên phải có định dạng NVXXX (ví dụ: NV001)!");
		                return;
		            }

		            // Kiểm tra định dạng họ tên
		            if (!tenNV.matches("^[a-zA-ZÀ-ỹ\\s]{2,}$")) {
		                JOptionPane.showMessageDialog(null, "Họ tên chỉ được chứa chữ cái và khoảng trắng, tối thiểu 2 ký tự!");
		                return;
		            }

		            // Kiểm tra năm sinh
		            int namSinh;
		            try {
		                namSinh = Integer.parseInt(namSinhText);
		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(null, "Năm sinh phải là một số nguyên hợp lệ!");
		                return;
		            }

		            // Kiểm tra tuổi phải trên 18
		            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
		            int age = currentYear - namSinh;
		            if (age < 18) {
		                JOptionPane.showMessageDialog(null, "Nhân viên phải trên 18 tuổi!");
		                return;
		            }

		            // Lấy ngày hiện tại
		            java.util.Calendar calendar = java.util.Calendar.getInstance();
		            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
		            calendar.set(java.util.Calendar.MINUTE, 0);
		            calendar.set(java.util.Calendar.SECOND, 0);
		            calendar.set(java.util.Calendar.MILLISECOND, 0);

		            // Kiểm tra ngày vào làm phải nhỏ hơn hoặc bằng ngày hiện tại
		            if (ngayVaoLamUtil.isAfter(LocalDate.now())) {
		                JOptionPane.showMessageDialog(null, "Ngày vào làm phải nhỏ hơn hoặc bằng ngày hiện tại!");
		                return;
		            }

					LocalDate ngayVaoLam = ngayVaoLamUtil;

		            // Kiểm tra số điện thoại
		            if (!sdt.matches("^0\\d{9}$")) {
		                JOptionPane.showMessageDialog(null, "Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
		                return;
		            }

		            boolean isMale = gioiTinh.equals("Nữ"); // Chuyển đổi giới tính sang boolean
		            NhanVien nv = new NhanVien(maNV, tenNV, sdt, isMale, namSinh, ngayVaoLam);

		            if (NV_SERVICE.update(nv)) {
		                JOptionPane.showMessageDialog(null, "Cập nhật thông tin nhân viên thành công!");

		                // Cập nhật dữ liệu trên bảng
		                model.setValueAt(tenNV, row, 1);
		                model.setValueAt(sdt, row, 2);
		                model.setValueAt(gioiTinh, row, 3);
		                model.setValueAt(namSinh, row, 4);
		                model.setValueAt(ngayVaoLam, row, 5);
		            } else {
		                JOptionPane.showMessageDialog(null, "Cập nhật thông tin nhân viên thất bại!");
		            }
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
		        }
		    }
		});



		btn_suaNV.setBounds(647, 314, 150, 35);
		add(btn_suaNV);
		
//		JButton btn_xoaNV = new JButton("Xóa Nhân Viên");
//		btn_xoaNV.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		btn_xoaNV.addActionListener(new ActionListener() {
//		    public void actionPerformed(ActionEvent e) {
//		        int row = table.getSelectedRow();
//		        if (row >= 0) {
//		            String maNV = txt_maNV.getText().trim();
//		            
//		            // Kiểm tra xem mã nhà cung cấp có được chọn hay không
//                    if (maNV.isEmpty()) {
//                        JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp để xóa!");
//                        return;
//                    }
//		            
//		            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
//		            
//		            if (confirm == JOptionPane.YES_OPTION) {
//		                // Delete the employee from the database
//		                if (nvDao.delete(maNV)) {
//		                    JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
//		                    
//		                    // Remove row from the table model
//		                    model.removeRow(row);
//		                    clearTextFields();
//		                } else {
//		                    JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại!");
//		                }
//		            }
//		        } else {
//		            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên để xóa!");
//		        }
//		    }
//		});
//		btn_xoaNV.setBounds(752, 314, 150, 35);
//		add(btn_xoaNV);
		
		CustomButton btn_ex = new CustomButton("Export");
		btn_ex.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_ex.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (list.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Danh sách nhân viên rỗng!");
		            return; // Không làm gì nếu danh sách rỗng
		        }

		        try (XSSFWorkbook wb = new XSSFWorkbook()) {
		            XSSFSheet sheet = wb.createSheet("danhsachnhanvien");
		            XSSFRow row = null;
		            Cell cell = null;

		            // Tạo định dạng ngày tháng cho Excel
		            XSSFCellStyle dateCellStyle = wb.createCellStyle();
		            XSSFCreationHelper createHelper = wb.getCreationHelper();
		            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

		            // Tiêu đề các cột
		            row = sheet.createRow(3);
		            String[] headers = { "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Giới tính", "Năm sinh", "Ngày vào làm" };
		            for (int i = 0; i < headers.length; i++) {
		                cell = row.createCell(i + 1, CellType.STRING);
		                cell.setCellValue(headers[i]);
		            }

		            // Ghi dữ liệu vào các dòng tiếp theo
		            for (int i = 0; i < list.size(); i++) {
		                row = sheet.createRow(4 + i);
		                cell = row.createCell(1, CellType.STRING);
		                cell.setCellValue(list.get(i).getId());

		                cell = row.createCell(2, CellType.STRING);
		                cell.setCellValue(list.get(i).getHoTen());

		                cell = row.createCell(3, CellType.STRING);
		                cell.setCellValue(list.get(i).getSoDienThoai());

		                String gioiTinh = list.get(i).isGioiTinh() ? "Nữ" : "Nam";
		                cell = row.createCell(4, CellType.STRING);
		                cell.setCellValue(gioiTinh);

		                // Xuất năm sinh dạng int
		                cell = row.createCell(5, CellType.NUMERIC);
		                cell.setCellValue(list.get(i).getNamSinh());

		                // Xử lý cột ngày vào làm
		                cell = row.createCell(6);
		                if (list.get(i).getNgayVaoLam() != null) {
		                    cell.setCellValue(list.get(i).getNgayVaoLam());
		                    cell.setCellStyle(dateCellStyle);
		                }
		            }

		            // Sử dụng JFileChooser để chọn vị trí lưu file
		            JFileChooser fileChooser = new JFileChooser();
		            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
		            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		            fileChooser.setSelectedFile(new File("danhsachnhanvien.xlsx"));

		            int userSelection = fileChooser.showSaveDialog(null);
		            if (userSelection == JFileChooser.APPROVE_OPTION) {
		                File fileToSave = fileChooser.getSelectedFile();

		                // Đảm bảo file có đuôi .xlsx
		                String filePath = fileToSave.getAbsolutePath();
		                if (!filePath.endsWith(".xlsx")) {
		                    filePath += ".xlsx";
		                }

		                try (FileOutputStream fls = new FileOutputStream(filePath)) {
		                    wb.write(fls);
		                    JOptionPane.showMessageDialog(null, "Xuất thành công danh sách nhân viên!\nFile được lưu tại: " + filePath);
		                } catch (FileNotFoundException e2) {
		                    JOptionPane.showMessageDialog(null, "Không tìm thấy file hoặc không có quyền ghi file.");
		                    e2.printStackTrace();
		                } catch (IOException e2) {
		                    JOptionPane.showMessageDialog(null, "Lỗi trong khi ghi file.");
		                    e2.printStackTrace();
		                }
		            } else {
		                JOptionPane.showMessageDialog(null, "Xuất file đã bị hủy.");
		            }
		        } catch (IOException e2) {
		            JOptionPane.showMessageDialog(null, "Lỗi khi tạo workbook.");
		            e2.printStackTrace();
		        }
		    }
		});



		btn_ex.setBounds(966, 314, 100, 35);
		add(btn_ex);
		
		
		DocDuLieuDatabaseVaoTable();
		table.addMouseListener(this);
		addMouseListener(this);
	}

	 private void clearTextFields() {
		    txt_maNV.setText("");  // Clear Mã nhân viên
		    txt_tenNV.setText("");  // Clear Tên nhân viên
		    txt_sDT.setText("");  // Clear Số điện thoại
		    cbo.setSelectedIndex(0);  // Reset Giới tính về Nam
		    txt_namSinh.setText("");  // Clear Năm sinh
		    datechooser.setDate(null);  // Clear Ngày vào làm
		}
		private void DocDuLieuDatabaseVaoTable() throws RemoteException {
			list = new ArrayList<>(NV_SERVICE.findAll());
			for (NhanVien nv : list) {
				model.addRow(new Object[] {nv.getId(),nv.getHoTen(),nv.getSoDienThoai(),nv.isGioiTinh() ? "Nữ" : "Nam",nv.getNamSinh(),nv.getNgayVaoLam() });
			}
		}
		public static void main(String[] args) throws RemoteException {
		
			JFrame frame = new JFrame("Quản Lý Nhân Viên");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1400, 800);
			frame.setLocationRelativeTo(null);
			frame.getContentPane().add(new gui_nhanVien());
			frame.setVisible(true);
	}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(!table.equals(e.getSource())) {
				txt_maNV.setEnabled(true);
				table.getSelectionModel().clearSelection();
				if(e.getClickCount() == 2)
					clearTextFields();
				return;
			}
			txt_maNV.setEnabled(false);
			int row = table.getSelectedRow();
			if (row>=0) {
				txt_maNV.setText(model.getValueAt(row, 0).toString()); // Mã nhân viên
				txt_tenNV.setText(model.getValueAt(row, 1).toString()); // Tên nhân viên
				txt_sDT.setText(model.getValueAt(row, 2).toString()); // Số điện thoại
        
				// Giới tính (assuming comboBox at index 3)
				String gender = model.getValueAt(row, 3).toString();
				if (gender.equals("Nam")) {
					cbo.setSelectedIndex(0); 
				} else {
					cbo.setSelectedIndex(1); 
				}
        
				txt_namSinh.setText(model.getValueAt(row, 4).toString()); // Năm sinh
				String dateString = model.getValueAt(row, 5).toString();
				Date date = Date.valueOf(((LocalDate) model.getValueAt(row, 5)));
				datechooser.setDate(date);
		
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
	
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
	
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
	
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
	
		}
	}
