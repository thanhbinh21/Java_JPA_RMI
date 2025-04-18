package dao.impl;

import dao.KhachHangDAO;
import entity.KhachHang;


import java.util.List;
import java.util.Optional;

public class KhachHangDAOImpl extends GenericDAOImpl<KhachHang, String> implements KhachHangDAO {

    public KhachHangDAOImpl() {
        super(KhachHang.class);
    }

    @Override
    public KhachHang findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<KhachHang> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(KhachHang khachHang) {
        return super.save(khachHang);
    }

    @Override
    public boolean update(KhachHang khachHang) {
        return super.update(khachHang);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    public KhachHang findBySoDienThoai(String soDienThoai) {
        try {
            return em.createQuery("SELECT k FROM KhachHang k WHERE k.soDienThoai = :soDienThoai", KhachHang.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}