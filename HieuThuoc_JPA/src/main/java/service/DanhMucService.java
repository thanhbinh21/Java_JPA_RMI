package service;

import entity.DanhMuc;

import java.rmi.RemoteException;
import java.util.List;

public interface DanhMucService extends GenericService<DanhMuc, String> {
    List<DanhMuc> findByTenDanhMuc(String tenDanhMuc) throws RemoteException;
}
