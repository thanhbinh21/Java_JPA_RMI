package service;

import entity.ChiTietPhieuNhapThuoc;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;
import entity.Thuoc;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PhieuNhapThuocService extends GenericService<PhieuNhapThuoc, String> {
    /**
     * Find medicine import receipts by supplier
     * @param nhaCungCapId the supplier ID
     * @return list of matching medicine import receipts
     * @throws RemoteException if RMI communication fails
     */
    List<PhieuNhapThuoc> findByNhaCungCap(String nhaCungCapId) throws RemoteException;
    
    /**
     * Find medicine import receipts by employee
     * @param nhanVienId the employee ID
     * @return list of matching medicine import receipts
     * @throws RemoteException if RMI communication fails
     */
    List<PhieuNhapThuoc> findByNhanVien(String nhanVienId) throws RemoteException;
    
    /**
     * Find medicine import receipts within a date range
     * @param fromDate start date
     * @param toDate end date
     * @return list of matching medicine import receipts
     * @throws RemoteException if RMI communication fails
     */
    List<PhieuNhapThuoc> findByDateRange(LocalDateTime fromDate, LocalDateTime toDate) throws RemoteException;
    
    /**
     * Add a new medicine import receipt with its details
     * @param phieuNhapThuoc the import receipt
     * @param chiTietPhieuNhapThuocs the details of the import receipt
     * @return true if successful, false otherwise
     * @throws RemoteException if RMI communication fails
     */
    boolean addPhieuNhapWithDetails(PhieuNhapThuoc phieuNhapThuoc, List<ChiTietPhieuNhapThuoc> chiTietPhieuNhapThuocs) throws RemoteException;
    
    /**
     * Get the details of a medicine import receipt
     * @param phieuNhapThuocId the import receipt ID
     * @return list of import receipt details
     * @throws RemoteException if RMI communication fails
     */
    List<ChiTietPhieuNhapThuoc> getChiTietPhieuNhap(String phieuNhapThuocId) throws RemoteException;
    
    /**
     * Generate a new import receipt ID
     * @return the new ID
     * @throws RemoteException if RMI communication fails
     */
    String generateNewPhieuNhapId() throws RemoteException;
    
    /**
     * Update medicine inventory based on import receipt
     * @param phieuNhapThuocId the import receipt ID
     * @return true if successful, false otherwise
     * @throws RemoteException if RMI communication fails
     */
    boolean updateInventoryFromPhieuNhap(String phieuNhapThuocId) throws RemoteException;
    
    /**
     * Search for receipts based on multiple criteria
     * 
     * @param receiptId the receipt ID (can be partial)
     * @param supplierName the supplier name (can be partial)
     * @param fromDate the start date (optional)
     * @param toDate the end date (optional)
     * @return list of matching receipts
     */
    List<PhieuNhapThuoc> searchPhieuNhap(String receiptId, String supplierName, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
} 