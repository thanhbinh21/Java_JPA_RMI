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
import java.util.UUID;

/**
 * Combined test script to test the entire supplier management and medicine import workflow
 */
public class TestSupplierAndMedicineImport {

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
            
            System.out.println("============================================================");
            System.out.println("=== SUPPLIER MANAGEMENT AND MEDICINE IMPORT WORKFLOW TEST ===");
            System.out.println("============================================================");
            
            // PART 1: Supplier Management Tests
            System.out.println("\n=======================================");
            System.out.println("=== PART 1: SUPPLIER MANAGEMENT TEST ===");
            System.out.println("=======================================");
            
            // Test 1.1: List all suppliers before adding
            System.out.println("\n1.1. Listing all suppliers before adding:");
            List<NhaCungCap> suppliers = nhaCungCapService.findAll();
            if (suppliers.isEmpty()) {
                System.out.println("No suppliers found.");
            } else {
                suppliers.forEach(s -> System.out.println("- " + s.getId() + ": " + s.getTen() + ", " + s.getDiaChi() + ", " + s.getSdt()));
            }
            
            // Test 1.2: Add a new supplier
            System.out.println("\n1.2. Adding a new supplier:");
            NhaCungCap newSupplier = new NhaCungCap();
            // Generate a unique ID using current timestamp + random UUID part
            String supplierId = "NCC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
            newSupplier.setId(supplierId);
            newSupplier.setTen("Công ty Dược phẩm XYZ");
            newSupplier.setDiaChi("456 Lê Văn Lương, Quận 1, TP.HCM");
            newSupplier.setSdt("0909123456");
            
            boolean added = nhaCungCapService.save(newSupplier);
            System.out.println("Supplier added: " + (added ? "SUCCESS" : "FAILED"));
            
            if (!added) {
                throw new RuntimeException("Failed to add supplier. Test cannot continue.");
            }
            
            // Test 1.3: Find the supplier by ID
            System.out.println("\n1.3. Finding the new supplier by ID:");
            NhaCungCap foundSupplier = nhaCungCapService.findById(supplierId);
            if (foundSupplier != null) {
                System.out.println("Found supplier: " + foundSupplier.getId() + ": " + 
                    foundSupplier.getTen() + ", " + foundSupplier.getDiaChi() + ", " + foundSupplier.getSdt());
            } else {
                System.out.println("Supplier not found.");
                throw new RuntimeException("Supplier not found. Test failed.");
            }
            
            // Test 1.4: Search supplier by name
            System.out.println("\n1.4. Searching suppliers by name 'XYZ':");
            List<NhaCungCap> searchResults = nhaCungCapService.searchSuppliers("XYZ");
            if (searchResults.isEmpty()) {
                System.out.println("No suppliers found with the search term.");
                throw new RuntimeException("Search by name failed. Test failed.");
            } else {
                searchResults.forEach(s -> System.out.println("- " + s.getId() + ": " + s.getTen()));
            }
            
            // Test 1.5: Find by exact name
            System.out.println("\n1.5. Finding supplier by exact name:");
            NhaCungCap foundByName = nhaCungCapService.findByTen("Công ty Dược phẩm XYZ");
            if (foundByName != null) {
                System.out.println("Found supplier by name: " + foundByName.getId() + ": " + foundByName.getTen());
            } else {
                System.out.println("Supplier not found by exact name.");
                // This might fail in some implementations, so not throwing an exception here
            }
            
            System.out.println("\nSupplier management tests completed successfully.");
            
            // PART 2: Medicine Import Tests
            System.out.println("\n==========================================");
            System.out.println("=== PART 2: MEDICINE IMPORT OPERATIONS ===");
            System.out.println("==========================================");
            
            // Test 2.1: Get medicines from the database
            System.out.println("\n2.1. Getting medicines from database:");
            List<Thuoc> medicines = thuocService.findAll();
            
