package dao;

import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import entity.Thuoc;

import java.util.List;
import java.util.Optional;

public interface ChiTietPhieuDatThuocDAO {
    ChiTietPhieuDatThuoc findById(Long id);
    List<ChiTietPhieuDatThuoc> findAll();
    boolean save(ChiTietPhieuDatThuoc chiTietPhieuDatThuoc);
    boolean update(ChiTietPhieuDatThuoc chiTietPhieuDatThuoc);
    boolean delete(Long id);
    List<ChiTietPhieuDatThuoc> findByPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc);
    Optional<ChiTietPhieuDatThuoc> findByThuocAndPhieuDatThuoc(Thuoc thuoc, PhieuDatThuoc phieuDatThuoc);
}