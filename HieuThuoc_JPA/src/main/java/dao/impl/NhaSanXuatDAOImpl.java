package dao.impl;

import dao.NhaSanXuatDAO;
import entity.NhaSanXuat;


import java.util.List;
import java.util.Optional;

public class NhaSanXuatDAOImpl extends GenericDAOImpl<NhaSanXuat, String> implements NhaSanXuatDAO {

    public NhaSanXuatDAOImpl() {
        super(NhaSanXuat.class);
    }

    @Override
    public NhaSanXuat findById(String maNhaSanXuat) {
        return super.findById(maNhaSanXuat);
    }

    @Override
    public List<NhaSanXuat> findAll() {
        return super.getAll();
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
    public Optional<NhaSanXuat> findByTenNhaSanXuat(String tenNhaSanXuat) {
        try {
            return em.createQuery("SELECT n FROM NhaSanXuat n WHERE n.tenNhaSanXuat = :tenNhaSanXuat", NhaSanXuat.class)
                    .setParameter("tenNhaSanXuat", tenNhaSanXuat)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}