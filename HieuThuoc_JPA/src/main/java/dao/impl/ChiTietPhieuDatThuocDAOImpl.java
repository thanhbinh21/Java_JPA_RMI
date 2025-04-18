package dao.impl;

import dao.ChiTietPhieuDatThuocDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import entity.Thuoc;


import java.util.List;
import java.util.Optional;

public class ChiTietPhieuDatThuocDAOImpl extends GenericDAOImpl<ChiTietPhieuDatThuoc, Long> implements ChiTietPhieuDatThuocDAO {

    public ChiTietPhieuDatThuocDAOImpl() {
        super(ChiTietPhieuDatThuoc.class);
    }

    @Override
    public ChiTietPhieuDatThuoc findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<ChiTietPhieuDatThuoc> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(ChiTietPhieuDatThuoc chiTietPhieuDatThuoc) {
        return super.save(chiTietPhieuDatThuoc);
    }

    @Override
    public boolean update(ChiTietPhieuDatThuoc chiTietPhieuDatThuoc) {
        return super.update(chiTietPhieuDatThuoc);
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id);
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

    @Override
    public Optional<ChiTietPhieuDatThuoc> findByThuocAndPhieuDatThuoc(Thuoc thuoc, PhieuDatThuoc phieuDatThuoc) {
        try {
            return em.createQuery("SELECT c FROM ChiTietPhieuDatThuoc c WHERE c.thuoc = :thuoc AND c.phieuDatThuoc = :phieuDatThuoc", ChiTietPhieuDatThuoc.class)
                    .setParameter("thuoc", thuoc)
                    .setParameter("phieuDatThuoc", phieuDatThuoc)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}