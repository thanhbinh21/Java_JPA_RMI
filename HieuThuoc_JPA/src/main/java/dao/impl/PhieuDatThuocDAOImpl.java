package dao.impl;

import dao.PhieuDatThuocDAO;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import jakarta.persistence.EntityManager;


import java.util.List;

public class PhieuDatThuocDAOImpl extends GenericDAOImpl<PhieuDatThuoc, String> implements PhieuDatThuocDAO {

    public PhieuDatThuocDAOImpl() {
        super(PhieuDatThuoc.class);
    }

    public PhieuDatThuocDAOImpl(EntityManager em) {
        super(em, PhieuDatThuoc.class);
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
        try {
            em.getTransaction().begin();
            PhieuDatThuoc phieuDatThuoc = em.find(PhieuDatThuoc.class, selectedMaPDT);
            if (phieuDatThuoc != null) {
                phieuDatThuoc.setTrangThai(true);
                em.merge(phieuDatThuoc);
                em.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
        return false;
    }
}