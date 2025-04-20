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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.DanhMucDAO;
import dao.impl.DanhMucDAOImpl;
import entity.DanhMuc;
import service.DanhMucService;


public class gui_qliDanhMuc extends JPanel implements MouseListener {
	private JTextField txtMaDM, txtTenDM;
	private JComboBox<String> cboViTriKe;
	private JButton btnThem, btnXoa, btnSua, btnThoat;
	private JTable table;
	private DefaultTableModel model;
	private DanhMucService DM_SERVICE;

	public gui_qliDanhMuc() throws RemoteException {
		try {
			Registry registry = LocateRegistry.getRegistry(8989);
			DM_SERVICE = (DanhMucService) registry.lookup("DanhMucService");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Không thể kết nối đến server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		setLayout(new BorderLayout());

		JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC", JLabel.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitle.setForeground(new Color(0, 0, 0));

		JPanel pnTitle = new JPanel();
		pnTitle.setLayout(new BoxLayout(pnTitle, BoxLayout.Y_AXIS));
		pnTitle.add(lblTitle);
		pnTitle.add(Box.createRigidArea(new Dimension(0, 50)));
		pnTitle.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		JPanel pnInfo = new JPanel(new GridLayout(3, 4, 5, 5));
		pnInfo.add(new JLabel("Mã Danh Mục:"));
		txtMaDM = new JTextField();
		pnInfo.add(txtMaDM);

		pnInfo.add(new JLabel("Tên Danh Mục:"));
		txtTenDM = new JTextField();
		pnInfo.add(txtTenDM);

		pnInfo.add(new JLabel("Vị Trí Kệ:"));
		cboViTriKe = new JComboBox<>();
		pnInfo.add(cboViTriKe);

		JPanel pnTitleAndInfo = new JPanel();
		pnTitleAndInfo.setLayout(new BoxLayout(pnTitleAndInfo, BoxLayout.Y_AXIS));
		pnTitleAndInfo.add(pnTitle);
		pnTitleAndInfo.add(pnInfo);
		pnTitleAndInfo.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		add(pnTitleAndInfo, BorderLayout.NORTH);

		JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		btnThem = new JButton("Thêm Danh Mục");
		btnXoa = new JButton("Xóa Danh Mục");
		btnSua = new JButton("Sửa Thông Tin");
		btnThoat = new JButton("Thoát");

		pnButtons.add(btnThem);
		pnButtons.add(btnXoa);
		pnButtons.add(btnSua);
		pnButtons.add(btnThoat);

		JPanel pnButtonsWrapper = new JPanel();
		pnButtonsWrapper.setLayout(new BoxLayout(pnButtonsWrapper, BoxLayout.Y_AXIS));
		pnButtonsWrapper.add(Box.createRigidArea(new Dimension(0, 50)));
		pnButtonsWrapper.add(pnButtons);

		add(pnButtonsWrapper, BorderLayout.CENTER);

		String[] columnNames = { "Mã Danh Mục", "Tên Danh Mục", "Vị Trí Kệ" };

		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Danh Mục"));
		add(scrollPane, BorderLayout.SOUTH);
		
		DocDuLieuVaoCombobox();
		DocDuLieuDatabaseVaoTable();

		table.addMouseListener(this);
		pnInfo.addMouseListener(this);
		pnTitle.addMouseListener(this);
		pnButtonsWrapper.addMouseListener(this);
		pnButtons.addMouseListener(this);
		pnTitleAndInfo.addMouseListener(this);
		scrollPane.addMouseListener(this);

		btnThem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String maDM = txtMaDM.getText().trim();
				String tenDM = txtTenDM.getText().trim();
				String viTriKe = cboViTriKe.getSelectedItem().toString();

				if (maDM.isEmpty() || tenDM.isEmpty() || viTriKe.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
					return;
				}

				DanhMuc dm = new DanhMuc(maDM, tenDM, viTriKe);

				try {
					if(!DM_SERVICE.save(dm))
						throw new Exception();

					model.addRow(new Object[] { dm.getId(), dm.getTen(), dm.getViTriKe() });
					JOptionPane.showMessageDialog(null, "Đã thêm danh mục");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Lỗi khi thêm danh mục");
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
					if (row >= 0) {
						String maDanhMuc = (String) model.getValueAt(row, 0);
						int option = JOptionPane.showOptionDialog(null, "Bạn có chắc muốn xóa không?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

						if (option != JOptionPane.YES_OPTION)return;

						if (DM_SERVICE.delete(maDanhMuc)) {
							model.removeRow(row);
							txtMaDM.setEnabled(true);
							JOptionPane.showMessageDialog(null, "Đã xóa danh mục");
						} else {
							JOptionPane.showMessageDialog(null, "Lỗi khi xóa danh mục");
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi xóa danh mục: " + ex.getMessage());
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
					if (row >= 0) {
						String maDM = txtMaDM.getText().trim();
						String tenDM = txtTenDM.getText().trim();
						String viTriKe = cboViTriKe.getSelectedItem().toString();

						if (maDM.isEmpty() || tenDM.isEmpty() || viTriKe.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
							return;
						}

						DanhMuc dm = new DanhMuc(maDM, tenDM, viTriKe);

						if (DM_SERVICE.update(dm)) {
							model.setValueAt(maDM, row, 0);
							model.setValueAt(tenDM, row, 1);
							model.setValueAt(viTriKe, row, 2);

							JOptionPane.showMessageDialog(null, "Đã cập nhật danh mục");
						} else {
							JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật danh mục");
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi cập nhật danh mục: " + ex.getMessage());
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
	
	public void DocDuLieuVaoCombobox() throws RemoteException {
		ArrayList<DanhMuc> dsDanhMuc = new ArrayList<>(DM_SERVICE.findAll());
		for (DanhMuc dm : dsDanhMuc) {
			cboViTriKe.addItem(dm.getViTriKe());
		}
		}

	private void DocDuLieuDatabaseVaoTable() throws RemoteException {
		ArrayList<DanhMuc> list = new ArrayList<>(DM_SERVICE.findAll());
		model.setRowCount(0);

		for (DanhMuc dm : list) {
			model.addRow(new Object[] { dm.getId(), dm.getTen(), dm.getViTriKe() });
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!table.equals(e.getSource())) {
			txtMaDM.setEnabled(true);
			table.getSelectionModel().clearSelection();
			if(e.getClickCount() == 2) {
				txtMaDM.setText(null);
				txtTenDM.setText(null);
				cboViTriKe.setSelectedIndex(0);
				txtMaDM.requestFocus();
			}
			return;
		}
		txtMaDM.setEnabled(false);
		int row = table.getSelectedRow();
		if (row >= 0) {
			txtMaDM.setText(model.getValueAt(row, 0).toString());
			txtTenDM.setText(model.getValueAt(row, 1).toString());
			cboViTriKe.setSelectedItem(model.getValueAt(row, 2).toString());
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

	public static void main(String[] args) throws RemoteException {
		JFrame frame = new JFrame("Quản Lý Danh Mục");
		gui_qliDanhMuc panel = new gui_qliDanhMuc();
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1400, 900);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

}
