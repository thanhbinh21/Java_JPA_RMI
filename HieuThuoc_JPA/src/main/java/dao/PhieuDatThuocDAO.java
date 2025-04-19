package dao;

import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import java.util.List;

public interface PhieuDatThuocDAO extends GenericDAO<PhieuDatThuoc, String> {
    List<PhieuDatThuoc> findByKhachHang(KhachHang khachHang);

    List<PhieuDatThuoc> findByNhanVien(NhanVien nhanVien);

    boolean updateTT(String selectedMaPDT);
    // Các phương thức đặc thù cho PhieuDatThuocDAO nếu có
}