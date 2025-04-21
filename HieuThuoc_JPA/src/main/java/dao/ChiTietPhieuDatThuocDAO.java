package dao;

import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import java.util.List;

public interface ChiTietPhieuDatThuocDAO extends GenericDAO<ChiTietPhieuDatThuoc, String> {
    List<ChiTietPhieuDatThuoc> findByPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc);


}