package dao.impl;

import dao.ChiTietHoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;


import java.util.List;

public class ChiTietHoaDonDAOImpl extends GenericDAOImpl<ChiTietHoaDon, Long> implements ChiTietHoaDonDAO {

    public ChiTietHoaDonDAOImpl() {
        super(ChiTietHoaDon.class);
    }

    @Override
    public ChiTietHoaDon findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<ChiTietHoaDon> findAll() {
        return super.getAll();
    }

    @Override
    public boolean save(ChiTietHoaDon chiTietHoaDon) {
        return super.save(chiTietHoaDon);
    }

    @Override
    public boolean update(ChiTietHoaDon chiTietHoaDon) {
        return super.update(chiTietHoaDon);
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id);
    }

    @Override
    public List<ChiTietHoaDon> findByHoaDon(HoaDon hoaDon) {
        try {
            return em.createQuery("SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon = :hoaDon", ChiTietHoaDon.class)
                    .setParameter("hoaDon", hoaDon)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}