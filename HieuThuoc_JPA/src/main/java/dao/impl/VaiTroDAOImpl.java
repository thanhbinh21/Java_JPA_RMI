package dao.impl;

import dao.VaiTroDAO;
import entity.VaiTro;
import jakarta.persistence.EntityManager;


import java.util.List;
import java.util.Optional;

public class VaiTroDAOImpl extends GenericDAOImpl<VaiTro, String> implements VaiTroDAO {

    public VaiTroDAOImpl() {
        super(VaiTro.class);
    }

    public VaiTroDAOImpl(EntityManager em) {super(em, VaiTro.class);}

    @Override
    public VaiTro findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<VaiTro> findAll() {
        return super.findAll();
    }

    @Override
    public boolean save(VaiTro vaiTro) {
        return super.save(vaiTro);
    }

    @Override
    public boolean update(VaiTro vaiTro) {
        return super.update(vaiTro);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    public VaiTro findByTenVaiTro(String tenVaiTro) {
        try {
            return em.createQuery("SELECT v FROM VaiTro v WHERE v.tenVaiTro = :tenVaiTro", VaiTro.class)
                    .setParameter("tenVaiTro", tenVaiTro)
                    .getResultList()
                    .get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}