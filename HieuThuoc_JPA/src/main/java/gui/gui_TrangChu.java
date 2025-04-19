
package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


import entity.TaiKhoan;
import other.MessageDialog;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class gui_TrangChu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane, homePanel;
    public  gui_TrangChu(TaiKhoan login) {
    	
    	setForeground(SystemColor.activeCaptionBorder);
    	
        setIconImage(Toolkit.getDefaultToolkit().getImage(gui_TrangChu.class.getResource("/icon/QuanLyNhaThuoc.png")));
        setTitle("Yên Tâm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 10, 0, 10);
        setSize(1400,800);
       

		setLocationRelativeTo(null);
        
        homePanel = createHomePanel();

        // Đặt layout và panel trang chủ làm panel mặc định
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(UIManager.getColor("Button.light"));
        setContentPane(contentPane);
        setPanel(homePanel);  // Đặt trang chủ vào contentPane


        // Tạo JMenuBar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setForeground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
        menuBar.setBackground(SystemColor.scrollbar);
        menuBar.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        setJMenuBar(menuBar); // Đặt menuBar cho JFrame

        JMenu trangchu = new JMenu("Trang Chủ");
        trangchu.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        trangchu.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/home.png")));
        menuBar.add(trangchu);
        
        JMenuItem mni_quayve = new JMenuItem("Quay về ");
        mni_quayve.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setPanel(homePanel);
        	}
        });
        mni_quayve.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/return.png")));
        mni_quayve.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        mni_quayve.setBackground(SystemColor.scrollbar);
        trangchu.add(mni_quayve);
        
        JMenuItem mni_logout = new JMenuItem("Đăng xuất");
        mni_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui_TrangChu currentFrame = gui_TrangChu.this; 
                if (MessageDialog.confirm(currentFrame, "Đăng xuất tài khoản?", "Đồng Ý")) {
                    if (currentFrame != null) {
                        currentFrame.dispose();  
                    }
                    
                    Login loginFrame = new Login();  
                    loginFrame.getFrmlogin().setVisible(true);  
                }else {
                	return;
                }
            }
        });





        mni_logout.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/logout.png")));
        mni_logout.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        mni_logout.setBackground(SystemColor.scrollbar);
        trangchu.add(mni_logout);
        
        JMenu hethong = new JMenu("Hệ Thống");
        hethong.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/control-system.png")));
        hethong.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        menuBar.add(hethong);
        
        JMenuItem mni_phanquyen = new JMenuItem("Phân quyền tài khoản");
        mni_phanquyen.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/decentralized.png")));
        mni_phanquyen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//setPanel(new gui_taiKhoan());
        	}
        });
    
     
        mni_phanquyen.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        mni_phanquyen.setBackground(SystemColor.scrollbar);
        hethong.add(mni_phanquyen);
        
        JMenuItem mni_gioithieu = new JMenuItem("Hướng dẫn sử dụng");
        mni_gioithieu.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/symbols.png")));
        mni_gioithieu.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        mni_gioithieu.setBackground(SystemColor.scrollbar);
        hethong.add(mni_gioithieu);
       


        JMenu danhmuc = new JMenu("Danh Mục ");
        danhmuc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/classified-document.png")));
        danhmuc.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        menuBar.add(danhmuc);

        JMenu xuli = new JMenu("Xử Lý");
        xuli.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/profit.png")));
        xuli.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        menuBar.add(xuli);
        JMenuItem mni_khachhang = new JMenuItem("Khách hàng");
        mni_khachhang.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//setPanel(new gui_qliKhachHang());
        	}
        });
		mni_khachhang.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/client.png")));
		mni_khachhang.setBackground(SystemColor.scrollbar);
		mni_khachhang.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		danhmuc.add(mni_khachhang);
		
		JMenuItem mni_thuoc = new JMenuItem("Thuốc");
		mni_thuoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_qliThuoc());
			}
		});
		mni_thuoc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/medicine.png")));
		mni_thuoc.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_thuoc.setBackground(SystemColor.scrollbar);
		danhmuc.add(mni_thuoc);
		
		JMenuItem mni_nhacungcap = new JMenuItem("Nhà cung cấp");
		mni_nhacungcap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_qliNCC());
			}
		});
	
		mni_nhacungcap.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/supplier.png")));
		mni_nhacungcap.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_nhacungcap.setBackground(SystemColor.scrollbar);
		danhmuc.add(mni_nhacungcap);
