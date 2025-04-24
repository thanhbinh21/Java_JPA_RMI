package service;

import entity.Thuoc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

/**
 * Interface for the medicine statistics service that provides methods
 * to retrieve information about medicines in various states (expired, sold, etc.)
 */
public interface ThongKeThuocService extends Remote {
    
    /**
     * Returns a list of expired medicines within the given date range
     * 
     * @param ngayBatDau Start date of the range
     * @param ngayKetThuc End date of the range
     * @return List of expired medicines
     * @throws RemoteException If there's a communication error
     */
    List<Thuoc> danhSachThuocHetHan(Date ngayBatDau, Date ngayKetThuc) throws RemoteException;
    
    /**
     * Returns a list of sold medicines within the given date range
     * 
     * @param ngayBatDau Start date of the range
     * @param ngayKetThuc End date of the range
     * @return List of sold medicines
     * @throws RemoteException If there's a communication error
     */
    List<Thuoc> danhSachThuocDaBan(Date ngayBatDau, Date ngayKetThuc) throws RemoteException;
    
    /**
     * Returns a list of medicines that are still within their expiration date
     * 
     * @param ngayBatDau Start date of the range
     * @param ngayKetThuc End date of the range
     * @return List of non-expired medicines
     * @throws RemoteException If there's a communication error
     */
    List<Thuoc> danhSachThuocConLai(Date ngayBatDau, Date ngayKetThuc) throws RemoteException;
    
    /**
     * Returns a list of medicines still available in the inventory
     * 
     * @param ngayBatDau Start date of the range
     * @param ngayKetThuc End date of the range
     * @return List of available medicines in inventory
     * @throws RemoteException If there's a communication error
     */
    List<Thuoc> danhsachThuocConLaiTrongKho(Date ngayBatDau, Date ngayKetThuc) throws RemoteException;
    
    /**
     * Calculates the total number of medicine types sold within the given date range
     * 
     * @param ngayBatDau Start date of the range
     * @param ngayKetThuc End date of the range
     * @return Total number of medicine types sold
     * @throws RemoteException If there's a communication error
     */
    int tinhTongLoaiThuocDaBan(Date ngayBatDau, Date ngayKetThuc) throws RemoteException;
}