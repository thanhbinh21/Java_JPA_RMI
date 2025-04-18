package dao.impl;

import dao.PhieuDatThuocDAO;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;


import java.util.List;
import java.util.Optional;

public class PhieuDatThuocDAOImpl extends GenericDAOImpl<PhieuDatThuoc, String> implements PhieuDatThuocDAO {

    public PhieuDatThuocDAOImpl() {
        super(PhieuDatThuoc.class);
    }

    @Override
    public PhieuDatThuoc findById(String maPhieuDat) {
        return super.findById(maPhieuDat);
    }

    @Override
    public List<PhieuDatThuoc> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(PhieuDatThuoc phieuDatThuoc) {
        return super.save(phieuDatThuoc);
    }

    @Override
    public boolean update(PhieuDatThuoc phieuDatThuoc) {
        return super.update(phieuDatThuoc);
    }

    @Override
    public boolean delete(String maPhieuDat) {
        return super.delete(maPhieuDat);
    }

    @Override
    public List<PhieuDatThuoc> findByKhachHang(KhachHang khachHang) {
        try {
            return em.createQuery("SELECT p FROM PhieuDatThuoc p WHERE p.khachHang = :khachHang", PhieuDatThuoc.class)
                    .setParameter("khachHang", khachHang)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<PhieuDatThuoc> findByNhanVien(NhanVien nhanVien) {
        try {
            return em.createQuery("SELECT p FROM PhieuDatThuoc p WHERE p.nhanVien = :nhanVien", PhieuDatThuoc.class)
                    .setParameter("nhanVien", nhanVien)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public boolean updateTT(String selectedMaPDT) {
        return false;
    }
}