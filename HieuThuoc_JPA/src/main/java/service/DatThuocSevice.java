package service;

import entity.PhieuDatThuoc;
import entity.Thuoc;

import java.rmi.RemoteException;
import java.util.List;

public interface DatThuocSevice  extends GenericService<PhieuDatThuoc,String>{
    boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) throws RemoteException;

}
