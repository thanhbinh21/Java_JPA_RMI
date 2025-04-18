package dao;

import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;

import java.util.List;

public interface PhieuDatThuocDAO {
    PhieuDatThuoc findById(String maPhieuDat);
    List<PhieuDatThuoc> findAll();
    boolean save(PhieuDatThuoc phieuDatThuoc);
    boolean update(PhieuDatThuoc phieuDatThuoc);
    boolean delete(String maPhieuDat);
    List<PhieuDatThuoc> findByKhachHang(KhachHang khachHang);
    List<PhieuDatThuoc> findByNhanVien(NhanVien nhanVien);

    boolean updateTT(String selectedMaPDT);
}