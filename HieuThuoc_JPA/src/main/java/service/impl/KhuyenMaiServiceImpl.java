package service.impl;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import service.KhuyenMaiService;

import java.rmi.RemoteException;

public class KhuyenMaiServiceImpl extends GenericServiceImpl<KhuyenMai, String> implements KhuyenMaiService {
    // Implement the methods defined in the GenericService interface

    public KhuyenMaiServiceImpl(KhuyenMaiDAO khuyenMaiDAO) throws RemoteException {
        super(khuyenMaiDAO);
    }

    // Additional methods specific to KhuyenMai can be added here
}
