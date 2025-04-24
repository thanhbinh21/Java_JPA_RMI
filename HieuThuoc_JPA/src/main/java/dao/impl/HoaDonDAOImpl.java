package dao.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.ChiTietHoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import until.JPAUtil;

import java.time.LocalDate;
import java.util.List;

public class HoaDonDAOImpl extends GenericDAOImpl<HoaDon, String> implements HoaDonDAO {

    public HoaDonDAOImpl() {
        super(HoaDon.class);
    }

    public HoaDonDAOImpl(EntityManager em) {
        super(em, HoaDon.class);
    }

    @Override
    public List<HoaDon> findByKhachHang(KhachHang khachHang) {
        Query query = em.createQuery("SELECT h FROM HoaDon h WHERE h.khachHang = :khachHang", HoaDon.class);
        query.setParameter("khachHang", khachHang);
        return query.getResultList();
    }

    @Override
    public List<HoaDon> findByNhanVien(NhanVien nhanVien) {
        Query query = em.createQuery("SELECT h FROM HoaDon h WHERE h.nhanVien = :nhanVien", HoaDon.class);
        query.setParameter("nhanVien", nhanVien);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getSoLuongHoaDonTheoKhachHang() {
        String query = "SELECT h.khachHang.id, COUNT(h) FROM HoaDon h GROUP BY h.khachHang.id";
        return em.createQuery(query, Object[].class).getResultList();
    }

    @Override
    public List<HoaDon> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            String jpql = "SELECT h FROM HoaDon h WHERE CAST(h.thoiGian AS LocalDate) BETWEEN :startDate AND :endDate";
            TypedQuery<HoaDon> query = em.createQuery(jpql, HoaDon.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error instead of null
        }
    }
    
    @Override
    public HoaDon findByIdWithDetails(String id) {
        // Use the current entity manager instead of creating a new one
        // This ensures we stay within the same persistence context
        try {
            // Use JOIN FETCH to eagerly load the chiTietHoaDons collection
            String jpql = "SELECT DISTINCT h FROM HoaDon h LEFT JOIN FETCH h.chiTietHoaDons WHERE h.id = :id";
            TypedQuery<HoaDon> query = em.createQuery(jpql, HoaDon.class);
            query.setParameter("id", id);
            
            HoaDon result = query.getSingleResult();
            
            // Force initialization of the collection while still in transaction
            if (result != null && result.getChiTietHoaDons() != null) {
                result.getChiTietHoaDons().size(); // Force initialization
            }
            
            return result;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ChiTietHoaDon> findChiTietByHoaDonId(String hoaDonId) {
        return em.createQuery(
                        "SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon.id = :hoaDonId", ChiTietHoaDon.class)
                .setParameter("hoaDonId", hoaDonId)
                .getResultList();
    }

}