package service.impl;

import dao.GenericDAO;
import dao.VaiTroDAO;
import entity.VaiTro;
import service.VaiTroService;

import java.rmi.RemoteException;

public class VaiTroServiceImpl extends GenericServiceImpl<VaiTro, String> implements VaiTroService {
    private VaiTroDAO vaiTroDAO;

    public VaiTroServiceImpl(VaiTroDAO vaiTroDAO) throws RemoteException {
        super(vaiTroDAO);
        this.vaiTroDAO = vaiTroDAO;
    }

    @Override
    public VaiTro findByTenVaiTro(String tenVaiTro) {
        return vaiTroDAO.findByTenVaiTro(tenVaiTro);
    }
}
