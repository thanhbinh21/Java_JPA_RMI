package dao;

import entity.ChiTietHoaDon;
import entity.HoaDon;

import java.util.List;
import java.util.Optional;

public interface ChiTietHoaDonDAO {
    ChiTietHoaDon findById(Long id);
    List<ChiTietHoaDon> findAll();
    boolean save(ChiTietHoaDon chiTietHoaDon);
    boolean update(ChiTietHoaDon chiTietHoaDon);
    boolean delete(Long id);
    List<ChiTietHoaDon> findByHoaDon(HoaDon hoaDon);
}