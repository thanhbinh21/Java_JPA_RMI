package service.impl;

import dao.KhachHangDAO;
import entity.KhachHang;
import java.rmi.RemoteException;
import service.KhachHangService;

public class KhachHangServiceImpl extends GenericServiceImpl<KhachHang, String> implements KhachHangService {

    private final KhachHangDAO khachHangDAO;

    public KhachHangServiceImpl(KhachHangDAO khachHangDAO) throws RemoteException {
        super(khachHangDAO);
        this.khachHangDAO = khachHangDAO;
    }

    @Override
    public KhachHang getKhachHangBySdt(String sdt) throws RemoteException {
        return khachHangDAO.selectBySdt(sdt);
    }
}