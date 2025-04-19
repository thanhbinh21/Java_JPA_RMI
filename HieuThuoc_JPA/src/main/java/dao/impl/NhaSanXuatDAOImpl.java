package dao.impl;

import dao.NhaSanXuatDAO;
import entity.NhaSanXuat;
import jakarta.persistence.EntityManager;


import java.util.List;
import java.util.Optional;

public class NhaSanXuatDAOImpl extends GenericDAOImpl<NhaSanXuat, String> implements NhaSanXuatDAO {

    public NhaSanXuatDAOImpl() {
        super(NhaSanXuat.class);
    }

    public NhaSanXuatDAOImpl(EntityManager em) {
        super(em, NhaSanXuat.class);
    }

    @Override
    public NhaSanXuat findById(String maNhaSanXuat) {
        return super.findById(maNhaSanXuat);
    }

    @Override
    public List<NhaSanXuat> findAll() {
        return super.findAll();
    }

    @Override
    public boolean save(NhaSanXuat nhaSanXuat) {
        return super.save(nhaSanXuat);
    }

    @Override
    public boolean update(NhaSanXuat nhaSanXuat) {
        return super.update(nhaSanXuat);
    }

    @Override
    public boolean delete(String maNhaSanXuat) {
        return super.delete(maNhaSanXuat);
    }

    @Override
    public NhaSanXuat findByTenNhaSanXuat(String tenNhaSanXuat) {
        try {
            return em.createQuery("SELECT n FROM NhaSanXuat n WHERE n.ten = :tenNhaSanXuat", NhaSanXuat.class)
                    .setParameter("tenNhaSanXuat", tenNhaSanXuat)
                    .getResultList()
                    .get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}