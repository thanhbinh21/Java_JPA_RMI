// service/HoaDonService.java
package service;

import entity.HoaDon;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HoaDonService {
    void themHoaDon(HoaDon hoaDon);
    Optional<HoaDon> timHoaDonTheoMa(String maHoaDon);
    List<HoaDon> timHoaDonTheoKhachHang(String maKhachHang);
    List<HoaDon> timHoaDonTheoNhanVien(String maNhanVien);
    List<HoaDon> timHoaDonTheoKhoangThoiGian(LocalDateTime tuNgay, LocalDateTime denNgay);
    List<HoaDon> layTatCaHoaDon();
    boolean capNhatHoaDon(HoaDon hoaDon);
    boolean xoaHoaDon(String maHoaDon);
}