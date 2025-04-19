package service.impl;

import dao.ChiTietPhieuDatThuocDAO;
import dao.PhieuDatThuocDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.KhachHang;
import entity.PhieuDatThuoc;
import java.rmi.RemoteException;
import java.util.List;
import service.PhieuDatThuocService;

public class PhieuDatThuocServiceImpl extends GenericServiceImpl<PhieuDatThuoc, String> implements PhieuDatThuocService {

    private final PhieuDatThuocDAO phieuDatThuocDAO;
    private final ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO;

    public PhieuDatThuocServiceImpl(PhieuDatThuocDAO phieuDatThuocDAO, ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO) throws RemoteException {
        super(phieuDatThuocDAO);
        this.phieuDatThuocDAO = phieuDatThuocDAO;
        this.chiTietPhieuDatThuocDAO = chiTietPhieuDatThuocDAO;
    }

    @Override
    public List<PhieuDatThuoc> findByKhachHang(KhachHang khachHang) throws RemoteException {
        return phieuDatThuocDAO.findByKhachHang(khachHang);
    }

    @Override
    public List<ChiTietPhieuDatThuoc> findChiTietByPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) throws RemoteException {
        return chiTietPhieuDatThuocDAO.findByPhieuDatThuoc(phieuDatThuoc);
    }

    @Override
    public boolean updateTrangThai(String maPDT, boolean daXuLy) throws RemoteException {
        PhieuDatThuoc pdt = phieuDatThuocDAO.findById(maPDT);
        if (pdt != null) {
            pdt.setTrangThai(daXuLy);
            return phieuDatThuocDAO.update(pdt);
        }
        return false;
    }
}