//		
//		JMenuItem mni_khuyenmai = new JMenuItem("Khuyến mãi");
//		mni_khuyenmai.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				setPanel(new gui_qliKhuyenMai());
//			}
//		});
//		mni_khuyenmai.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/tag.png")));
//		mni_khuyenmai.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
//		mni_khuyenmai.setBackground(SystemColor.scrollbar);
//		danhmuc.add(mni_khuyenmai);
		
		JMenuItem mni_nhanvien = new JMenuItem("Nhân viên");
		mni_nhanvien.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_nhanVien());
			}
		});
		
				mni_nhanvien.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/employees.png")));
				mni_nhanvien.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
				mni_nhanvien.setBackground(SystemColor.scrollbar);
				danhmuc.add(mni_nhanvien);
				
				JMenuItem mni_nsx = new JMenuItem("Nhà Sản Xuất");
				mni_nsx.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//setPanel(new gui_qliNhaSX());
					}
				});
				mni_nsx.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/decentralized.png")));
				mni_nsx.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
				mni_nsx.setBackground(SystemColor.scrollbar);
				danhmuc.add(mni_nsx);
				
				JMenuItem mni_km = new JMenuItem("Khuyến Mãi");
				mni_km.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//setPanel(new gui_qliKhuyenMai());
					}
				});
				mni_km.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/tag.png")));
				mni_km.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
				mni_km.setBackground(SystemColor.scrollbar);
				danhmuc.add(mni_km);
		
		

		xuli.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		xuli.setBackground(SystemColor.textHighlightText);
		menuBar.add(xuli);
		
		JMenuItem mni_banthuoc = new JMenuItem("Bán thuốc");
		mni_banthuoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  gui_BanThuoc banthuoc = new gui_BanThuoc(login);
			        // Thay thế panel hiện tại bằng panel mới
			        setPanel(banthuoc);
			}
		});
	
		mni_banthuoc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/medicine.png")));
		mni_banthuoc.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_banthuoc.setBackground(SystemColor.scrollbar);
		xuli.add(mni_banthuoc);
		
		JMenuItem mni_traThuoc = new JMenuItem("Trả thuốc");
		mni_traThuoc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/bill.png")));
		mni_traThuoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_TraThuoc(login));
			}
		});
		
		JMenuItem mni_nhapdatthuoc = new JMenuItem("Đặt thuốc");
		mni_nhapdatthuoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_DatThuoc(login));
			}
		});
		mni_nhapdatthuoc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/fulfillment.png")));
		mni_nhapdatthuoc.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_nhapdatthuoc.setBackground(SystemColor.scrollbar);
		xuli.add(mni_nhapdatthuoc);
		mni_traThuoc.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_traThuoc.setBackground(SystemColor.scrollbar);
		xuli.add(mni_traThuoc);
		
		JMenuItem mnu_datthuoc = new JMenuItem("Nhập thuốc");
		mnu_datthuoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_nhapThuoc(login));
			}
		});
		mnu_datthuoc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/import.png")));
		mnu_datthuoc.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mnu_datthuoc.setBackground(SystemColor.scrollbar);
		xuli.add(mnu_datthuoc);
		
		JMenuItem mni_taokhuyenmai = new JMenuItem("Tạo khuyến mãi");
		mni_taokhuyenmai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_taoKhuyenMai());
			}
		});
		mni_taokhuyenmai.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/tag.png")));
		mni_taokhuyenmai.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_taokhuyenmai.setBackground(SystemColor.scrollbar);
		xuli.add(mni_taokhuyenmai);
		
		JMenu tracuu = new JMenu("Tìm Kiếm");
		tracuu.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/search.png")));
		tracuu.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		menuBar.add(tracuu);
		
		JMenuItem mni_khachhang_1 = new JMenuItem("Khách Hàng");
		mni_khachhang_1.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/client.png")));
		mni_khachhang_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_timKiemKhachHang());
			}
			
		});
		mni_khachhang_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_khachhang_1.setBackground(SystemColor.scrollbar);
		tracuu.add(mni_khachhang_1);
		
		JMenuItem mni_hoadon_1 = new JMenuItem("Hóa Đơn");
		mni_hoadon_1.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/bill.png")));
		mni_hoadon_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_qliHoaDon());
			}
		});
		
		JMenuItem mni_thuoc_1 = new JMenuItem("Thuốc");
		mni_thuoc_1.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/medicine.png")));
		mni_thuoc_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_timKiemThuoc());
			}
		});
		mni_thuoc_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_thuoc_1.setBackground(SystemColor.scrollbar);
		tracuu.add(mni_thuoc_1);
		
		mni_hoadon_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_hoadon_1.setBackground(SystemColor.scrollbar);
		
		tracuu.add(mni_hoadon_1);
		
		JMenuItem mni_phieudat = new JMenuItem("Phiếu Đặt");
		mni_phieudat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_timKiemPhieuDat(login));
			}
		});
		mni_phieudat.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/fulfillment.png")));
		mni_phieudat.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_phieudat.setBackground(SystemColor.scrollbar);
		tracuu.add(mni_phieudat);
		
		JMenuItem mni_phieuNhap = new JMenuItem("Phiếu Nhập");
		mni_phieuNhap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_timKiemPhieuNhap(login));
			}
		});
		mni_phieuNhap.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/import.png")));
		mni_phieuNhap.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_phieuNhap.setBackground(SystemColor.scrollbar);
		tracuu.add(mni_phieuNhap);
		
		JMenuItem mni_nhacungcap_1 = new JMenuItem("Nhà Cung Cấp");
		mni_nhacungcap_1.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/supplier.png")));
		mni_nhacungcap_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_timKiemNCC());
			}
		});
		
	
		mni_nhacungcap_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_nhacungcap_1.setBackground(SystemColor.scrollbar);
		tracuu.add(mni_nhacungcap_1);
		
		JMenuItem mni_khuyenmai_1 = new JMenuItem("Khuyến Mãi");
		mni_khuyenmai_1.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/tag.png")));
		mni_khuyenmai_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_timKiemKhuyenMai());
			}
		});
		mni_khuyenmai_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_khuyenmai_1.setBackground(SystemColor.scrollbar);
		tracuu.add(mni_khuyenmai_1);
		
		JMenu thongke = new JMenu("Thống Kê");
		thongke.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/bar-graph.png")));
		thongke.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		menuBar.add(thongke);
		
		JMenuItem mni_Khachhangthanthiet = new JMenuItem("Khách Hàng Thân Thiết");
		mni_Khachhangthanthiet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_thongKeKhachHang());
			}
		});
		mni_Khachhangthanthiet.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/reputation.png")));
		mni_Khachhangthanthiet.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_Khachhangthanthiet.setBackground(SystemColor.scrollbar);
		thongke.add(mni_Khachhangthanthiet);
		
		JMenuItem mni_doanhthu = new JMenuItem("Doanh Thu");
		mni_doanhthu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_thongKeDoanhThu(login));
			}
		});
		mni_doanhthu.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/revenue.png")));
		mni_doanhthu.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_doanhthu.setBackground(SystemColor.scrollbar);
		thongke.add(mni_doanhthu);
		
		JMenuItem mni_thuochh = new JMenuItem("Thuốc cận hạn");
		mni_thuochh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_thongKeThuoc());
			}
		});
		mni_thuochh.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/expired.png")));
		mni_thuochh.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_thuochh.setBackground(SystemColor.scrollbar);
		thongke.add(mni_thuochh);
		
		JMenuItem mni_topncc = new JMenuItem("Nhà cung cấp uy tín");
		mni_topncc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setPanel(new gui_thongKeNCC());
			}
		});
		mni_topncc.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/supplier (1).png")));
		mni_topncc.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		mni_topncc.setBackground(SystemColor.scrollbar);
		thongke.add(mni_topncc);
		
		JPanel panel_login = new JPanel();
		panel_login.setBackground(SystemColor.scrollbar);
		menuBar.add(panel_login);
		panel_login.setLayout(null);
		
		JLabel icon_login = new JLabel("");
		icon_login.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/user.png")));
		icon_login.setBounds(494, 0, 48, 36);
		panel_login.add(icon_login);

		// Thêm MouseListener để mở panel mới khi click vào icon
		icon_login.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		       
		        
		        // Tạo JFrame để chứa panel thông tin
		        JFrame infoFrame = new JFrame("THÔNG TIN CÁ NHÂN");
		        infoFrame.setSize(600, 400);
		        infoFrame.setLocationRelativeTo(null);
		        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng frame mà không dừng ứng dụng
		        
		        // Tạo panel thông tin login và truyền tài khoản
		        gui_thongtinlogin infoPanel = new gui_thongtinlogin(login);
		        
		        // Thêm panel vào frame
		        infoFrame.getContentPane().add(infoPanel);
		        infoFrame.setVisible(true);
		    }
		});



		
		JLabel lbl_login = new JLabel("NVA");
		lbl_login.setText(login.toString());
		lbl_login.setForeground(new Color(255, 20, 147));
		lbl_login.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lbl_login.setBounds(328, 0, 172, 36);
		panel_login.add(lbl_login);
		lbl_login.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		       
		        
		        // Tạo JFrame để chứa panel thông tin
		        JFrame infoFrame = new JFrame("THÔNG TIN CÁ NHÂN");
		        infoFrame.setSize(600, 400);
		        infoFrame.setLocationRelativeTo(null);
		        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng frame mà không dừng ứng dụng
		        
		        // Tạo panel thông tin login và truyền tài khoản
		        gui_thongtinlogin infoPanel = new gui_thongtinlogin(login);
		        
		        // Thêm panel vào frame
		        infoFrame.getContentPane().add(infoPanel);
		        infoFrame.setVisible(true);
		    }
		});

        // Thêm JLabel chứa ảnh nền vào contentPane bên dưới menu bar
        ImageIcon backgroundImage = new ImageIcon(gui_TrangChu.class.getResource("/icon/trangchu.png"));
        Image img = backgroundImage.getImage().getScaledInstance(1000, -1, Image.SCALE_SMOOTH); // -1 để giữ tỷ lệ chiều cao

        // Tạo lại ImageIcon từ ảnh đã scale
        ImageIcon scaledImageIcon = new ImageIcon(img);
        
        // set quyen 
        if (!"admin".equals(login.getVaiTro().getId())) {
            mni_phanquyen.setEnabled(false);
            mni_nhanvien.setEnabled(false);
            
        }

        
    }
    public void setPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        revalidate();
        repaint();
    }
    
    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setBackground(Color.WHITE);
        homePanel.setLayout(null);
        
        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon(gui_TrangChu.class.getResource("/icon/Nha thuoc.png")));
        lblNewLabel_1.setBounds(0, -62, 1437, 787);
        homePanel.add(lblNewLabel_1);
        
        JLabel title_1 = new JLabel("YÊN TÂM ", SwingConstants.CENTER);
        title_1.setForeground(Color.BLUE);
        title_1.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 60));
        title_1.setBounds(-272, 120, 849, 93);
        homePanel.add(title_1);
        
        JLabel lblNiauCa = new JLabel("NỖI ĐAU CỦA BẠN ", SwingConstants.CENTER);
        lblNiauCa.setForeground(Color.DARK_GRAY);
        lblNiauCa.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 30));
        lblNiauCa.setBounds(-294, 248, 849, 56);
        homePanel.add(lblNiauCa);
        
        JLabel lblNiLVn = new JLabel("VẤN ĐỀ CỦA CHÚNG TÔI ", SwingConstants.CENTER);
        lblNiLVn.setForeground(Color.PINK);
        lblNiLVn.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 38));
        lblNiLVn.setBounds(-184, 334, 849, 69);
        homePanel.add(lblNiLVn);

        return homePanel;
    }
}
