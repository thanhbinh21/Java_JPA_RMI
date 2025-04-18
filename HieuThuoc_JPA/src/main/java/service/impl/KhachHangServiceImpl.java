// service/impl/KhachHangServiceImpl.java
package service.impl;

import dao.KhachHangDAO;
import dao.impl.KhachHangDAOImpl;
import entity.KhachHang;
import service.KhachHangService;

import java.util.List;
import java.util.Optional;

public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangDAO khachHangDAO = new KhachHangDAOImpl();

    @Override
    public void themKhachHang(KhachHang khachHang) {
        khachHangDAO.save(khachHang);
    }

    @Override
    public Optional<KhachHang> timKhachHangTheoId(String id) {
        return Optional.ofNullable(khachHangDAO.findById(id));
    }

    @Override
    public Optional<KhachHang> timKhachHangTheoSoDienThoai(String soDienThoai) {
        return Optional.ofNullable(khachHangDAO.findBySoDienThoai(soDienThoai));
    }

    @Override
    public List<KhachHang> layTatCaKhachHang() {
        return khachHangDAO.findAll();
    }

    @Override
    public boolean capNhatKhachHang(KhachHang khachHang) {
        return khachHangDAO.update(khachHang);
    }

    @Override
    public boolean xoaKhachHang(String id) {
        return khachHangDAO.delete(id);
    }
}