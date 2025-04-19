package service;

import entity.KhachHang;
import java.rmi.RemoteException;

public interface KhachHangService extends GenericService<KhachHang, String> {
    KhachHang getKhachHangBySdt(String sdt) throws RemoteException;
}