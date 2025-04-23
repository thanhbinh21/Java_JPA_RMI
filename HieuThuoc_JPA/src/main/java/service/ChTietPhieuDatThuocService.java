package service;

import entity.ChiTietPhieuDatThuoc;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuDatThuoc;

import java.rmi.RemoteException;
import java.util.List;


public interface ChTietPhieuDatThuocService extends GenericService<ChiTietPhieuDatThuoc, String> {

    List<ChiTietPhieuDatThuoc> findByIdPDT(PhieuDatThuoc phieuDatThuoc) throws RemoteException;
}
