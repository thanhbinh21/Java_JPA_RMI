package service.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.ChiTietHoaDon;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import service.HoaDonService;

public class HoaDonServiceImpl extends GenericServiceImpl<HoaDon, String> implements HoaDonService {

    private final HoaDonDAO hoaDonDAO;

    public HoaDonServiceImpl(HoaDonDAO hoaDonDAO) throws RemoteException {
        super(hoaDonDAO);
        this.hoaDonDAO = hoaDonDAO;
    }

    @Override
    public List<HoaDon> findByKhachHang(KhachHang khachHang) throws RemoteException {
        return hoaDonDAO.findByKhachHang(khachHang);
    }

    @Override
    public List<HoaDon> findByNhanVien(NhanVien nhanVien) throws RemoteException {
        return hoaDonDAO.findByNhanVien(nhanVien);
    }

    @Override
    public List<Object[]> getSoLuongHoaDonTheoKhachHang() throws RemoteException {
        return hoaDonDAO.getSoLuongHoaDonTheoKhachHang();
    }

    @Override
    public List<HoaDon> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return hoaDonDAO.findByDateRange(startDate, endDate);
    }

    @Override
    public List<HoaDon> findByKhachHangName(String khachHangName) {
        try {
            List<HoaDon> allInvoices = findAll();
            
            // Filter invoices that have a customer with a name containing the search term
            return allInvoices.stream()
                .filter(invoice -> {
                    KhachHang khachHang = invoice.getKhachHang();
                    if (khachHang == null) {
                        return false;
                    }
                    String hoTen = khachHang.getHoTen();
                    return hoTen != null && hoTen.toLowerCase().contains(khachHangName.toLowerCase());
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list in case of error
        }
    }

    @Override
    public List<HoaDon> findByNhanVienName(String nhanVienName) {
        try {
            List<HoaDon> allInvoices = findAll();
            
            // Filter invoices that have an employee with a name containing the search term
            return allInvoices.stream()
                .filter(invoice -> {
                    NhanVien nhanVien = invoice.getNhanVien();
                    if (nhanVien == null) {
                        return false;
                    }
                    String hoTen = nhanVien.getHoTen();
                    return hoTen != null && hoTen.toLowerCase().contains(nhanVienName.toLowerCase());
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list in case of error
        }
    }
    
    @Override
    public double calculateTotalAmount(String hoaDonId) throws RemoteException {
        try {
            // Get the invoice with eagerly loaded chiTietHoaDons
            HoaDon hoaDon = hoaDonDAO.findByIdWithDetails(hoaDonId);
            
            if (hoaDon == null) {
                System.err.println("Warning: Invoice " + hoaDonId + " not found");
                return 0.0;
            }
            
            // Use the collection - it should never be null due to our safety in the entity class
            Set<ChiTietHoaDon> chiTietSet = hoaDon.getChiTietHoaDons();
            
            // If the collection is empty (not null), try loading directly as fallback
            if (chiTietSet.isEmpty()) {
                System.out.println("Debug: Invoice " + hoaDonId + " has empty chiTietHoaDons, trying direct fetch");
                List<ChiTietHoaDon> details = hoaDonDAO.findChiTietByHoaDonId(hoaDonId);
                if (details != null && !details.isEmpty()) {
                    double total = 0.0;
                    for (ChiTietHoaDon chiTiet : details) {
                        total += chiTiet.getSoLuong() * chiTiet.getDonGia();
                    }
                    return total;
                }
                return 0.0;
            }

            // Calculate the total amount
            double total = 0.0;
            for (ChiTietHoaDon chiTiet : chiTietSet) {
                total += chiTiet.getSoLuong() * chiTiet.getDonGia();
            }
            
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}