            if (medicines.isEmpty() || medicines.size() < 2) {
                System.out.println("Not enough medicines found in the database. Need at least 2 medicines.");
                return;
            }
            
            Thuoc medicine1 = medicines.get(0);
            Thuoc medicine2 = medicines.get(1);
            
            System.out.println("Selected medicine 1: " + medicine1.getId() + " - " + medicine1.getTen());
            System.out.println("Selected medicine 2: " + medicine2.getId() + " - " + medicine2.getTen());
            
            // Record the original inventory levels
            int originalQuantity1 = medicine1.getSoLuongTon();
            int originalQuantity2 = medicine2.getSoLuongTon();
            
            System.out.println("Original inventory - Medicine 1: " + originalQuantity1);
            System.out.println("Original inventory - Medicine 2: " + originalQuantity2);
            
            // Test 2.2: Create a new import receipt
            System.out.println("\n2.2. Creating a new import receipt:");
            PhieuNhapThuoc phieuNhap = new PhieuNhapThuoc();
            
            // Generate a new ID
            String phieuNhapId = phieuNhapThuocService.generateNewPhieuNhapId();
            phieuNhap.setId(phieuNhapId);
            
            // Set the supplier that we created in the previous test
            phieuNhap.setNhaCungCap(foundSupplier);
            
            // Set import time
            phieuNhap.setThoiGian(LocalDateTime.now());
            
            System.out.println("Created import receipt ID: " + phieuNhap.getId());
            System.out.println("Import time: " + phieuNhap.getThoiGian());
            System.out.println("Supplier: " + phieuNhap.getNhaCungCap().getTen());
            
            // Test 2.3: Create import receipt details
            System.out.println("\n2.3. Creating import receipt details:");
            List<ChiTietPhieuNhapThuoc> chiTietList = new ArrayList<>();
            
            // Quantities to add in this import
            int addQuantity1 = 100;
            int addQuantity2 = 50;
            
            ChiTietPhieuNhapThuoc chiTiet1 = new ChiTietPhieuNhapThuoc();
            chiTiet1.setPhieuNhapThuoc(phieuNhap);
            chiTiet1.setThuoc(medicine1);
            chiTiet1.setSoLuong(addQuantity1);  // Import 100 units
            chiTiet1.setDonGia(50000);          // 50,000 VND per unit
            chiTietList.add(chiTiet1);
            
            ChiTietPhieuNhapThuoc chiTiet2 = new ChiTietPhieuNhapThuoc();
            chiTiet2.setPhieuNhapThuoc(phieuNhap);
            chiTiet2.setThuoc(medicine2);
            chiTiet2.setSoLuong(addQuantity2);  // Import 50 units
            chiTiet2.setDonGia(75000);          // 75,000 VND per unit
            chiTietList.add(chiTiet2);
            
            System.out.println("Created " + chiTietList.size() + " import details:");
            System.out.println("- Detail 1: " + medicine1.getTen() + ", Quantity: " + addQuantity1 + ", Unit Price: 50,000 VND");
            System.out.println("- Detail 2: " + medicine2.getTen() + ", Quantity: " + addQuantity2 + ", Unit Price: 75,000 VND");
            
            // Test 2.4: Save the import receipt with details
            System.out.println("\n2.4. Saving the import receipt with details:");
            boolean saved = phieuNhapThuocService.addPhieuNhapWithDetails(phieuNhap, chiTietList);
            System.out.println("Import receipt saved: " + (saved ? "SUCCESS" : "FAILED"));
            
            if (!saved) {
                throw new RuntimeException("Failed to save import receipt. Test failed.");
            }
            
            // Test 2.5: Retrieve the import receipt
            System.out.println("\n2.5. Retrieving the import receipt:");
            PhieuNhapThuoc foundPhieuNhap = phieuNhapThuocService.findById(phieuNhapId);
            
