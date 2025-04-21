package dao.impl;

import dao.ChiTietPhieuDatThuocDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import entity.Thuoc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


import java.util.List;
import java.util.Optional;

public class ChiTietPhieuDatThuocDAOImpl extends GenericDAOImpl<ChiTietPhieuDatThuoc, String> implements ChiTietPhieuDatThuocDAO {

    public ChiTietPhieuDatThuocDAOImpl() {
        super(ChiTietPhieuDatThuoc.class);
    }
    public ChiTietPhieuDatThuocDAOImpl(EntityManager em) {
        super(em, ChiTietPhieuDatThuoc.class);
    }
    @Override
    public List<ChiTietPhieuDatThuoc> findByPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) {
        try {
            return em.createQuery("SELECT c FROM ChiTietPhieuDatThuoc c WHERE c.phieuDatThuoc = :phieuDatThuoc", ChiTietPhieuDatThuoc.class)
                    .setParameter("phieuDatThuoc", phieuDatThuoc)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }



//    @Override
//    public Optional<ChiTietPhieuDatThuoc> findByThuocAndPhieuDatThuoc(Thuoc thuoc, PhieuDatThuoc phieuDatThuoc) {
//        try {
//            return em.createQuery("SELECT c FROM ChiTietPhieuDatThuoc c WHERE c.thuoc = :thuoc AND c.phieuDatThuoc = :phieuDatThuoc", ChiTietPhieuDatThuoc.class)
//                    .setParameter("thuoc", thuoc)
//                    .setParameter("phieuDatThuoc", phieuDatThuoc)
//                    .getResultList()
//                    .stream()
//                    .findFirst();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }
}