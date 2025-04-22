package test;

import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dao.PhieuDatThuocDAO;
import dao.ThuocDAO;
import dao.impl.KhachHangDAOImpl;
import dao.impl.NhanVienDAOImpl;
import dao.impl.PhieuDatThuocDAOImpl;
import dao.impl.ThuocDAOImpl;
import entity.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PhieuDatThuocDAOTest {
    public static void main(String[] args) throws RemoteException {
        PhieuDatThuocDAO phieuDatThuocDAO = new PhieuDatThuocDAOImpl();
        KhachHangDAO khachHangDAO = new KhachHangDAOImpl();
        NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();
        ThuocDAO thuocDAO = new ThuocDAOImpl();

        // 👉 Lấy dữ liệu liên quan (đảm bảo đã có trong DB)
        KhachHang khachHang = khachHangDAO.findById("KH001");
        NhanVien nhanVien = nhanVienDAO.findById("NV001");
        Thuoc thuoc = thuocDAO.findById("T001");

        if (khachHang == null) {
            System.out.println("❌ Lỗi: Không tìm thấy KH");
            return;
        }
        if (nhanVien == null){
            System.out.println("❌ Lỗi: Không tìm thấy  NV !");
            return;
        }
        if (thuoc == null){
            System.out.println("❌ Lỗi: Không tìm thấy  thuốc !");
            return;
        }


        // 👉 Tạo phiếu đặt thuốc
        PhieuDatThuoc phieu = new PhieuDatThuoc();
        phieu.setId("PDT001");
        phieu.setThoiGian(new Timestamp(System.currentTimeMillis()));
        phieu.setKhachHang(khachHang);
        phieu.setNhanVien(nhanVien);
        phieu.setTrangThai(true); // ví dụ: true là "đã xác nhận"

        // 👉 Tạo chi tiết phiếu
        ChiTietPhieuDatThuoc chiTiet = new ChiTietPhieuDatThuoc();
        chiTiet.setPhieuDatThuoc(phieu);
        chiTiet.setThuoc(thuoc);
        chiTiet.setSoLuong(10);
        chiTiet.setDonGia(thuoc.getDonGia()); // giả sử Thuoc có getGiaBan()

        // 👉 Gắn chi tiết vào phiếu
        Set<ChiTietPhieuDatThuoc> chiTietSet = new HashSet<>();
        chiTietSet.add(chiTiet);
        phieu.setChiTietPhieuDatThuocs(chiTietSet);

        // 👉 Gọi hàm thêm
        boolean result = phieuDatThuocDAO.addPhieuDatThuoc(phieu);
        System.out.println("✅ Thêm phiếu đặt thuốc thành công? " + result);
    }
}
