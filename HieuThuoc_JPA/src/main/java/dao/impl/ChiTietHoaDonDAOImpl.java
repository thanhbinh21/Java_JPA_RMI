package dao.impl;

import dao.ChiTietHoaDonDAO;
import entity.ChiTietHoaDon;
import entity.KhachHang;
import jakarta.persistence.EntityManager;


import java.util.List;

public class ChiTietHoaDonDAOImpl extends GenericDAOImpl<ChiTietHoaDon, ChiTietHoaDon.ChiTietHoaDonID> implements ChiTietHoaDonDAO {

    public ChiTietHoaDonDAOImpl() {
        super(ChiTietHoaDon.class);
    }
    public ChiTietHoaDonDAOImpl(EntityManager em) {
        super(em, ChiTietHoaDon.class);
    }

//    @Override
//    public List<ChiTietHoaDon> findByHoaDon(HoaDon hoaDon) {
//        try {
//            return em.createQuery("SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon = :hoaDon", ChiTietHoaDon.class)
//                    .setParameter("hoaDon", hoaDon)
//                    .getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return List.of();
//        }
//    }

    @Override
    public List<ChiTietHoaDon> findByHoaDonId(ChiTietHoaDon.ChiTietHoaDonID hoaDonId) {
        return List.of();
    }

}