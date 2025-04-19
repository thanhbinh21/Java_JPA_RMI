package dao.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
}