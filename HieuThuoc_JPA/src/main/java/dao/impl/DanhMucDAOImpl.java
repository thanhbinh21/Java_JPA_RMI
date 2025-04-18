package dao.impl;

import dao.DanhMucDAO;
import entity.DanhMuc;


import java.util.List;
import java.util.Optional;

public class DanhMucDAOImpl extends GenericDAOImpl<DanhMuc, String> implements DanhMucDAO {

    public DanhMucDAOImpl() {
        super(DanhMuc.class);
    }

    @Override
    public DanhMuc findById(String maDanhMuc) {
        return super.findById(maDanhMuc);
    }

    @Override
    public List<DanhMuc> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(DanhMuc danhMuc) {
        return super.save(danhMuc);
    }

    @Override
    public boolean update(DanhMuc danhMuc) {
        return super.update(danhMuc);
    }

    @Override
    public boolean delete(String maDanhMuc) {
        return super.delete(maDanhMuc);
    }

    @Override
    public Optional<DanhMuc> findByTenDanhMuc(String tenDanhMuc) {
        try {
            return em.createQuery("SELECT d FROM DanhMuc d WHERE d.tenDanhMuc = :tenDanhMuc", DanhMuc.class)
                    .setParameter("tenDanhMuc", tenDanhMuc)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}