package dao.impl;

import dao.ThuocDAO;
import entity.NhaSanXuat;
import entity.Thuoc;


import java.util.List;
import java.util.Optional;

public class ThuocDAOImpl extends GenericDAOImpl<Thuoc, String> implements ThuocDAO {

    public ThuocDAOImpl() {
        super(Thuoc.class);
    }

    @Override
    public Thuoc findById(String maThuoc) {
        return super.findById(maThuoc);
    }

    @Override
    public List<Thuoc> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(Thuoc thuoc) {
        return super.save(thuoc);
    }

    @Override
    public boolean update(Thuoc thuoc) {
        return super.update(thuoc);
    }

    @Override
    public boolean delete(String maThuoc) {
        return super.delete(maThuoc);
    }

    @Override
    public Optional<Thuoc> findByTenThuoc(String tenThuoc) {
        try {
            return em.createQuery("SELECT t FROM Thuoc t WHERE t.tenThuoc = :tenThuoc", Thuoc.class)
                    .setParameter("tenThuoc", tenThuoc)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Thuoc> findByDanhMuc(String danhMuc) {
        try {
            return em.createQuery("SELECT t FROM Thuoc t WHERE t.danhMuc = :danhMuc", Thuoc.class)
                    .setParameter("danhMuc", danhMuc)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Thuoc> findByNhaSanXuat(NhaSanXuat nhaSanXuat) {
        try {
            return em.createQuery("SELECT t FROM Thuoc t WHERE t.nhaSanXuat = :nhaSanXuat", Thuoc.class)
                    .setParameter("nhaSanXuat", nhaSanXuat)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void updateSoLuongTon(Thuoc thuoc, int updatedSoLuongTon) {
        try {
            em.getTransaction().begin();
            thuoc.setSoLuongTon(updatedSoLuongTon);
            em.merge(thuoc);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
}