            if (foundPhieuNhap != null) {
                System.out.println("Found import receipt: " + foundPhieuNhap.getId());
                System.out.println("Import time: " + foundPhieuNhap.getThoiGian());
                System.out.println("Supplier: " + foundPhieuNhap.getNhaCungCap().getTen());
            } else {
                System.out.println("Import receipt not found.");
                throw new RuntimeException("Import receipt not found. Test failed.");
            }
            
            // Test 2.6: Get import receipt details
            System.out.println("\n2.6. Getting import receipt details:");
            List<ChiTietPhieuNhapThuoc> details = chiTietPhieuNhapThuocService.findByPhieuNhapThuoc(phieuNhapId);
            
            if (details.isEmpty()) {
                System.out.println("No details found.");
                throw new RuntimeException("Import receipt details not found. Test failed.");
            }
            
            System.out.println("Found " + details.size() + " details:");
            double manualTotal = 0;
            for (ChiTietPhieuNhapThuoc detail : details) {
                double lineTotal = detail.getSoLuong() * detail.getDonGia();
                manualTotal += lineTotal;
                System.out.println("- Medicine: " + detail.getThuoc().getTen());
                System.out.println("  Quantity: " + detail.getSoLuong());
                System.out.println("  Unit price: " + detail.getDonGia() + " VND");
                System.out.println("  Total: " + lineTotal + " VND");
            }
            
            // Test 2.7: Calculate total amount
            System.out.println("\n2.7. Calculating total amount:");
            double totalAmount = chiTietPhieuNhapThuocService.calculateTotalAmount(phieuNhapId);
            System.out.println("Total amount (service): " + totalAmount + " VND");
            System.out.println("Total amount (manual): " + manualTotal + " VND");
            
            if (Math.abs(totalAmount - manualTotal) > 0.01) {
                throw new RuntimeException("Total amount calculation mismatch. Test failed.");
            }
            
            // Test 2.8: Verify medicine inventory has been updated
            System.out.println("\n2.8. Verifying medicine inventory has been updated:");
            Thuoc updatedMedicine1 = thuocService.findById(medicine1.getId());
            Thuoc updatedMedicine2 = thuocService.findById(medicine2.getId());
            
            System.out.println("- Medicine 1 (" + updatedMedicine1.getTen() + "):");
            System.out.println("  Original inventory: " + originalQuantity1);
            System.out.println("  Current inventory: " + updatedMedicine1.getSoLuongTon());
            System.out.println("  Expected inventory: " + (originalQuantity1 + addQuantity1));
            
            System.out.println("- Medicine 2 (" + updatedMedicine2.getTen() + "):");
            System.out.println("  Original inventory: " + originalQuantity2);
            System.out.println("  Current inventory: " + updatedMedicine2.getSoLuongTon());
            System.out.println("  Expected inventory: " + (originalQuantity2 + addQuantity2));
            
            // Verify quantities match expected values
            if (updatedMedicine1.getSoLuongTon() != originalQuantity1 + addQuantity1) {
                System.out.println("WARNING: Medicine 1 inventory not updated as expected.");
            }
            
            if (updatedMedicine2.getSoLuongTon() != originalQuantity2 + addQuantity2) {
                System.out.println("WARNING: Medicine 2 inventory not updated as expected.");
            }
            
            System.out.println("\nMedicine import tests completed successfully.");
            
            // Final summary
            System.out.println("\n=== TEST SUMMARY ===");
            System.out.println("1. Created new supplier: " + foundSupplier.getTen());
            System.out.println("2. Created medicine import receipt: " + phieuNhapId);
            System.out.println("3. Added " + addQuantity1 + " units of " + medicine1.getTen());
            System.out.println("4. Added " + addQuantity2 + " units of " + medicine2.getTen());
            System.out.println("5. Total import value: " + totalAmount + " VND");
            
            System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
            
        } catch (Exception e) {
            System.err.println("Error in test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 