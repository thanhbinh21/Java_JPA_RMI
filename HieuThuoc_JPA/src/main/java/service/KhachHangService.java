// service/KhachHangService.java
package service;

import entity.KhachHang;
import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    void themKhachHang(KhachHang khachHang);
    Optional<KhachHang> timKhachHangTheoId(String id);
    Optional<KhachHang> timKhachHangTheoSoDienThoai(String soDienThoai);
    List<KhachHang> layTatCaKhachHang();
    boolean capNhatKhachHang(KhachHang khachHang);
    boolean xoaKhachHang(String id);
}