package dao;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

import java.util.List;
import java.util.Optional;

public interface HoaDonDAO {
    HoaDon findById(String id);
    List<HoaDon> findAll();
    boolean save(HoaDon hoaDon);
    boolean update(HoaDon hoaDon);
    boolean delete(String id);
    List<HoaDon> findByKhachHang(KhachHang khachHang);
    List<HoaDon> findByNhanVien(NhanVien nhanVien);
}