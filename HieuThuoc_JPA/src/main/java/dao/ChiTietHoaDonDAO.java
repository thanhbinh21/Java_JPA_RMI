package dao;

import entity.ChiTietHoaDon;
import java.util.List;

public interface ChiTietHoaDonDAO extends GenericDAO<ChiTietHoaDon, String> {
    // Các phương thức đặc thù cho ChiTietHoaDonDAO nếu có
    List<ChiTietHoaDon> findByHoaDonId(String hoaDonId);
}