package service;

import entity.NhanVien;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;

public interface NhanVienService extends GenericService<NhanVien, String> {
    NhanVien findByTen(String ten) throws RemoteException;
}
