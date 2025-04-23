package service.impl;

import dao.ChiTietPhieuDatThuocDAO;
import dao.GenericDAO;
import dao.PhieuDatThuocDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuDatThuoc;
import service.ChTietPhieuDatThuocService;
import service.PhieuDatThuocService;

import java.rmi.RemoteException;
import java.util.List;

public class ChiTietPhieuDatThuocSeiviceImpl extends GenericServiceImpl<ChiTietPhieuDatThuoc, String> implements ChTietPhieuDatThuocService {
    private final ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO;

     ChiTietPhieuDatThuocSeiviceImpl(GenericDAO<ChiTietPhieuDatThuoc, String> genericDAO, ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO) throws RemoteException {
        super(genericDAO);
         this.chiTietPhieuDatThuocDAO = chiTietPhieuDatThuocDAO;
     }


    @Override
    public List<ChiTietPhieuDatThuoc> findByIdPDT(PhieuDatThuoc phieuDatThuoc) throws RemoteException {
        return chiTietPhieuDatThuocDAO.findByPhieuDatThuoc(phieuDatThuoc);
    }


}
