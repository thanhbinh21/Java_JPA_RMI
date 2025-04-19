package service;

import entity.Thuoc;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import java.rmi.RemoteException;
import java.util.List;

public interface BanThuocService extends GenericService<Thuoc, String> {
    List<Thuoc> getAllThuoc() throws RemoteException; // Override để chỉ định kiểu trả về
    Thuoc getThuocById(String maThuoc) throws RemoteException; // Override
    List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException;
    List<Thuoc> searchThuoc(String keyword) throws RemoteException;
    boolean addThuocToCart(List<ChiTietHoaDon> cart, Thuoc thuoc, int soLuong) throws RemoteException;
    boolean updateCartItemQuantity(List<ChiTietHoaDon> cart, String maThuoc, int newQuantity) throws RemoteException;
    boolean removeThuocFromCart(List<ChiTietHoaDon> cart, String maThuoc) throws RemoteException;
    HoaDon createHoaDon(HoaDon hoaDon, List<ChiTietHoaDon> cart) throws RemoteException;
    KhachHang getKhachHangBySdt(String sdt) throws RemoteException;
    boolean createKhachHang(KhachHang khachHang) throws RemoteException;
}