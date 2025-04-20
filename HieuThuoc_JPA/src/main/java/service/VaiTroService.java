package service;

import entity.VaiTro;

import java.rmi.RemoteException;

public interface VaiTroService extends GenericService<VaiTro, String> {
    VaiTro findByTenVaiTro(String tenVaiTro) throws RemoteException;
}
