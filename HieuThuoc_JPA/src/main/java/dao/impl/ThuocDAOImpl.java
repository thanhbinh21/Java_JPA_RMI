package dao.impl;

import dao.ThuocDAO;
import entity.DanhMuc;
import entity.Thuoc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.sql.Date;
import java.util.List;

public class ThuocDAOImpl extends GenericDAOImpl<Thuoc, String> implements ThuocDAO {

    public ThuocDAOImpl() {
        super(Thuoc.class);
    }

    public ThuocDAOImpl(EntityManager em) {
        super(em, Thuoc.class);
    }

    @Override
    public List<Thuoc> selectByDanhMuc(DanhMuc danhMuc) {
        Query query = em.createQuery("SELECT t FROM Thuoc t WHERE t.danhMuc = :danhMuc", Thuoc.class);
        query.setParameter("danhMuc", danhMuc);
        return query.getResultList();
    }

    @Override
    public List<Thuoc> searchByKeyword(String keyword) {
        Query query = em.createQuery(
                "SELECT t FROM Thuoc t WHERE LOWER(t.id) LIKE LOWER(:keyword) OR LOWER(t.ten) LIKE LOWER(:keyword) OR LOWER(t.thanhPhan) LIKE LOWER(:keyword)",
                Thuoc.class
        );
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
    
    @Override
    public Thuoc getReference(String id) {
        // Get a reference to the entity without loading its collections
        return em.getReference(Thuoc.class, id);
    }

    @Override
    public List<Thuoc> findSoldMedicines(Date ngayBatDau, Date ngayKetThuc) {
        return List.of();
    }

//    @Override
//    public List<Object[]> getMaTenThuoc() {
//        String query = "SELECT t.maThuoc, t.tenThuoc FROM Thuoc t";
//        return em.createQuery(query, Object[].class).getResultList();
//    }
}