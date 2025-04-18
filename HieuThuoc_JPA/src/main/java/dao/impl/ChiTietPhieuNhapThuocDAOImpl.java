package dao.impl;

import dao.ChiTietPhieuNhapThuocDAO;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;


import java.util.List;
import java.util.Optional;

public class ChiTietPhieuNhapThuocDAOImpl extends GenericDAOImpl<ChiTietPhieuNhapThuoc, Long> implements ChiTietPhieuNhapThuocDAO {

    public ChiTietPhieuNhapThuocDAOImpl() {
        super(ChiTietPhieuNhapThuoc.class);
    }

    @Override
    public ChiTietPhieuNhapThuoc findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<ChiTietPhieuNhapThuoc> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(ChiTietPhieuNhapThuoc chiTietPhieuNhapThuoc) {
        return super.save(chiTietPhieuNhapThuoc);
    }

    @Override
    public boolean update(ChiTietPhieuNhapThuoc chiTietPhieuNhapThuoc) {
        return super.update(chiTietPhieuNhapThuoc);
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id);
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