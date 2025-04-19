package dao.impl;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class TaiKhoanDAOImpl extends GenericDAOImpl<TaiKhoan, String> implements TaiKhoanDAO {

    public TaiKhoanDAOImpl() {
        super(TaiKhoan.class);
    }

    public TaiKhoanDAOImpl(EntityManager em) {
        super(em, TaiKhoan.class);
    }

    @Override
    public TaiKhoan findByIdAndPassword(String id, String password) {
        Query query = em.createQuery("SELECT tk FROM TaiKhoan tk WHERE tk.id = :id AND tk.password = :password", TaiKhoan.class);
        query.setParameter("id", id);
        query.setParameter("password", password);
        List<TaiKhoan> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}