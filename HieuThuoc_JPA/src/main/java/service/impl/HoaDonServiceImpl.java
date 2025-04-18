package service.impl;

import dao.HoaDonDAO;
import dao.impl.HoaDonDAOImpl;
import entity.HoaDon;
import service.HoaDonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();

    @Override
    public void themHoaDon(HoaDon hoaDon) {
        hoaDonDAO.save(hoaDon);
    }

    @Override
    public Optional<HoaDon> timHoaDonTheoMa(String maHoaDon) {
        return Optional.ofNullable(hoaDonDAO.findById(maHoaDon));
    }

    @Override
    public List<HoaDon> timHoaDonTheoKhachHang(String maKhachHang) {
       return null;
    }

    @Override
    public List<HoaDon> timHoaDonTheoNhanVien(String maNhanVien) {
        return null;
    }

    @Override
    public List<HoaDon> timHoaDonTheoKhoangThoiGian(LocalDateTime tuNgay, LocalDateTime denNgay) {
        return null;
    }

    @Override
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonDAO.findAll();
    }

    @Override
    public boolean capNhatHoaDon(HoaDon hoaDon) {
        return hoaDonDAO.update(hoaDon);
    }

    @Override
    public boolean xoaHoaDon(String maHoaDon) {
        return hoaDonDAO.delete(maHoaDon);
    }
}