package service;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import java.rmi.RemoteException;
import java.util.List;

public interface HoaDonService extends GenericService<HoaDon, String> {
    List<HoaDon> findByKhachHang(KhachHang khachHang) throws RemoteException;
    List<HoaDon> findByNhanVien(NhanVien nhanVien) throws RemoteException;
    List<Object[]> getSoLuongHoaDonTheoKhachHang() throws RemoteException;
}