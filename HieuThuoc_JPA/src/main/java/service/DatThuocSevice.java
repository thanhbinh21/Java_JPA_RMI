package service;

import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import entity.Thuoc;

import java.rmi.RemoteException;
import java.util.List;

public interface DatThuocSevice  extends GenericService<PhieuDatThuoc,String>{
    boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc, List<ChiTietPhieuDatThuoc> list) throws RemoteException;

}
