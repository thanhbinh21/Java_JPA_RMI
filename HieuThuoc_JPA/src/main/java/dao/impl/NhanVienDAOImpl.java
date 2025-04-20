package dao.impl;

import dao.NhanVienDAO;
import entity.NhanVien;
import jakarta.persistence.EntityManager;


import java.util.List;
import java.util.Optional;

public class NhanVienDAOImpl extends GenericDAOImpl<NhanVien, String> implements NhanVienDAO {

    public NhanVienDAOImpl() {
        super(NhanVien.class);
    }

    public NhanVienDAOImpl(EntityManager em) {
        super(em, NhanVien.class);
    }

    @Override
    public NhanVien findById(String maNhanVien) {
        return super.findById(maNhanVien);
    }

    @Override
    public List<NhanVien> findAll() {
        return super.findAll();
    }

    @Override
    public boolean save(NhanVien nhanVien) {
        return super.save(nhanVien);
    }

    @Override
    public boolean update(NhanVien nhanVien) {
        return super.update(nhanVien);
    }

    @Override
    public boolean delete(String maNhanVien) {
        return super.delete(maNhanVien);
    }

    @Override
    public NhanVien findByTen(String ten) {
        try {
            return em.createQuery("SELECT n FROM NhanVien n WHERE n.hoTen = :ten", NhanVien.class)
                    .setParameter("ten", ten)
                    .getResultList()
                    .get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}