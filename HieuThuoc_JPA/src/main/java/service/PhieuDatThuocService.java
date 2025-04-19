package service;

import entity.ChiTietPhieuDatThuoc;
import entity.KhachHang;
import entity.PhieuDatThuoc;
import java.rmi.RemoteException;
import java.util.List;

public interface PhieuDatThuocService extends GenericService<PhieuDatThuoc, String> {
    List<PhieuDatThuoc> findByKhachHang(KhachHang khachHang) throws RemoteException;
    List<ChiTietPhieuDatThuoc> findChiTietByPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) throws RemoteException;
    boolean updateTrangThai(String maPDT, boolean daXuLy) throws RemoteException;


    List<PhieuDatThuoc> findBySdt(String sdt) throws RemoteException;
}