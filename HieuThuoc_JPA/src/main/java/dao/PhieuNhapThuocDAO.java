package dao;

import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;

import java.util.List;
import java.util.Optional;

public interface PhieuNhapThuocDAO {
    PhieuNhapThuoc findById(String id);
    List<PhieuNhapThuoc> findAll();
    boolean save(PhieuNhapThuoc phieuNhapThuoc);
    boolean update(PhieuNhapThuoc phieuNhapThuoc);
    boolean delete(String id);
    List<PhieuNhapThuoc> findByNhaCungCap(NhaCungCap nhaCungCap);
    List<PhieuNhapThuoc> findByNhanVien(NhanVien nhanVien);
}