package dao;

import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;

import java.util.List;
import java.util.Optional;

public interface ChiTietPhieuNhapThuocDAO extends GenericDAO<ChiTietPhieuNhapThuoc, ChiTietPhieuNhapThuoc.ChiTietPhieuNhapThuocID> {
    List<ChiTietPhieuNhapThuoc> findByPhieuNhapThuoc(PhieuNhapThuoc phieuNhapThuoc);
    Optional<ChiTietPhieuNhapThuoc> findByThuocAndPhieuNhapThuoc(Thuoc thuoc, PhieuNhapThuoc phieuNhapThuoc);
}