package service.impl;

import dao.ChiTietHoaDonDAO;
import dao.GenericDAO;
import entity.ChiTietHoaDon;
import service.ChiTietHoaDonService;

import java.rmi.RemoteException;

public class ChiTietHoaDonServiceImpl extends GenericServiceImpl<ChiTietHoaDon, ChiTietHoaDon.ChiTietHoaDonID> implements ChiTietHoaDonService {
    private ChiTietHoaDonDAO chiTietHoaDonDAO;

    public ChiTietHoaDonServiceImpl(ChiTietHoaDonDAO chiTietHoaDonDAO) throws RemoteException {
        super(chiTietHoaDonDAO);
    }
}
