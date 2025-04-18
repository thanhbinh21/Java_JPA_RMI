package dao;

import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;

import java.util.List;
import java.util.Optional;

public interface ChiTietPhieuNhapThuocDAO {
    ChiTietPhieuNhapThuoc findById(Long id);
    List<ChiTietPhieuNhapThuoc> findAll();
    boolean save(ChiTietPhieuNhapThuoc chiTietPhieuNhapThuoc);
    boolean update(ChiTietPhieuNhapThuoc chiTietPhieuNhapThuoc);
    boolean delete(Long id);
    List<ChiTietPhieuNhapThuoc> findByPhieuNhapThuoc(PhieuNhapThuoc phieuNhapThuoc);
    Optional<ChiTietPhieuNhapThuoc> findByThuocAndPhieuNhapThuoc(Thuoc thuoc, PhieuNhapThuoc phieuNhapThuoc);
}