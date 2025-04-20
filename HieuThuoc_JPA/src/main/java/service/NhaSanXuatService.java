package service;

import entity.NhaSanXuat;

import java.rmi.RemoteException;

public interface NhaSanXuatService extends GenericService<NhaSanXuat, String> {
    NhaSanXuat findByTenNhaSanXuat(String tenNhaSanXuat) throws RemoteException;
}
