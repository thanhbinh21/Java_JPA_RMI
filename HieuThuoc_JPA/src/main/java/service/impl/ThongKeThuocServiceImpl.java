package service.impl;

import dao.ThuocDAO;
import entity.Thuoc;
import service.ThongKeThuocService;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ThongKeThuocService interface.
 * This class handles the statistics operations for medicines.
 */
public class ThongKeThuocServiceImpl implements ThongKeThuocService {

    private final ThuocDAO thuocDAO;

    /**
     * Constructor that initializes the service with required DAOs
     * 
     * @param thuocDAO The DAO for medicine operations
     */
    public ThongKeThuocServiceImpl(ThuocDAO thuocDAO) {
        this.thuocDAO = thuocDAO;
    }

    @Override
    public List<Thuoc> danhSachThuocHetHan(Date ngayBatDau, Date ngayKetThuc) throws RemoteException {
        try {
            // Get medicines that have expired within the given date range
            List<Thuoc> allThuoc = thuocDAO.findAll();
            List<Thuoc> result = new ArrayList<>();
            
            for (Thuoc thuoc : allThuoc) {
                Date hanSuDung = (Date) thuoc.getHanSuDung();
                if (hanSuDung != null && 
                    (hanSuDung.compareTo(ngayBatDau) >= 0) && 
                    (hanSuDung.compareTo(ngayKetThuc) <= 0)) {
                    result.add(thuoc);
                }
            }
            
            return result;
        } catch (Exception e) {
            throw new RemoteException("Error retrieving expired medicines", e);
        }
    }

    @Override
    public List<Thuoc> danhSachThuocDaBan(Date ngayBatDau, Date ngayKetThuc) throws RemoteException {
        try {
            // In a real implementation, this would query the sold medicines from invoice details
            // For now, we'll return all medicines as a placeholder
            return thuocDAO.findSoldMedicines(ngayBatDau, ngayKetThuc);
        } catch (Exception e) {
            throw new RemoteException("Error retrieving sold medicines", e);
        }
    }

    @Override
    public List<Thuoc> danhSachThuocConLai(Date ngayBatDau, Date ngayKetThuc) throws RemoteException {
        try {
            // Get medicines that are not expired (expiry date > endDate)
            List<Thuoc> allThuoc = thuocDAO.findAll();
            List<Thuoc> result = new ArrayList<>();
            
            for (Thuoc thuoc : allThuoc) {
                Date hanSuDung = (Date) thuoc.getHanSuDung();
                if (hanSuDung != null && hanSuDung.compareTo(ngayKetThuc) > 0) {
                    result.add(thuoc);
                }
            }
            
            return result;
        } catch (Exception e) {
            throw new RemoteException("Error retrieving non-expired medicines", e);
        }
    }

    @Override
    public List<Thuoc> danhsachThuocConLaiTrongKho(Date ngayBatDau, Date ngayKetThuc) throws RemoteException {
        try {
            // Get medicines that are in stock (quantity > 0) and not expired
            List<Thuoc> allThuoc = thuocDAO.findAll();
            List<Thuoc> result = new ArrayList<>();
            
            for (Thuoc thuoc : allThuoc) {
                Date hanSuDung = (Date) thuoc.getHanSuDung();
                if (hanSuDung != null && 
                    hanSuDung.compareTo(ngayKetThuc) > 0 && 
                    thuoc.getSoLuongTon() > 0) {
                    result.add(thuoc);
                }
            }
            
            return result;
        } catch (Exception e) {
            throw new RemoteException("Error retrieving available medicines in inventory", e);
        }
    }

    @Override
    public int tinhTongLoaiThuocDaBan(Date ngayBatDau, Date ngayKetThuc) throws RemoteException {
        try {
            // In a real implementation, this would count distinct medicine types from invoice details
            List<Thuoc> soldMedicines = danhSachThuocDaBan(ngayBatDau, ngayKetThuc);
            return soldMedicines.size();
        } catch (Exception e) {
            throw new RemoteException("Error calculating total number of medicine types sold", e);
        }
    }
}