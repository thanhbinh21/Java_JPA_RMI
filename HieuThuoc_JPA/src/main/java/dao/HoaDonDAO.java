package dao;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.ChiTietHoaDon;

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
    
    /**
     * Find all ChiTietHoaDon records for a specific HoaDon ID
     * This is a backup method in case the relationship loading fails
     * @param hoaDonId The ID of the HoaDon to find details for
     * @return A list of ChiTietHoaDon objects associated with the given HoaDon ID
     */
    List<ChiTietHoaDon> findChiTietByHoaDonId(String hoaDonId);
}