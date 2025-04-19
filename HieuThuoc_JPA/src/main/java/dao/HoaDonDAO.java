package dao;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

import java.util.List;

public interface HoaDonDAO extends GenericDAO<HoaDon, String> {
    List<HoaDon> findByKhachHang(KhachHang khachHang);
    List<HoaDon> findByNhanVien(NhanVien nhanVien);
    List<Object[]> getSoLuongHoaDonTheoKhachHang();
}