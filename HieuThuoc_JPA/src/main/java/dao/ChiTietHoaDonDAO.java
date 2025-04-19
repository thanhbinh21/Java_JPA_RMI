package dao;

import entity.ChiTietHoaDon;
import java.util.List;

public interface ChiTietHoaDonDAO extends GenericDAO<ChiTietHoaDon, ChiTietHoaDon.ChiTietHoaDonID> {
    // Các phương thức đặc thù cho ChiTietHoaDonDAO nếu có
    List<ChiTietHoaDon> findByHoaDonId(ChiTietHoaDon.ChiTietHoaDonID hoaDonId);
}