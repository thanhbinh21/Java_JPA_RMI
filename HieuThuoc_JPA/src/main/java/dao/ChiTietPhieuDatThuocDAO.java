package dao;

import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import java.util.List;

public interface ChiTietPhieuDatThuocDAO extends GenericDAO<ChiTietPhieuDatThuoc, ChiTietPhieuDatThuoc.ChiTietPhieuDatThuocID> {
    List<ChiTietPhieuDatThuoc> findByPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc);
}