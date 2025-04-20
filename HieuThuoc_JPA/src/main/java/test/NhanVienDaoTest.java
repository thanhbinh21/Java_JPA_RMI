package test;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import dao.VaiTroDAO;
import dao.impl.NhanVienDAOImpl;
import dao.impl.TaiKhoanDAOImpl;
import dao.impl.VaiTroDAOImpl;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;
import service.NhanVienService;
import service.TaiKhoanService;
import service.VaiTroService;
import service.impl.NhanVienServiceImpl;
import service.impl.TaiKhoanServiceImpl;
import service.impl.VaiTroServiceImpl;

import java.time.LocalDate;

public class NhanVienDaoTest {
    public static void main(String[] args) {
        VaiTroDAO vaiTroDAO = new VaiTroDAOImpl();

//        VaiTro vt1 = new VaiTro();
//        vt1.setId("VT001");
//        vt1.setTenVaiTro("Quản Lý");
//
//        VaiTro vt2 = new VaiTro();
//        vt2.setId("VT002");
//        vt2.setTenVaiTro("Nhân viên");
//
//        vaiTroDAO.save(vt1);
//        vaiTroDAO.save(vt2);

        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAOImpl();
        NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();

//        TaiKhoan tk1 = new TaiKhoan();
//        tk1.setId("TK001");
//        tk1.setPassword("001");
//        tk1.setVaiTro(vaiTroDAO.findById("VT001"));
//        NhanVien nv1 = new NhanVien();
//        nv1.setId("NV001");
//        nv1.setHoTen("A");
//        nv1.setNamSinh(2024);
//        nv1.setSoDienThoai("1234567890");
//        nv1.setGioiTinh(true);
//        nv1.setNgayVaoLam(LocalDate.of(2024, 1, 1));
//        tk1.setNhanVien(nv1);
//        nv1.setTaiKhoan(tk1);
//
//        TaiKhoan tk2 = new TaiKhoan();
//        tk2.setId("TK002");
//        tk2.setPassword("002");
//        tk2.setVaiTro(vaiTroDAO.findById("VT002"));
//        NhanVien nv2 = new NhanVien();
//        nv2.setId("NV002");
//        nv2.setHoTen("B");
//        nv2.setNamSinh(2024);
//        nv2.setSoDienThoai("1234567890");
//        nv2.setGioiTinh(true);
//        nv2.setNgayVaoLam(LocalDate.of(2024, 1, 1));
//        tk2.setNhanVien(nv2);
//        nv2.setTaiKhoan(tk2);
//
//        taiKhoanDAO.save(tk1);
//        taiKhoanDAO.save(tk2);
//        nhanVienDAO.save(nv1);
//        nhanVienDAO.save(nv2);

        try {
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(new TaiKhoanDAOImpl());
            NhanVienService nhanVienService = new NhanVienServiceImpl(new NhanVienDAOImpl());
            VaiTroService vaiTroService = new VaiTroServiceImpl(new VaiTroDAOImpl());
//            TaiKhoan tk = new TaiKhoan("vn", "vn", nhanVienService.findByTen("Vũ Nương"), vaiTroService.findByTenVaiTro("Nhân viên Bán Thuốc"));
            TaiKhoan tk = taiKhoanService.findById("vn");
            tk.setPassword("vnabc");
            tk.setNhanVien(nhanVienService.findByTen("Vũ Nương"));
            System.out.println(taiKhoanService.update(tk));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
