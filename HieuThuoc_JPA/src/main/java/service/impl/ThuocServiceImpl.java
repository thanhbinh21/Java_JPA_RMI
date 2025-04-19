package service.impl;

import dao.DanhMucDAO;
import dao.ThuocDAO;
import entity.Thuoc;
import java.rmi.RemoteException;
import java.util.List;
import service.ThuocService;

public class ThuocServiceImpl extends GenericServiceImpl<Thuoc, String> implements ThuocService {

    private final ThuocDAO thuocDAO;
    private final DanhMucDAO danhMucDAO;

    public ThuocServiceImpl(ThuocDAO thuocDAO, DanhMucDAO danhMucDAO) throws RemoteException {
        super(thuocDAO);
        this.thuocDAO = thuocDAO;
        this.danhMucDAO = danhMucDAO;
    }

    @Override
    public List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException {
        return thuocDAO.selectByDanhMuc(danhMucDAO.findById(maDM));
    }

    @Override
    public List<Thuoc> searchThuoc(String keyword) throws RemoteException {
        return thuocDAO.searchByKeyword(keyword);
    }

    @Override
    public List<Object[]> getMaTenThuoc() throws RemoteException {
        return List.of();
    }

//    @Override
//    public List<Object[]> getMaTenThuoc() throws RemoteException {
//        return thuocDAO.getMaTenThuoc();
//    }
}