package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OneSessionService extends Remote {
    BanThuocService getBanThuocService() throws RemoteException;
    ChiTietHoaDonService getChiTietHoaDonService() throws RemoteException;
    DanhMucService getDanhMucService() throws RemoteException;
    HoaDonService getHoaDonService() throws RemoteException;
    KhachHangService getKhachHangService() throws RemoteException;
    NhanVienService getNhanVienService() throws RemoteException;
    NhaSanXuatService getNhaSanXuatService() throws RemoteException;
    PhieuDatThuocService getPhieuDatThuocService() throws RemoteException;
    TaiKhoanService getTaiKhoanService() throws RemoteException;
    ThuocService getThuocService() throws RemoteException;
    VaiTroService getVaiTroService() throws RemoteException;
}
