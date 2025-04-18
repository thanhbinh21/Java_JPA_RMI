package service.impl;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.ThuocDAO;
import dao.impl.ChiTietHoaDonDAOImpl;
import dao.impl.HoaDonDAOImpl;
import dao.impl.KhachHangDAOImpl;
import dao.impl.ThuocDAOImpl;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.Thuoc;
import service.BanThuocService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BanThuocServiceImpl implements BanThuocService {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAOImpl();
    private final ThuocDAO thuocDAO = new ThuocDAOImpl();
    private final KhachHangDAO khachHangDAO = new KhachHangDAOImpl();

    @Override
    public HoaDon banThuoc(KhachHang khachHang, NhanVien nhanVien, List<ChiTietHoaDon> chiTietHoaDons) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setThoiGian(Timestamp.valueOf(LocalDateTime.now()));
        hoaDonDAO.save(hoaDon);

        for (ChiTietHoaDon chiTiet : chiTietHoaDons) {
            chiTiet.setHoaDon(hoaDon);
            chiTietHoaDonDAO.save(chiTiet);

            Optional<Thuoc> thuocOptional = Optional.ofNullable(thuocDAO.findById(chiTiet.getThuoc().getId()));
            thuocOptional.ifPresent(thuoc -> {
                int soLuongBan = chiTiet.getSoLuong();
                int soLuongHienCo = thuoc.getSoLuongTon();
                if (soLuongHienCo >= soLuongBan) {
                    thuoc.setSoLuongTon(soLuongHienCo - soLuongBan);
                    thuocDAO.update(thuoc);
                } else {
                    System.err.println("Không đủ số lượng thuốc: " + thuoc.getTen());
                    // Xử lý lỗi thực tế cần được triển khai cẩn thận
                }
            });
        }
        return hoaDon;
    }

    @Override
    public Optional<Thuoc> timThuocTheoMa(String maThuoc) {
        return Optional.ofNullable(thuocDAO.findById(maThuoc));
    }

    @Override
    public Optional<KhachHang> timKhachHangTheoId(String maKhachHang) {
        return Optional.ofNullable(khachHangDAO.findById(maKhachHang));
    }

    @Override
    public Optional<KhachHang> timKhachHangTheoSoDienThoai(String soDienThoai) {
        return Optional.ofNullable(khachHangDAO.findBySoDienThoai(soDienThoai));
    }
}