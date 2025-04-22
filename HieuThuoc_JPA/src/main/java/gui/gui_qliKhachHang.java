package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import dao.impl.KhachHangDAOImpl;
import entity.KhachHang;
import entity.TaiKhoan;
import service.KhachHangService;
import service.impl.KhachHangServiceImpl;


public class gui_qliKhachHang extends JPanel implements MouseListener {
	private JTextField txtMaKH, txtTenKH, txtDienThoai;
	private JButton btnThem, btnXoa, btnSua, btnThoat;
	private JDateChooser dateChooser;
	private JComboBox<String> cboGioiTinh;
	private JTable table;
	private DefaultTableModel model;
	private KhachHangService KHACH_HANG_SEVICE;



	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Đặt Thuốc Siêu Xịn");
			gui_qliKhachHang panel = new gui_qliKhachHang();

			frame.setContentPane(panel);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
	public gui_qliKhachHang() {

		try {
			KHACH_HANG_SEVICE = new KhachHangServiceImpl(new KhachHangDAOImpl());
		} catch ( RemoteException e) {
			JOptionPane.showMessageDialog(null, "Lỗi khởi tạo service: " + e.getMessage());
			e.printStackTrace();
		}


		setLayout(new BorderLayout());
		setSize(1400, 800);

		JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 35));
		lblTitle.setForeground(new Color(0, 0, 0));

		JPanel pnTitle = new JPanel();
		pnTitle.setLayout(new BoxLayout(pnTitle, BoxLayout.Y_AXIS));
		pnTitle.add(lblTitle);
		pnTitle.add(Box.createRigidArea(new Dimension(0, 50)));
		pnTitle.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		JPanel pnInfo = new JPanel(new GridLayout(3, 4, 10, 10));
		JLabel lblMaKH = new JLabel("Mã Khách Hàng:");
		lblMaKH.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		pnInfo.add(lblMaKH);
		txtMaKH = new JTextField();
		pnInfo.add(txtMaKH);

		JLabel lblTenKH = new JLabel("Tên Khách Hàng:");
		lblTenKH.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		pnInfo.add(lblTenKH);
		txtTenKH = new JTextField();
		pnInfo.add(txtTenKH);

		JLabel lblDienThoai = new JLabel("Số Điện Thoại:");
		lblDienThoai.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		pnInfo.add(lblDienThoai);
		txtDienThoai = new JTextField();
		pnInfo.add(txtDienThoai);

		JLabel lblGioiTinh = new JLabel("Giới Tính:");
		lblGioiTinh.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		pnInfo.add(lblGioiTinh);
		cboGioiTinh = new JComboBox<>(new String[] { "Nữ", "Nam" });
		pnInfo.add(cboGioiTinh);

		JLabel lblNgayThamGia = new JLabel("Ngày Tham Gia:");
		lblNgayThamGia.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		pnInfo.add(lblNgayThamGia);
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setPreferredSize(new Dimension(150, 20));
		pnInfo.add(dateChooser);

		JPanel pnTitleAndInfo = new JPanel();
		pnTitleAndInfo.setLayout(new BoxLayout(pnTitleAndInfo, BoxLayout.Y_AXIS));
		pnTitleAndInfo.add(pnTitle);
		pnTitleAndInfo.add(pnInfo);
		pnTitleAndInfo.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		add(pnTitleAndInfo, BorderLayout.NORTH);

		JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		btnThem = new JButton("Thêm Khách Hàng");
		btnThem.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnXoa = new JButton("Xóa Khách Hàng");
		btnXoa.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnSua = new JButton("Sửa Thông Tin");
		btnSua.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnThoat = new JButton("Thoát");
		btnThoat.setFont(new Font("Times New Roman", Font.BOLD, 15));

		pnButtons.add(btnThem);
		pnButtons.add(btnXoa);
		pnButtons.add(btnSua);
		pnButtons.add(btnThoat);

		JPanel pnButtonsWrapper = new JPanel();
		pnButtonsWrapper.setLayout(new BoxLayout(pnButtonsWrapper, BoxLayout.Y_AXIS));
		pnButtonsWrapper.add(Box.createRigidArea(new Dimension(0, 50)));
		pnButtonsWrapper.add(pnButtons);

		add(pnButtonsWrapper, BorderLayout.CENTER);

		String[] columnNames = { "Mã Khách Hàng", "Họ Tên Khách Hàng", "Số Điện Thoại", "Giới Tính", "Ngày Tham Gia" };

		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Khách Hàng"));
		add(scrollPane, BorderLayout.SOUTH);

		DocDuLieuDatabaseVaoTable();
		table.addMouseListener(this);

		btnThem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String maKH = txtMaKH.getText().trim();
					String tenKH = txtTenKH.getText().trim();
					String dienThoai = txtDienThoai.getText().trim();
					String gioiTinh = cboGioiTinh.getSelectedItem().toString();
					boolean gioiTinhBoolean = gioiTinh.equals("Nữ");
					Date ngayTGUtil = dateChooser.getDate();

					if (ngayTGUtil == null || maKH.isEmpty() || tenKH.isEmpty() || dienThoai.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
						return;
					}

					if (!maKH.matches("KH\\d+")) {
						JOptionPane.showMessageDialog(null, "Mã khách hàng phải bắt đầu bằng 'KH' theo sau là số!");
						return;
					}

					if (!dienThoai.matches("^0\\d{9}$")) {
						JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
						return;
					}

					LocalDate ngayThamGia = ngayTGUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					KhachHang kh = new KhachHang(maKH, tenKH, dienThoai, gioiTinhBoolean, ngayThamGia);

					if (KHACH_HANG_SEVICE.save(kh)) {
						model.addRow(new Object[]{maKH, tenKH, dienThoai, gioiTinh, ngayThamGia});
						JOptionPane.showMessageDialog(null, "Đã thêm khách hàng!");
					} else {
						JOptionPane.showMessageDialog(null, "Thêm thất bại!");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng: " + ex.getMessage());
				}
			}
		});

		btnXoa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getSelectedRow();
					if (row == -1) {
						JOptionPane.showMessageDialog(null, "Chọn một dòng để xóa!");
						return;
					}
					String maKH = model.getValueAt(row, 0).toString();
					if (KHACH_HANG_SEVICE.delete(maKH)) {
						model.removeRow(row);
						JOptionPane.showMessageDialog(null, "Xóa thành công!");
					} else {
						JOptionPane.showMessageDialog(null, "Xóa thất bại!");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + ex.getMessage());
				}
			}
		});
		btnSua.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getSelectedRow();
					if (row == -1) {
						JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa");
						return;
					}

					String maKH = txtMaKH.getText().trim();
					String tenKH = txtTenKH.getText().trim();
					String dienThoai = txtDienThoai.getText().trim();
					String gioiTinh = cboGioiTinh.getSelectedItem().toString();
					boolean gioiTinhBoolean = gioiTinh.equals("Nữ");
					Date ngayTGUtil = dateChooser.getDate();

					if (!maKH.matches("KH\\d+") || !dienThoai.matches("^0\\d{9}$")) {
						JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ!");
						return;
					}

					LocalDate ngayThamGia = ngayTGUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					KhachHang kh = new KhachHang(maKH, tenKH, dienThoai, gioiTinhBoolean, ngayThamGia);

					if (KHACH_HANG_SEVICE.update(kh)) {
						model.setValueAt(tenKH, row, 1);
						model.setValueAt(dienThoai, row, 2);
						model.setValueAt(gioiTinh, row, 3);
						model.setValueAt(ngayThamGia, row, 4);
						JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
					} else {
						JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật: " + ex.getMessage());
				}
			}
		});

		btnThoat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}



	private void DocDuLieuDatabaseVaoTable() {
		try {
			model.setRowCount(0);
			for (KhachHang kh : KHACH_HANG_SEVICE.findAll()) {
				model.addRow(new Object[]{
						kh.getId(),
						kh.getHoTen(),
						kh.getSoDienThoai(),

						kh.isGioiTinh() ? "Nữ" : "Nam",
						kh.getNgayThamGia()
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
			txtMaKH.setText(model.getValueAt(row, 0).toString());
			txtTenKH.setText(model.getValueAt(row, 1).toString());
			txtDienThoai.setText(model.getValueAt(row, 2).toString());
			cboGioiTinh.setSelectedItem(model.getValueAt(row, 3).toString());
			LocalDate ngayThamGia = (LocalDate) model.getValueAt(row, 4);
			Date ngayTGUtil = Date.from(ngayThamGia.atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateChooser.setDate(ngayTGUtil);
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
