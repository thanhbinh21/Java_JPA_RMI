package service;

import entity.Thuoc;
import java.rmi.RemoteException;
import java.util.List;

public interface ThuocService extends GenericService<Thuoc, String> {
    List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException;
    List<Thuoc> searchThuoc(String keyword) throws RemoteException;
    List<Object[]> getMaTenThuoc() throws RemoteException;
}