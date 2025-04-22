package test;

import dao.NhaCungCapDAO;
import dao.impl.NhaCungCapDAOImpl;
import entity.NhaCungCap;
import service.NhaCungCapService;

import java.rmi.Naming;
import java.util.List;
import java.util.UUID;

/**
 * Test class for Supplier (NhaCungCap) operations
 */
public class TestNhaCungCap {

    public static void main(String[] args) {
        try {
            // Connect to the RMI registry
//            String serviceUrl = "rmi://localhost:1099/NhaCungCapService";

            NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl();
            
            System.out.println("=== SUPPLIER MANAGEMENT TEST ===");
            
            // Test 1: List all suppliers before adding
            System.out.println("\n1. Listing all suppliers before adding:");
            List<NhaCungCap> suppliers = nhaCungCapDAO.findAll();
            if (suppliers.isEmpty()) {
                System.out.println("No suppliers found.");
            } else {
                suppliers.forEach(s -> System.out.println("- " + s.getId() + ": " + s.getTen() + ", " + s.getDiaChi() + ", " + s.getSdt()));
            }
            
            // Test 2: Add a new supplier
            System.out.println("\n2. Adding a new supplier:");
            NhaCungCap newSupplier = new NhaCungCap();
            // Generate a unique ID using current timestamp + random UUID part
            String supplierId = "NCC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
            newSupplier.setId(supplierId);
            newSupplier.setTen("Công ty Dược phẩm ABC");
            newSupplier.setDiaChi("123 Nguyễn Văn Linh, Quận 7, TP.HCM");
            newSupplier.setSdt("0987654321");
            
            boolean added = nhaCungCapDAO.save(newSupplier);
            System.out.println("Supplier added: " + (added ? "SUCCESS" : "FAILED"));
            
            // Test 3: Find the supplier by ID
            System.out.println("\n3. Finding the new supplier by ID:");
            NhaCungCap foundSupplier = nhaCungCapDAO.findById(supplierId);
            if (foundSupplier != null) {
                System.out.println("Found supplier: " + foundSupplier.getId() + ": " + 
                    foundSupplier.getTen() + ", " + foundSupplier.getDiaChi() + ", " + foundSupplier.getSdt());
            } else {
                System.out.println("Supplier not found.");
            }
            
//            // Test 4: Search supplier by name
//            System.out.println("\n4. Searching suppliers by name 'ABC':");
//            List<NhaCungCap> searchResults = nhaCungCapDAO.searchSuppliers("ABC");
//            if (searchResults.isEmpty()) {
//                System.out.println("No suppliers found with the search term.");
//            } else {
//                searchResults.forEach(s -> System.out.println("- " + s.getId() + ": " + s.getTen()));
//            }
            
            // Test 5: Find by exact name
//            System.out.println("\n5. Finding supplier by exact name:");
//            NhaCungCap foundByName = nhaCungCapDAO.findByTen("Công ty Dược phẩm ABC");
//            if (foundByName != null) {
//                System.out.println("Found supplier by name: " + foundByName.getId() + ": " + foundByName.getTen());
//            } else {
//                System.out.println("Supplier not found by exact name.");
//            }
//
//            // Test 6: List all suppliers after adding
//            System.out.println("\n6. Listing all suppliers after adding:");
//            List<NhaCungCap> updatedSuppliers = nhaCungCapService.findAll();
//            updatedSuppliers.forEach(s -> System.out.println("- " + s.getId() + ": " + s.getTen()));
            
            System.out.println("\n=== TEST COMPLETED ===");
            
        } catch (Exception e) {
            System.err.println("Error in TestNhaCungCap: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 