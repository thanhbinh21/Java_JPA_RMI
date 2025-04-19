package service;

import entity.TaiKhoan;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;

public interface TaiKhoanService extends GenericService<TaiKhoan, String> {
    TaiKhoan findByIdAndPassword(String username, String password) throws RemoteException;
}