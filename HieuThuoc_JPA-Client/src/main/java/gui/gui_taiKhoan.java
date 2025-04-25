package gui;

import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;
import jakarta.persistence.EntityManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import other.CustomButton;
import other.CustomComboBox;
import other.CustomTable;
import other.CustomTextField;
import service.NhanVienService;
import service.OneSessionService;
import service.TaiKhoanService;
import service.VaiTroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
import java.util.ArrayList;

public class gui_taiKhoan extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private CustomTextField txt_maTK;
	private CustomTextField txt_mk;
	private CustomTable table;
	private CustomTextField txt_tim;
	private CustomComboBox cbo_nv,cbo_vt;
	private DefaultTableModel model;
	EntityManager em;
	private TaiKhoanService TK_SERVICE;
	private NhanVienService NV_SERVICE;
	private VaiTroService VT_SERVICE;
	private OneSessionService ONE_SESSION_SERVICES;
	private ArrayList<TaiKhoan> list;
	/**
	 * Create the panel.
	 */
	public gui_taiKhoan() throws RemoteException {
		try {
			Registry registry = LocateRegistry.getRegistry(BinhCode.HOST, 8989);
			TK_SERVICE = (TaiKhoanService) registry.lookup("TaiKhoanService");
			NV_SERVICE = (NhanVienService) registry.lookup("NhanVienService");
			VT_SERVICE = (VaiTroService) registry.lookup("VaiTroService");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Không thể kết nối đến server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		setSize(1400,800);
		setLayout(null);
		
		JLabel lbl_title = new JLabel("TÀI KHOẢN");
		lbl_title.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbl_title.setBounds(605, 10, 400, 35);
		add(lbl_title);
		
		JLabel lbl_1 = new JLabel("Mã tài khoản");
		lbl_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_1.setBounds(387, 78, 150, 35);
		add(lbl_1);
		
		JLabel lbl_3 = new JLabel("Mật Khẩu");
		lbl_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_3.setBounds(387, 147, 150, 35);
		add(lbl_3);
		
		JLabel lbl_4 = new JLabel("Nhân viên");
		lbl_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_4.setBounds(387, 209, 150, 35);
		add(lbl_4);
		
		txt_maTK = new CustomTextField();
		txt_maTK.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_maTK.setBounds(571, 78, 400, 35);
		add(txt_maTK);
		txt_maTK.setColumns(10);
		
		txt_mk = new CustomTextField();
		txt_mk.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_mk.setColumns(10);
		txt_mk.setBounds(571, 147, 400, 35);
		add(txt_mk);
		
		JLabel lbl_5 = new JLabel("Vai trò");
		lbl_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_5.setBounds(387, 269, 150, 35);
		add(lbl_5);
		
		 cbo_nv = new CustomComboBox();
		 cbo_nv.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbo_nv.setBounds(573, 209, 200, 35);
		add(cbo_nv);
		
		 cbo_vt = new CustomComboBox();
		 cbo_vt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbo_vt.setBounds(571, 269, 200, 35);
		add(cbo_vt);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(41, 383, 1289, 2);
		add(separator);
		
		CustomButton btn_them = new CustomButton("Thêm tài khoản");
		btn_them.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_them.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
                try {
                    String maTK = txt_maTK.getText().trim();
                    String mk = txt_mk.getText().trim();
                    String nv = (String) cbo_nv.getSelectedItem();
                    String vt = (String) cbo_vt.getSelectedItem();


                    if (maTK.isEmpty() ||  mk.isEmpty() || nv.isEmpty()|| vt.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
                        return;
                    }


                    ArrayList<TaiKhoan> dsTK = new ArrayList<>(TK_SERVICE.findAll());
                    for (TaiKhoan tkExist : dsTK) {
                        if (tkExist.getId().equalsIgnoreCase(maTK)) {
                            JOptionPane.showMessageDialog(null, "Mã tài khoản đã tồn tại!");
                            return;
                        }
                    }


                    TaiKhoan tk = new TaiKhoan(maTK, mk, NV_SERVICE.findByTen(nv), VT_SERVICE.findByTenVaiTro(vt));
                    System.out.println(tk);
                    boolean result = TK_SERVICE.save(tk);

                    if (result) {
                        JOptionPane.showMessageDialog(null, "Thêm tài khoản thành công!");
                        model.addRow(new Object[] { maTK, mk, nv, vt });
                        clearInputFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm tài khoản thất bại!");
                    }
                } catch (RemoteException ex) {
					ex.printStackTrace();
                }
            }
		});
		btn_them.setBounds(354, 338, 150, 35);
		add(btn_them);
		
		JLabel lbl_6 = new JLabel("Danh sách tài khoản");
		lbl_6.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbl_6.setBounds(41, 395, 270, 35);
		add(lbl_6);
		
		String[] columnNames = { "Mã tài khoản", "Mật khẩu", "Nhân viên", "Vai trò" };
		model = new DefaultTableModel(columnNames,0);
		
		table = new CustomTable();
		table.setModel(model);
		table.setBounds(489, 424, 1, 1);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(215, 524, 900, 154);
		add(scrollPane);
		
		CustomComboBox cbo_tim = new CustomComboBox();
		cbo_tim.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbo_tim.setModel(new DefaultComboBoxModel(new String[] {"Mã tài khoản", "Vai trò","Nhân viên"}));
		cbo_tim.setSelectedIndex(1);
		cbo_tim.setBounds(215, 446, 150, 35);
		add(cbo_tim);
		
		txt_tim = new CustomTextField();
		txt_tim.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_tim.setBounds(405, 446, 200, 35);
		add(txt_tim);
		txt_tim.setColumns(10);

		CustomButton btn_tim = new CustomButton("Tìm");
		btn_tim.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_tim.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String searchText = txt_tim.getText().trim(); // Get the search input
		        String searchCriteria = (String)cbo_tim.getSelectedItem(); // Get the search criteria
		        
		        // Check if the search text is empty
		        if (searchText.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Vui lòng nhập thông tin tìm kiếm!");
		            return;
		        }

		        // Clear the table before showing the filtered results
		        model.setRowCount(0);

		        // Filter based on the selected criteria
		        for (TaiKhoan tk : list) {
		            if (searchCriteria.equals("Vai trò")) {
		                // Use tenVT (role name) instead of maVT (role ID) and add a null check
		                String tenVaiTro = tk.getVaiTro().getTenVaiTro();
		                if (tenVaiTro != null && tenVaiTro.toLowerCase().contains(searchText.toLowerCase())) {
		                    model.addRow(new Object[] { tk.getId(), tk.getPassword(), tk.getNhanVien().getHoTen(), tenVaiTro });
		                }
		            } else if (searchCriteria.equals("Mã tài khoản")) {
		                if (tk.getId() != null && tk.getId().toLowerCase().contains(searchText.toLowerCase())) {
		                    model.addRow(new Object[] { tk.getId(), tk.getPassword(), tk.getNhanVien().getHoTen(), tk.getVaiTro().getTenVaiTro() });
		                }
		            } else if (searchCriteria.equals("Nhân viên")) {
		                String hoTenNhanVien = tk.getNhanVien().getHoTen();
		                if (hoTenNhanVien != null && hoTenNhanVien.toLowerCase().contains(searchText.toLowerCase())) {
		                    model.addRow(new Object[] { tk.getId(), tk.getPassword(), hoTenNhanVien, tk.getVaiTro().getTenVaiTro() });
		                }
		            }
		        }
		        
		        // If no matching data is found
		        if (model.getRowCount() == 0) {
		            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả phù hợp!");
		        }
		    }
		});

		btn_tim.setBounds(654, 446, 100, 35);
		add(btn_tim);

		CustomButton btn_lamMoi = new CustomButton("Làm mới");
		btn_lamMoi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_lamMoi.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        clearInputFields();
                try {
                    model.setRowCount(0); // Clear the table model
                    DocDuLieuDatabaseVaoTable(); // Refresh data
                } catch (RemoteException ex) {
					ex.printStackTrace();
                }
            }
		});
		btn_lamMoi.setBounds(791, 446, 100, 35);
		add(btn_lamMoi);

		CustomButton btn_sua = new CustomButton("Sửa thông tin");
		btn_sua.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_sua.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
                try {
                    // Get the selected account ID from the input field
                    String maTK = txt_maTK.getText().trim();

                    // Check if the account ID field is empty
                    if (maTK.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn tài khoản để sửa thông tin!");
                        return;
                    }

                    // Retrieve the existing account details using the DAO
                    TaiKhoan existingTK = TK_SERVICE.findById(maTK); // Assuming this method exists

                    // Check if the account exists
                    if (existingTK == null) {
                        JOptionPane.showMessageDialog(null, "Tài khoản không tồn tại!");
                        return;
                    }

                    // Get the updated information from the text fields
                    String matKhau = txt_mk.getText().trim();
                    String maNV = (String) cbo_nv.getSelectedItem(); // Get selected employee
                    String maVT = (String) cbo_vt.getSelectedItem(); // Get selected role

                    // Validate the input fields
                    if ( matKhau.isEmpty() || maNV == null || maVT == null) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
                        return;
                    }

                    // Create an updated TaiKhoan object
					existingTK.setPassword(matKhau);
					existingTK.setNhanVien(NV_SERVICE.findByTen(maNV));
					existingTK.setVaiTro(VT_SERVICE.findByTenVaiTro(maVT));

                    // Call the update method in the DAO
                    boolean result = TK_SERVICE.update(existingTK); // Assuming this method exists

                    // Check if the update was successful
                    if (result) {
                        JOptionPane.showMessageDialog(null, "Sửa thông tin tài khoản thành công!");
                        int row = table.getSelectedRow();
						if (row != -1) {
							model.setValueAt(matKhau, row, 1);
							model.setValueAt(maNV, row, 2);
							model.setValueAt(maVT, row, 3);
						}

                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa thông tin tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
		});
		btn_sua.setBounds(538, 338, 150, 35);
		add(btn_sua);


		CustomButton btn_xoa = new CustomButton("Xóa tài khoản");
		btn_xoa.setFont(new Font("Tahoma", Font.PLAIN, 15));
	      btn_xoa.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                try {
	                    String maTK = txt_maTK.getText().trim();

	                    // Kiểm tra xem mã nhà cung cấp có được chọn hay không
	                    if (maTK.isEmpty()) {
	                        JOptionPane.showMessageDialog(null, "Vui lòng chọn mã tài khoản để xóa!");
	                        return;
	                    }

	                    // Xác nhận việc xóa
	                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
	                    if (confirm == JOptionPane.YES_OPTION) {
	                        boolean result = TK_SERVICE.delete(maTK);
	                        if (result) {
	                            JOptionPane.showMessageDialog(null, "Xóa tài khoản thành công!");
	                            int row = table.getSelectedRow();
	                            if (row != -1) {
	                                model.removeRow(row);
	                                clearInputFields();
	                            }
	                        } else {
	                            JOptionPane.showMessageDialog(null, "Xóa tài khoản thất bại!");
	                        }
	                    }
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
	                }
	            }
	        });
		btn_xoa.setBounds(736, 338, 150, 35);
		add(btn_xoa);

		CustomButton btn_ex = new CustomButton("Export");
		btn_ex.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_ex.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (list.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Danh sách tài khoản rỗng!");
		            return; // Không làm gì nếu danh sách rỗng
		        }

		        try (XSSFWorkbook wb = new XSSFWorkbook()) {
		            XSSFSheet sheet = wb.createSheet("danhsachtaikhoan");
		            XSSFRow row = null;
		            Cell cell = null;

		            // Tạo định dạng ngày tháng cho Excel
		            XSSFCellStyle dateCellStyle = wb.createCellStyle();
		            XSSFCreationHelper createHelper = wb.getCreationHelper();
		            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy")); // Format date as dd/MM/yyyy

		            // Tiêu đề các cột
		            row = sheet.createRow(3);
		            String[] headers = { "Mã tài khoản", "Mật khẩu", "Nhân viên", "Vai trò" };
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
		                cell.setCellValue(list.get(i).getPassword());

		                cell = row.createCell(3, CellType.STRING);
		                cell.setCellValue(list.get(i).getNhanVien().getHoTen());

		                cell = row.createCell(4, CellType.STRING);
		                cell.setCellValue(list.get(i).getVaiTro().getTenVaiTro());
		            }

		            // Sử dụng JFileChooser để chọn vị trí lưu file
		            JFileChooser fileChooser = new JFileChooser();
		            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
		            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		            fileChooser.setSelectedFile(new File("danhsachtaikhoan.xlsx"));

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
		                    JOptionPane.showMessageDialog(null, "Xuất thành công danh sách tài khoản!\nFile được lưu tại: " + filePath);
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


		btn_ex.setBounds(921, 338, 100, 35);
		add(btn_ex);
		
		DocDuLieuVaoCombobox();
		DocDuLieuDatabaseVaoTable();
		table.addMouseListener(this);
		addMouseListener(this);

	}
	public static void main(String[] args) throws RemoteException {
		
		JFrame frame = new JFrame("Quản Lý Tài Khoản");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1400, 800);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(new gui_taiKhoan());
		frame.setVisible(true);
}

	 private void DocDuLieuDatabaseVaoTable() throws RemoteException {
			list = new ArrayList<>(TK_SERVICE.findAll());
			for (TaiKhoan tk : list) {
				model.addRow(new Object[] {tk.getId(),tk.getPassword(),tk.getNhanVien().getHoTen(),tk.getVaiTro().getTenVaiTro() });
			}
		}
	 public void DocDuLieuVaoCombobox() {
		    try {
		        ArrayList<NhanVien> dsnv = new ArrayList<>(NV_SERVICE.findAll());
		        for (NhanVien nv : dsnv) {
		            cbo_nv.addItem(nv.getHoTen());
		        }

		        ArrayList<VaiTro> dsvt = new ArrayList<>(VT_SERVICE.findAll());
		        for (VaiTro vt : dsvt) {
		            cbo_vt.addItem(vt.getTenVaiTro());
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Lỗi khi đọc dữ liệu vào ComboBox!");
		    }
		}
	 private void clearInputFields() {
		   
		    txt_maTK.setText("");
		    txt_mk.setText("");
		    cbo_nv.setSelectedIndex(0); 
		    cbo_vt.setSelectedIndex(0);
		}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!table.equals(e.getSource())) {
			txt_maTK.setEnabled(true);
			table.getSelectionModel().clearSelection();
			if(e.getClickCount() == 2) {
				clearInputFields();
			}
			return;
		}
		txt_maTK.setEnabled(false);
		int row = table.getSelectedRow();
		if (row>=0) {
			txt_maTK.setText(model.getValueAt(row, 0).toString());  
			txt_mk.setText(model.getValueAt(row, 1).toString()); 
			String maNV = model.getValueAt(row, 2).toString();
		    String maVT = model.getValueAt(row, 3).toString();
		        
		        cbo_nv.setSelectedItem(maNV);
		        cbo_vt.setSelectedItem(maVT);
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
