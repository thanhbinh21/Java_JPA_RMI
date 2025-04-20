package service.impl;

import dao.NhaSanXuatDAO;
import entity.NhaSanXuat;
import service.NhaSanXuatService;

import java.rmi.RemoteException;

public class NhaSanXuatServiceImpl extends GenericServiceImpl<NhaSanXuat, String> implements NhaSanXuatService {
    private NhaSanXuatDAO nhaSanXuatDAO;

    public NhaSanXuatServiceImpl(NhaSanXuatDAO nhaSanXuatDAO) throws RemoteException {
        super(nhaSanXuatDAO);
        this.nhaSanXuatDAO = nhaSanXuatDAO;
    }

    @Override
    public NhaSanXuat findByTenNhaSanXuat(String tenNhaSanXuat) {
        return nhaSanXuatDAO.findByTenNhaSanXuat(tenNhaSanXuat);
    }
}
