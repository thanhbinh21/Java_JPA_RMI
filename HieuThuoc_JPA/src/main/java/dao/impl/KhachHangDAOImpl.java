package dao.impl;

import dao.KhachHangDAO;
import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class KhachHangDAOImpl extends GenericDAOImpl<KhachHang, String> implements KhachHangDAO {

    public KhachHangDAOImpl() {
        super(KhachHang.class);
    }

    public KhachHangDAOImpl(EntityManager em) {
        super(em, KhachHang.class);
    }

    @Override
    public KhachHang selectBySdt(String sdt) {
        Query query = em.createQuery("SELECT kh FROM KhachHang kh WHERE kh.soDienThoai = :sdt", KhachHang.class);
        query.setParameter("sdt", sdt);
        List<KhachHang> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}