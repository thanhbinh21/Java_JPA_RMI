package dao.impl;

import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;


import java.util.List;
import java.util.Optional;

public class TaiKhoanDAOImpl extends GenericDAOImpl<TaiKhoan, String> implements TaiKhoanDAO {

    public TaiKhoanDAOImpl() {
        super(TaiKhoan.class);
    }

    @Override
    public TaiKhoan findById(String tenTaiKhoan) {
        return super.findById(tenTaiKhoan);
    }

    @Override
    public List<TaiKhoan> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(TaiKhoan taiKhoan) {
        return super.save(taiKhoan);
    }

    @Override
    public boolean update(TaiKhoan taiKhoan) {
        return super.update(taiKhoan);
    }

    @Override
    public boolean delete(String tenTaiKhoan) {
        return super.delete(tenTaiKhoan);
    }

    @Override
    public Optional<TaiKhoan> findByMatKhau(String matKhau) {
        try {
            return em.createQuery("SELECT t FROM TaiKhoan t WHERE t.matKhau = :matKhau", TaiKhoan.class)
                    .setParameter("matKhau", matKhau)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<TaiKhoan> findByNhanVien(NhanVien nhanVien) {
        try {
            return em.createQuery("SELECT t FROM TaiKhoan t WHERE t.nhanVien = :nhanVien", TaiKhoan.class)
                    .setParameter("nhanVien", nhanVien)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}