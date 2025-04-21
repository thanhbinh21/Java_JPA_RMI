package service.impl;

import dao.DanhMucDAO;
import dao.GenericDAO;
import entity.DanhMuc;
import service.DanhMucService;

import java.rmi.RemoteException;
import java.util.List;

public class DanhMucServiceImpl extends GenericServiceImpl<DanhMuc, String> implements DanhMucService {
    private DanhMucDAO danhMucDAO;

    public DanhMucServiceImpl(DanhMucDAO danhMucDAO) throws RemoteException {
        super(danhMucDAO);
        this.danhMucDAO = danhMucDAO;
    }

    @Override
    public List<DanhMuc> findByTenDanhMuc(String tenDanhMuc) {
        return danhMucDAO.findByTenDanhMuc(tenDanhMuc);
    }
}
