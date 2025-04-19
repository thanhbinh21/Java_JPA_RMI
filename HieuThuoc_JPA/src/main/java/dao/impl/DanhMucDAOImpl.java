package dao.impl;

import dao.DanhMucDAO;
import entity.DanhMuc;
import jakarta.persistence.EntityManager;


import java.util.List;
import java.util.Optional;

public class DanhMucDAOImpl extends GenericDAOImpl<DanhMuc, String> implements DanhMucDAO {

    public DanhMucDAOImpl() {
        super(DanhMuc.class);
    }

    public DanhMucDAOImpl(EntityManager em) {
        super(em, DanhMuc.class);
    }
    @Override
    public List<DanhMuc> findByTenDanhMuc(String tenDanhMuc) {
        try {
            return em.createQuery("SELECT d FROM DanhMuc d WHERE d.ten = :tenDanhMuc", DanhMuc.class)
                    .setParameter("tenDanhMuc", tenDanhMuc)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}