package service.impl;

import dao.*;
import entity.PhieuDatThuoc;

import entity.Thuoc;
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
    public boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) throws RemoteException {
        phieuDatThuocDAO.addPhieuDatThuoc(phieuDatThuoc);
        return false;
    }


}
