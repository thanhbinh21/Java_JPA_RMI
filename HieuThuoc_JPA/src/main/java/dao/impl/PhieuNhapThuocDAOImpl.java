package dao.impl;

import dao.PhieuNhapThuocDAO;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;


import java.util.List;
import java.util.Optional;

public class PhieuNhapThuocDAOImpl extends GenericDAOImpl<PhieuNhapThuoc, String> implements PhieuNhapThuocDAO {

    public PhieuNhapThuocDAOImpl() {
        super(PhieuNhapThuoc.class);
    }

    @Override
    public PhieuNhapThuoc findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<PhieuNhapThuoc> findAll() {
        return super.findAll();
    }

    @Override
    public boolean save(PhieuNhapThuoc phieuNhapThuoc) {
        return super.save(phieuNhapThuoc);
    }

    @Override
    public boolean update(PhieuNhapThuoc phieuNhapThuoc) {
        return super.update(phieuNhapThuoc);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    public List<PhieuNhapThuoc> findByNhaCungCap(NhaCungCap nhaCungCap) {
        try {
            return em.createQuery("SELECT p FROM PhieuNhapThuoc p WHERE p.nhaCungCap = :nhaCungCap", PhieuNhapThuoc.class)
                    .setParameter("nhaCungCap", nhaCungCap)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<PhieuNhapThuoc> findByNhanVien(NhanVien nhanVien) {
        try {
            return em.createQuery("SELECT p FROM PhieuNhapThuoc p WHERE p.nhanVien = :nhanVien", PhieuNhapThuoc.class)
                    .setParameter("nhanVien", nhanVien)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}