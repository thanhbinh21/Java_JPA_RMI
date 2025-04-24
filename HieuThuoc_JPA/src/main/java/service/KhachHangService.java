package service;

import entity.KhachHang;
import java.rmi.RemoteException;
import java.util.List;

public interface KhachHangService extends GenericService<KhachHang, String> {
    KhachHang getKhachHangBySdt(String sdt) throws RemoteException;

    List<KhachHang> findByGender(boolean genderFilter) throws RemoteException;

    List<KhachHang> findByName(String searchValue) throws RemoteException;

    List<KhachHang> findByPhone(String searchValue) throws RemoteException;
}