package service;

import entity.DanhMuc;
import entity.KhuyenMai;
import entity.NhaSanXuat;
import entity.Thuoc;
import java.rmi.RemoteException;
import java.util.List;

public interface ThuocService extends GenericService<Thuoc, String> {
    List<DanhMuc > getAllDanhMuc() throws RemoteException;
    List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException;
    List<Thuoc> searchThuoc(String keyword) throws RemoteException;
    List<Object[]> getMaTenThuoc() throws RemoteException;
    List<Thuoc> getThuocByTenDanhMuc(String tenDM) throws RemoteException;

    NhaSanXuat findNhaSanXuatById(String nhaSanXuatStr) throws RemoteException;

    DanhMuc findDanhMucById(String danhMucStr) throws RemoteException;

    KhuyenMai findKhuyenMaiById(String khuyenMaiStr) throws RemoteException;

    List<Thuoc> findByCategory(String id) throws RemoteException;

    List<Thuoc> findByManufacturer(String id) throws RemoteException;

    List<Thuoc> findByCategoryAndManufacturer(String id, String id1) throws RemoteException;

    List<Thuoc> findByName(String searchValue) throws RemoteException;

    List<Thuoc> findByIngredient(String searchValue) throws RemoteException;
}