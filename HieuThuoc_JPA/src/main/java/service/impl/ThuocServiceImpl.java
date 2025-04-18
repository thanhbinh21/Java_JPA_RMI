package service.impl;

import dao.ThuocDAO;
import dao.impl.ThuocDAOImpl;
import entity.NhaSanXuat;
import entity.Thuoc;
import service.ThuocService;

import java.util.List;
import java.util.Optional;

public class ThuocServiceImpl implements ThuocService {

    private final ThuocDAO thuocDAO = new ThuocDAOImpl();

    @Override
    public void themThuoc(Thuoc thuoc) {
        thuocDAO.save(thuoc);
    }

    @Override
    public Optional<Thuoc> timThuocTheoMa(String maThuoc) {
        return Optional.ofNullable(thuocDAO.findById(maThuoc));
    }

    @Override
    public Optional<Thuoc> timThuocTheoTen(String tenThuoc) {
        return thuocDAO.findByTenThuoc(tenThuoc);
    }

    @Override
    public List<Thuoc> layTatCaThuoc() {
        return thuocDAO.findAll();
    }

    @Override
    public boolean capNhatThuoc(Thuoc thuoc) {
        return thuocDAO.update(thuoc);
    }

    @Override
    public boolean xoaThuoc(String maThuoc) {
        return thuocDAO.delete(maThuoc);
    }

    @Override
    public List<Thuoc> timThuocTheoDanhMuc(String maDanhMuc) {
        return thuocDAO.findByDanhMuc(maDanhMuc);
    }

    @Override
    public Optional<Thuoc> timThuocTheoNhaSanXuat(String maNhaSanXuat) {
        return Optional.empty();
    }

    public List<Thuoc> timThuocTheoNhaSanXuat(NhaSanXuat maNhaSanXuat) {
        return thuocDAO.findByNhaSanXuat(maNhaSanXuat);
    }
}