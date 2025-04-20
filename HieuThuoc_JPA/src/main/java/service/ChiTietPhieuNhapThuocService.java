package service;

import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;

import java.rmi.RemoteException;
import java.util.List;

public interface ChiTietPhieuNhapThuocService extends GenericService<ChiTietPhieuNhapThuoc, Long> {
    /**
     * Find import details by import receipt
     * @param phieuNhapThuocId the import receipt ID
     * @return list of import details
     * @throws RemoteException if RMI communication fails
     */
    List<ChiTietPhieuNhapThuoc> findByPhieuNhapThuoc(String phieuNhapThuocId) throws RemoteException;
    
    /**
     * Find import detail by medicine and import receipt
     * @param thuocId the medicine ID
     * @param phieuNhapThuocId the import receipt ID
     * @return the import detail if found
     * @throws RemoteException if RMI communication fails
     */
    ChiTietPhieuNhapThuoc findByThuocAndPhieuNhapThuoc(String thuocId, String phieuNhapThuocId) throws RemoteException;
    
    /**
     * Save all import details for an import receipt
     * @param chiTietPhieuNhapThuocs the list of import details
     * @return true if successful, false otherwise
     * @throws RemoteException if RMI communication fails
     */
    boolean saveAll(List<ChiTietPhieuNhapThuoc> chiTietPhieuNhapThuocs) throws RemoteException;
    
    /**
     * Calculate total amount of an import receipt
     * @param phieuNhapThuocId the import receipt ID
     * @return the total amount
     * @throws RemoteException if RMI communication fails
     */
    double calculateTotalAmount(String phieuNhapThuocId) throws RemoteException;
} 