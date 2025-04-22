package dao.impl;

import dao.ChiTietPhieuNhapThuocDAO;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


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
    
    /**
     * Insert ChiTietPhieuNhapThuoc using native SQL to avoid Hibernate session issues
     * @param phieuNhapId The PhieuNhapThuoc ID
     * @param thuocId The Thuoc ID
     * @param soLuong Quantity
     * @param donGia Unit price
     * @return true if successful, false otherwise
     */
    public boolean insertChiTietPhieuNhap(String phieuNhapId, String thuocId, int soLuong, double donGia) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            
            // Use native SQL query to insert directly into the database
            int rowsAffected = em.createNativeQuery(
                    "INSERT INTO ChiTietPhieuNhapThuoc (ma_phieu_nhap_thuoc, ma_thuoc, so_luong, don_gia) " +   
                    "VALUES (?, ?, ?, ?)")
                    .setParameter(1, phieuNhapId)
                    .setParameter(2, thuocId)
                    .setParameter(3, soLuong)
                    .setParameter(4, donGia)
                    .executeUpdate();
            
            tr.commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            tr.rollback();
            e.printStackTrace();
            return false;
        }
    }
}