package dao;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

import java.time.LocalDate;
import java.util.List;

public interface HoaDonDAO extends GenericDAO<HoaDon, String> {
    List<HoaDon> findByKhachHang(KhachHang khachHang);
    List<HoaDon> findByNhanVien(NhanVien nhanVien);
    List<Object[]> getSoLuongHoaDonTheoKhachHang();

    List<HoaDon> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find a HoaDon by ID and eagerly load its chiTietHoaDons collection
     * @param id The ID of the HoaDon to find
     * @return The HoaDon with eagerly loaded chiTietHoaDons or null if not found
     */
    HoaDon findByIdWithDetails(String id);
}