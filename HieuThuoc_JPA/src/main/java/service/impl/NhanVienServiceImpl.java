package service.impl;

import dao.GenericDAO;
import dao.NhanVienDAO;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import service.NhanVienService;

import java.rmi.RemoteException;

public class NhanVienServiceImpl extends GenericServiceImpl<NhanVien, String> implements NhanVienService {
    private NhanVienDAO nhanVienDAO;

    public NhanVienServiceImpl(NhanVienDAO nhanVienDAO) throws RemoteException {
        super(nhanVienDAO);
        this.nhanVienDAO = nhanVienDAO;
    }

    @Override
    public NhanVien findByTen(String ten) {
        return nhanVienDAO.findByTen(ten);
    }
}
