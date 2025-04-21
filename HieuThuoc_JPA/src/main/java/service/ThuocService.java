package service;

import entity.DanhMuc;
import entity.Thuoc;
import java.rmi.RemoteException;
import java.util.List;

public interface ThuocService extends GenericService<Thuoc, String> {
    List<DanhMuc > getAllDanhMuc() throws RemoteException;
    List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException;
    List<Thuoc> searchThuoc(String keyword) throws RemoteException;
    List<Object[]> getMaTenThuoc() throws RemoteException;
    List<Thuoc> getThuocByTenDanhMuc(String tenDM) throws RemoteException;
}