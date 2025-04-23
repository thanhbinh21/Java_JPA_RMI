package service.impl;

import dao.ChiTietPhieuNhapThuocDAO;
import dao.NhaCungCapDAO;
import dao.NhanVienDAO;
import dao.PhieuNhapThuocDAO;
import dao.ThuocDAO;
import entity.ChiTietPhieuNhapThuoc;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import service.PhieuNhapThuocService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuNhapThuocServiceImpl extends UnicastRemoteObject implements PhieuNhapThuocService {
    private final PhieuNhapThuocDAO phieuNhapThuocDAO;
    private final NhaCungCapDAO nhaCungCapDAO;
    private final NhanVienDAO nhanVienDAO;
    private final ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO;
    private final ThuocDAO thuocDAO;

    public PhieuNhapThuocServiceImpl(PhieuNhapThuocDAO phieuNhapThuocDAO, 
                                    NhaCungCapDAO nhaCungCapDAO,
                                    NhanVienDAO nhanVienDAO,
                                    ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO,
                                    ThuocDAO thuocDAO) throws RemoteException {
        super();
        this.phieuNhapThuocDAO = phieuNhapThuocDAO;
        this.nhaCungCapDAO = nhaCungCapDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.chiTietPhieuNhapThuocDAO = chiTietPhieuNhapThuocDAO;
        this.thuocDAO = thuocDAO;
    }

    @Override
    public PhieuNhapThuoc findById(String id) throws RemoteException {
        return phieuNhapThuocDAO.findById(id);
    }

    @Override
    public List<PhieuNhapThuoc> findAll() throws RemoteException {
        return phieuNhapThuocDAO.findAll();
    }

    @Override
    public boolean save(PhieuNhapThuoc entity) throws RemoteException {
        return phieuNhapThuocDAO.save(entity);
    }

    @Override
    public boolean update(PhieuNhapThuoc entity) throws RemoteException {
        return phieuNhapThuocDAO.update(entity);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        return phieuNhapThuocDAO.delete(id);
    }

    @Override
    public List<PhieuNhapThuoc> findByNhaCungCap(String nhaCungCapId) throws RemoteException {
        NhaCungCap nhaCungCap = nhaCungCapDAO.findById(nhaCungCapId);
        if (nhaCungCap == null) {
            return List.of();
        }
        return phieuNhapThuocDAO.findByNhaCungCap(nhaCungCap);
    }

    @Override
    public List<PhieuNhapThuoc> findByNhanVien(String nhanVienId) throws RemoteException {
        NhanVien nhanVien = nhanVienDAO.findById(nhanVienId);
        if (nhanVien == null) {
            return List.of();
        }
        return phieuNhapThuocDAO.findByNhanVien(nhanVien);
    }

    @Override
    public List<PhieuNhapThuoc> findByDateRange(LocalDateTime fromDate, LocalDateTime toDate) throws RemoteException {
        return phieuNhapThuocDAO.findAll().stream()
                .filter(phieu -> {
                    LocalDateTime phieuDate = phieu.getThoiGian();
                    return phieuDate != null && 
                           !phieuDate.isBefore(fromDate) && 
                           !phieuDate.isAfter(toDate);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean addPhieuNhapWithDetails(PhieuNhapThuoc phieuNhapThuoc, List<ChiTietPhieuNhapThuoc> chiTietPhieuNhapThuocs) throws RemoteException {
        // First save the import receipt
        boolean phieuSaved = phieuNhapThuocDAO.save(phieuNhapThuoc);
        if (!phieuSaved) {
            return false;
        }
        
        // Then save all details using ChiTietPhieuNhapThuocService for proper session handling
        boolean allDetailsSaved = true;
        ChiTietPhieuNhapThuocServiceImpl chiTietService = new ChiTietPhieuNhapThuocServiceImpl(
                chiTietPhieuNhapThuocDAO, phieuNhapThuocDAO, thuocDAO);
        
        for (ChiTietPhieuNhapThuoc chiTiet : chiTietPhieuNhapThuocs) {
            String thuocId = chiTiet.getThuoc().getId();
            int soLuong = chiTiet.getSoLuong();
            double donGia = chiTiet.getDonGia();
            
            boolean detailSaved = chiTietService.createAndSaveChiTiet(
                    phieuNhapThuoc.getId(), thuocId, soLuong, donGia);
                    
            if (!detailSaved) {
                allDetailsSaved = false;
            }
        }
        
        // If all details were saved successfully, update inventory
        if (allDetailsSaved) {
            return updateInventoryFromPhieuNhap(phieuNhapThuoc.getId());
        }
        
        return false;
    }

    @Override
    public List<ChiTietPhieuNhapThuoc> getChiTietPhieuNhap(String phieuNhapThuocId) throws RemoteException {
        PhieuNhapThuoc phieuNhapThuoc = phieuNhapThuocDAO.findById(phieuNhapThuocId);
        if (phieuNhapThuoc == null) {
            return List.of();
        }
        return chiTietPhieuNhapThuocDAO.findByPhieuNhapThuoc(phieuNhapThuoc);
    }

    @Override
    public String generateNewPhieuNhapId() throws RemoteException {
        // Format: PN + current date + sequential number
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = now.format(formatter);
        
        // Get all receipts starting with "PN" + current date
        String prefix = "PN" + dateStr;
        List<PhieuNhapThuoc> existingPhieus = phieuNhapThuocDAO.findAll().stream()
                .filter(p -> p.getId() != null && p.getId().startsWith(prefix))
                .collect(Collectors.toList());
        
        // Determine the next sequence number
        int nextSeq = 1;
        if (!existingPhieus.isEmpty()) {
            nextSeq = existingPhieus.stream()
                    .map(p -> {
                        try {
                            return Integer.parseInt(p.getId().substring(prefix.length()));
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .max(Integer::compareTo)
                    .orElse(0) + 1;
        }
        
        // Format with leading zeros (e.g., PN202305010001)
        return prefix + String.format("%04d", nextSeq);
    }

    @Override
    public boolean updateInventoryFromPhieuNhap(String phieuNhapThuocId) throws RemoteException {
        List<ChiTietPhieuNhapThuoc> chiTietList = getChiTietPhieuNhap(phieuNhapThuocId);
        boolean allUpdated = true;
        
        for (ChiTietPhieuNhapThuoc chiTiet : chiTietList) {
            Thuoc thuoc = chiTiet.getThuoc();
            if (thuoc != null) {
                // Update medicine quantity
                int currentQuantity = thuoc.getSoLuongTon();
                thuoc.setSoLuongTon(currentQuantity + chiTiet.getSoLuong());
                
                // Update medicine if price has changed
                if (chiTiet.getDonGia() > 0) {
                    // Update price
                    thuoc.setDonGia(chiTiet.getDonGia() * 1.2); // 20% markup, adjust as needed
                }
                
                boolean updated = thuocDAO.update(thuoc);
                if (!updated) {
                    allUpdated = false;
                }
            }
        }
        
        return allUpdated;
    }

    /**
     * Search for receipts based on multiple criteria
     * 
     * @param receiptId the receipt ID (can be partial)
     * @param supplierName the supplier name (can be partial)
     * @param fromDate the start date (optional)
     * @param toDate the end date (optional)
     * @return list of matching receipts
     */
    public List<PhieuNhapThuoc> searchPhieuNhap(String receiptId, String supplierName, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        try {
            List<PhieuNhapThuoc> allReceipts = findAll();
            List<PhieuNhapThuoc> filteredReceipts = new ArrayList<>();
            
            for (PhieuNhapThuoc receipt : allReceipts) {
                boolean matches = true;
                
                // Check receipt ID
                if (!receiptId.isEmpty() && !receipt.getId().toLowerCase().contains(receiptId.toLowerCase())) {
                    matches = false;
                }
                
                // Check supplier name
                if (matches && !supplierName.isEmpty() && 
                    (receipt.getNhaCungCap() == null || 
                    !receipt.getNhaCungCap().getTen().toLowerCase().contains(supplierName.toLowerCase()))) {
                    matches = false;
                }
                
                // Check date range
                if (matches && fromDate != null && 
                    (receipt.getThoiGian() == null || receipt.getThoiGian().isBefore(fromDate))) {
                    matches = false;
                }
                
                if (matches && toDate != null && 
                    (receipt.getThoiGian() == null || receipt.getThoiGian().isAfter(toDate))) {
                    matches = false;
                }
                
                if (matches) {
                    filteredReceipts.add(receipt);
                }
            }
            
            return filteredReceipts;
        } catch (Exception e) {
            throw new Exception("Error searching receipts: " + e.getMessage());
        }
    }
} 