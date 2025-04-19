package dao.impl;

import dao.ChiTietPhieuNhapThuocDAO;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import jakarta.persistence.EntityManager;


import java.util.List;
import java.util.Optional;

public class ChiTietPhieuNhapThuocDAOImpl extends GenericDAOImpl<ChiTietPhieuNhapThuoc, ChiTietPhieuNhapThuoc.ChiTietPhieuNhapThuocID> implements ChiTietPhieuNhapThuocDAO {

    public ChiTietPhieuNhapThuocDAOImpl() {
        super(ChiTietPhieuNhapThuoc.class);
    }

    public ChiTietPhieuNhapThuocDAOImpl(EntityManager em) {
        super(em, ChiTietPhieuNhapThuoc.class);
    }

    @Override
    public List<ChiTietPhieuNhapThuoc> findByPhieuNhapThuoc(PhieuNhapThuoc phieuNhapThuoc) {
        try {
            return em.createQuery("SELECT c FROM ChiTietPhieuNhapThuoc c WHERE c.phieuNhapThuoc = :phieuNhapThuoc", ChiTietPhieuNhapThuoc.class)
                    .setParameter("phieuNhapThuoc", phieuNhapThuoc)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<ChiTietPhieuNhapThuoc> findByThuocAndPhieuNhapThuoc(Thuoc thuoc, PhieuNhapThuoc phieuNhapThuoc) {
        try {
            return em.createQuery("SELECT c FROM ChiTietPhieuNhapThuoc c WHERE c.thuoc = :thuoc AND c.phieuNhapThuoc = :phieuNhapThuoc", ChiTietPhieuNhapThuoc.class)
                    .setParameter("thuoc", thuoc)
                    .setParameter("phieuNhapThuoc", phieuNhapThuoc)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}