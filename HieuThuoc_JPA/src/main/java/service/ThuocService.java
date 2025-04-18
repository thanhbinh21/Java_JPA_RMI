// service/ThuocService.java
package service;

import entity.Thuoc;
import java.util.List;
import java.util.Optional;

public interface ThuocService {
    void themThuoc(Thuoc thuoc);
    Optional<Thuoc> timThuocTheoMa(String maThuoc);
    Optional<Thuoc> timThuocTheoTen(String tenThuoc);
    List<Thuoc> layTatCaThuoc();
    boolean capNhatThuoc(Thuoc thuoc);
    boolean xoaThuoc(String maThuoc);
    List<Thuoc> timThuocTheoDanhMuc(String maDanhMuc);
    Optional<Thuoc> timThuocTheoNhaSanXuat(String maNhaSanXuat);
}