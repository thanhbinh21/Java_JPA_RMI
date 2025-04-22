package dao.impl;

import dao.PhieuDatThuocDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


import java.rmi.RemoteException;
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
    public List<PhieuDatThuoc> findBySdt(String sdt) {
        try {
            return em.createQuery("SELECT p FROM PhieuDatThuoc p WHERE p.khachHang.soDienThoai = :sdt and p.trangThai = false ", PhieuDatThuoc.class)
                    .setParameter("sdt", sdt)
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
    @Override
    public boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) throws RemoteException {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(phieuDatThuoc);

            if (phieuDatThuoc.getChiTietPhieuDatThuocs() != null) {
                for (ChiTietPhieuDatThuoc chiTiet : phieuDatThuoc.getChiTietPhieuDatThuocs()) {
                    chiTiet.setPhieuDatThuoc(phieuDatThuoc);
                    em.persist(chiTiet);
                }
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}