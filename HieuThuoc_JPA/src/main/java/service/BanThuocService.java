package service;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.Thuoc;

import java.util.List;
import java.util.Optional;

public interface BanThuocService {
    HoaDon banThuoc(KhachHang khachHang, NhanVien nhanVien, List<ChiTietHoaDon> chiTietHoaDons);
    Optional<Thuoc> timThuocTheoMa(String maThuoc);
    Optional<KhachHang> timKhachHangTheoId(String maKhachHang);
    Optional<KhachHang> timKhachHangTheoSoDienThoai(String soDienThoai);
}