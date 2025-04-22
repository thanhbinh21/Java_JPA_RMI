package dao;

import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;

import java.rmi.RemoteException;
import java.util.List;

public interface PhieuDatThuocDAO extends GenericDAO<PhieuDatThuoc, String>  {
    List<PhieuDatThuoc> findByKhachHang(KhachHang khachHang);

    List<PhieuDatThuoc> findByNhanVien(NhanVien nhanVien);

    boolean updateTT(String selectedMaPDT);
    // Các phương thức đặc thù cho PhieuDatThuocDAO nếu có
    List<PhieuDatThuoc> findBySdt(String sdt);

    boolean addPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) throws RemoteException;
}