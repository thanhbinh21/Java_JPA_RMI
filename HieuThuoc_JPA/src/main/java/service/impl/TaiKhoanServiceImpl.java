package service.impl;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import java.rmi.RemoteException;

import jakarta.persistence.EntityManager;
import service.TaiKhoanService;

public class TaiKhoanServiceImpl extends GenericServiceImpl<TaiKhoan, String> implements TaiKhoanService {

    private final TaiKhoanDAO taiKhoanDAO;

    public TaiKhoanServiceImpl(TaiKhoanDAO taiKhoanDAO) throws RemoteException {
        super(taiKhoanDAO);
        this.taiKhoanDAO = taiKhoanDAO;
    }

    @Override
    public TaiKhoan findByIdAndPassword(String username, String password) throws RemoteException {
        return taiKhoanDAO.findByIdAndPassword(username, password);
    }
}