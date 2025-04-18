package test;

import dao.KhachHangDAO;
import dao.impl.KhachHangDAOImpl;
import entity.KhachHang;

import java.time.LocalDate;

public class KhachHangDAOTest {

    public static void main(String[] args) {
        KhachHangDAO khachHangDAO = new KhachHangDAOImpl();

        // Test save
        KhachHang khachHang1 = new KhachHang();
        khachHang1.setId("KH001"); // Gán ID thủ công
        khachHang1.setHoTen("Trần Thị B");
        khachHang1.setSoDienThoai("0987654321");
        khachHang1.setNgayThamGia(LocalDate.parse("2023-10-01"));
        boolean result = khachHangDAO.save(khachHang1);
        System.out.println("Đã lưu khách hàng: " + result);

        // Test findById
        KhachHang foundKhachHang = khachHangDAO.findById("KH001");
        if (foundKhachHang != null) {
            System.out.println("Tìm thấy khách hàng theo ID: " + foundKhachHang.getHoTen());
        } else {
            System.out.println("Không tìm thấy khách hàng theo ID: KH001");
        }

        // ... (các test khác bạn muốn thực hiện) ...
    }
}