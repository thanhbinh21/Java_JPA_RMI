package dao.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;


import java.util.List;

public class HoaDonDAOImpl extends GenericDAOImpl<HoaDon, String> implements HoaDonDAO {

    public HoaDonDAOImpl() {
        super(HoaDon.class);
    }

    @Override
    public HoaDon findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<HoaDon> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(HoaDon hoaDon) {
        return super.save(hoaDon);
    }

    @Override
    public boolean update(HoaDon hoaDon) {
        return super.update(hoaDon);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    public List<HoaDon> findByKhachHang(KhachHang khachHang) {
        try {
            return em.createQuery("SELECT h FROM HoaDon h WHERE h.khachHang = :khachHang", HoaDon.class)
                    .setParameter("khachHang", khachHang)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<HoaDon> findByNhanVien(NhanVien nhanVien) {
        try {
            return em.createQuery("SELECT h FROM HoaDon h WHERE h.nhanVien = :nhanVien", HoaDon.class)
                    .setParameter("nhanVien", nhanVien)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}