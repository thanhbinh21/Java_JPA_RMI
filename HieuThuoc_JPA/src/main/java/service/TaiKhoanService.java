package service;

import entity.TaiKhoan;
import java.rmi.RemoteException;

public interface TaiKhoanService extends GenericService<TaiKhoan, String> {
    TaiKhoan authenticate(String username, String password) throws RemoteException;
}