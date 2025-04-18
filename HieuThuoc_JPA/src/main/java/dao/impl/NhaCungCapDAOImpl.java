package dao.impl;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;


import java.util.List;
import java.util.Optional;

public class NhaCungCapDAOImpl extends GenericDAOImpl<NhaCungCap, String> implements NhaCungCapDAO {

    public NhaCungCapDAOImpl() {
        super(NhaCungCap.class);
    }

    @Override
    public NhaCungCap findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<NhaCungCap> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(NhaCungCap nhaCungCap) {
        return super.save(nhaCungCap);
    }

    @Override
    public boolean update(NhaCungCap nhaCungCap) {
        return super.update(nhaCungCap);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    public Optional<NhaCungCap> findByTen(String ten) {
        try {
            return em.createQuery("SELECT n FROM NhaCungCap n WHERE n.ten = :ten", NhaCungCap.class)
                    .setParameter("ten", ten)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}