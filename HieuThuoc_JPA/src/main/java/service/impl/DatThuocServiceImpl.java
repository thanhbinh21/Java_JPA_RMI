package service.impl;

import dao.*;
import entity.*;

import service.DatThuocSevice;

import java.rmi.RemoteException;
import java.util.List;

public class DatThuocServiceImpl extends GenericServiceImpl<PhieuDatThuoc, String> implements DatThuocSevice {

    private final PhieuDatThuocDAO phieuDatThuocDAO;
    private final ThuocDAO thuocDAO;
    private final KhachHangDAO khachHangDAO;
    private final ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO;

    public DatThuocServiceImpl( PhieuDatThuocDAO phieuDatThuocDAO, ThuocDAO thuocDAO, KhachHangDAO khachHangDAO, ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO) throws RemoteException {
        super(phieuDatThuocDAO);
        this.phieuDatThuocDAO = phieuDatThuocDAO;
        this.thuocDAO = thuocDAO;
        this.khachHangDAO = khachHangDAO;
        this.chiTietPhieuDatThuocDAO = chiTietPhieuDatThuocDAO;
    }


    @Override
    public boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc, List<ChiTietPhieuDatThuoc> list) throws RemoteException {
        try {
            KhachHang khachHang = phieuDatThuoc.getKhachHang();
            if (khachHang != null) {
                KhachHang existingKH = khachHangDAO.selectBySdt(khachHang.getSoDienThoai());
                if (existingKH == null) {
                    boolean saveKhachHangResult = khachHangDAO.save(khachHang);
                    if (!saveKhachHangResult) {
                        return false;
                    }
                } else {
                    phieuDatThuoc.setKhachHang(existingKH);
                }
            }
            boolean savePDTResult = phieuDatThuocDAO.save(phieuDatThuoc);
            if (!savePDTResult) {
                return false;
            }

            jakarta.persistence.EntityManager em = until.JPAUtil.getEntityManager();
            jakarta.persistence.EntityTransaction tx = null;

            try {
                for (ChiTietPhieuDatThuoc cartItem : list) {
                    tx = em.getTransaction();
                    tx.begin();

                    PhieuDatThuoc freshPDT = em.find(PhieuDatThuoc.class, phieuDatThuoc.getId());

                    String thuocId = cartItem.getThuoc().getId();
                    Thuoc freshThuoc = em.find(Thuoc.class, thuocId);

                    if (freshThuoc == null) {
                        tx.rollback();
                        System.err.println("không thể tìm thấy thuốc ID: " + thuocId);
                        continue;
                    }

                    ChiTietPhieuDatThuoc newCTPDT = new ChiTietPhieuDatThuoc();
                    newCTPDT.setPhieuDatThuoc(freshPDT);
                    newCTPDT.setThuoc(freshThuoc);
                    newCTPDT.setSoLuong(cartItem.getSoLuong());
                    newCTPDT.setDonGia(cartItem.getDonGia());

                    em.persist(newCTPDT);

                    int newSoLuongTon = freshThuoc.getSoLuongTon() - cartItem.getSoLuong();
                    freshThuoc.setSoLuongTon(newSoLuongTon);
                    tx.commit();
                }

                return true;
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                return false;
            } finally {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
