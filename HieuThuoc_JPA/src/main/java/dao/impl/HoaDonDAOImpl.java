package dao.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

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
        // Create a JPQL query that eagerly fetches chiTietHoaDons
        String jpql = "SELECT h FROM HoaDon h LEFT JOIN FETCH h.chiTietHoaDons WHERE h.id = :id";
        TypedQuery<HoaDon> query = em.createQuery(jpql, HoaDon.class);
        query.setParameter("id", id);
        
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            // Handle the case where no result is found or multiple results are found
            e.printStackTrace();
            return null;
        }
    }
}