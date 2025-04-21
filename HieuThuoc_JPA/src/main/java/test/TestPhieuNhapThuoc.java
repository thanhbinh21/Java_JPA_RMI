package test;

import entity.ChiTietPhieuNhapThuoc;
import entity.NhaCungCap;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import service.ChiTietPhieuNhapThuocService;
import service.NhaCungCapService;
import service.PhieuNhapThuocService;
import service.ThuocService;

import java.rmi.Naming;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Medicine Import (PhieuNhapThuoc) operations
 */
public class TestPhieuNhapThuoc {

    public static void main(String[] args) {
        try {
            // Connect to the RMI registry for all required services
            System.out.println("Connecting to RMI services...");
            String nhaCungCapUrl = "rmi://localhost:1099/NhaCungCapService";
            String phieuNhapThuocUrl = "rmi://localhost:1099/PhieuNhapThuocService";
            String chiTietPhieuNhapThuocUrl = "rmi://localhost:1099/ChiTietPhieuNhapThuocService";
            String thuocUrl = "rmi://localhost:1099/ThuocService";
            
            NhaCungCapService nhaCungCapService = (NhaCungCapService) Naming.lookup(nhaCungCapUrl);
            PhieuNhapThuocService phieuNhapThuocService = (PhieuNhapThuocService) Naming.lookup(phieuNhapThuocUrl);
            ChiTietPhieuNhapThuocService chiTietPhieuNhapThuocService = (ChiTietPhieuNhapThuocService) Naming.lookup(chiTietPhieuNhapThuocUrl);
            ThuocService thuocService = (ThuocService) Naming.lookup(thuocUrl);
            
            System.out.println("=== MEDICINE IMPORT TEST ===");
            
            // Test 1: Get a supplier from the database
            System.out.println("\n1. Getting a supplier from database:");
            List<NhaCungCap> suppliers = nhaCungCapService.findAll();
            
            if (suppliers.isEmpty()) {
                System.out.println("No suppliers found. Please add a supplier first using TestNhaCungCap.");
                return;
            }
            
            NhaCungCap supplier = suppliers.get(0);
            System.out.println("Selected supplier: " + supplier.getId() + " - " + supplier.getTen());
            
            // Test 2: Get medicines from the database
            System.out.println("\n2. Getting medicines from database:");
            List<Thuoc> medicines = thuocService.findAll();
            
            if (medicines.isEmpty() || medicines.size() < 2) {
                System.out.println("Not enough medicines found in the database. Need at least 2 medicines.");
                return;
            }
            
            Thuoc medicine1 = medicines.get(0);
            Thuoc medicine2 = medicines.get(1);
            
            System.out.println("Selected medicine 1: " + medicine1.getId() + " - " + medicine1.getTen());
            System.out.println("Selected medicine 2: " + medicine2.getId() + " - " + medicine2.getTen());
            
            // Test 3: Create a new import receipt
            System.out.println("\n3. Creating a new import receipt:");
            PhieuNhapThuoc phieuNhap = new PhieuNhapThuoc();
            
            // Generate a new ID
            String phieuNhapId = phieuNhapThuocService.generateNewPhieuNhapId();
            phieuNhap.setId(phieuNhapId);
            
            // Set supplier and time
            phieuNhap.setNhaCungCap(supplier);
            phieuNhap.setThoiGian(LocalDateTime.now());
            
            System.out.println("Created import receipt ID: " + phieuNhap.getId());
            System.out.println("Import time: " + phieuNhap.getThoiGian());
            System.out.println("Supplier: " + phieuNhap.getNhaCungCap().getTen());
            
            // Test 4: Create import receipt details
            System.out.println("\n4. Creating import receipt details:");
            List<ChiTietPhieuNhapThuoc> chiTietList = new ArrayList<>();
            
            ChiTietPhieuNhapThuoc chiTiet1 = new ChiTietPhieuNhapThuoc();
            chiTiet1.setPhieuNhapThuoc(phieuNhap);
            chiTiet1.setThuoc(medicine1);
            chiTiet1.setSoLuong(100);  // Import 100 units
            chiTiet1.setDonGia(50000); // 50,000 VND per unit
            chiTietList.add(chiTiet1);
            
            ChiTietPhieuNhapThuoc chiTiet2 = new ChiTietPhieuNhapThuoc();
            chiTiet2.setPhieuNhapThuoc(phieuNhap);
            chiTiet2.setThuoc(medicine2);
            chiTiet2.setSoLuong(50);   // Import 50 units
            chiTiet2.setDonGia(75000); // 75,000 VND per unit
            chiTietList.add(chiTiet2);
            
            System.out.println("Created " + chiTietList.size() + " import details:");
            System.out.println("- Detail 1: " + medicine1.getTen() + ", Quantity: 100, Unit Price: 50,000 VND");
            System.out.println("- Detail 2: " + medicine2.getTen() + ", Quantity: 50, Unit Price: 75,000 VND");
            
            // Test 5: Save the import receipt with details
            System.out.println("\n5. Saving the import receipt with details:");
            boolean saved = phieuNhapThuocService.addPhieuNhapWithDetails(phieuNhap, chiTietList);
            System.out.println("Import receipt saved: " + (saved ? "SUCCESS" : "FAILED"));
            
            // Test 6: Retrieve the import receipt
            System.out.println("\n6. Retrieving the import receipt:");
            PhieuNhapThuoc foundPhieuNhap = phieuNhapThuocService.findById(phieuNhapId);
            
            if (foundPhieuNhap != null) {
                System.out.println("Found import receipt: " + foundPhieuNhap.getId());
                System.out.println("Import time: " + foundPhieuNhap.getThoiGian());
                System.out.println("Supplier: " + foundPhieuNhap.getNhaCungCap().getTen());
            } else {
                System.out.println("Import receipt not found.");
            }
            
            // Test 7: Get import receipt details
            System.out.println("\n7. Getting import receipt details:");
            List<ChiTietPhieuNhapThuoc> details = chiTietPhieuNhapThuocService.findByPhieuNhapThuoc(phieuNhapId);
            
            if (!details.isEmpty()) {
                System.out.println("Found " + details.size() + " details:");
                for (ChiTietPhieuNhapThuoc detail : details) {
                    System.out.println("- Medicine: " + detail.getThuoc().getTen());
                    System.out.println("  Quantity: " + detail.getSoLuong());
                    System.out.println("  Unit price: " + detail.getDonGia() + " VND");
                    System.out.println("  Total: " + (detail.getSoLuong() * detail.getDonGia()) + " VND");
                }
            } else {
                System.out.println("No details found.");
            }
            
            // Test 8: Calculate total amount
            System.out.println("\n8. Calculating total amount:");
            double totalAmount = chiTietPhieuNhapThuocService.calculateTotalAmount(phieuNhapId);
            System.out.println("Total amount: " + totalAmount + " VND");
            
            // Test 9: Verify medicine inventory has been updated
            System.out.println("\n9. Verifying medicine inventory has been updated:");
            Thuoc updatedMedicine1 = thuocService.findById(medicine1.getId());
            Thuoc updatedMedicine2 = thuocService.findById(medicine2.getId());
            
            System.out.println("- Medicine 1 (" + updatedMedicine1.getTen() + "):");
            System.out.println("  Current inventory: " + updatedMedicine1.getSoLuongTon());
            System.out.println("  Current price: " + updatedMedicine1.getDonGia() + " VND");
            
            System.out.println("- Medicine 2 (" + updatedMedicine2.getTen() + "):");
            System.out.println("  Current inventory: " + updatedMedicine2.getSoLuongTon());
            System.out.println("  Current price: " + updatedMedicine2.getDonGia() + " VND");
            
            System.out.println("\n=== TEST COMPLETED ===");
            
        } catch (Exception e) {
            System.err.println("Error in TestPhieuNhapThuoc: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 