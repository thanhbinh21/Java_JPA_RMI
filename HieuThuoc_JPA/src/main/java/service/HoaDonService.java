package service;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface HoaDonService extends GenericService<HoaDon, String> {
    List<HoaDon> findByKhachHang(KhachHang khachHang) throws RemoteException;
    List<HoaDon> findByNhanVien(NhanVien nhanVien) throws RemoteException;
    List<Object[]> getSoLuongHoaDonTheoKhachHang() throws RemoteException;

    List<HoaDon> findByDateRange(LocalDate startDate, LocalDate endDate) throws RemoteException;

    List<HoaDon> findByKhachHangName(String khachHangName) throws RemoteException;

    List<HoaDon> findByNhanVienName(String nhanVienName) throws RemoteException;
    
    /**
     * Calculate the total amount for a given invoice safely within the Hibernate session
     * @param hoaDonId The ID of the invoice
     * @return The total amount of the invoice
     * @throws RemoteException if there's an error during remote execution
     */
    double calculateTotalAmount(String hoaDonId) throws RemoteException;
}