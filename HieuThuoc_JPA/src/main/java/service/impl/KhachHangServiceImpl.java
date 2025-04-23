package service.impl;

import dao.KhachHangDAO;
import entity.KhachHang;
import java.rmi.RemoteException;
import java.util.List;

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

    @Override
    public List<KhachHang> findByGender(boolean genderFilter) {
        return List.of();
    }

    @Override
    public List<KhachHang> findByName(String searchValue) {
        return List.of();
    }

    @Override
    public List<KhachHang> findByPhone(String searchValue) {
        return List.of();
    }
}