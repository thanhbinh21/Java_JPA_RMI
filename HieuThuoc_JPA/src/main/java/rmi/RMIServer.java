package rmi;

import dao.impl.ThuocDAOImpl;
import dao.impl.DanhMucDAOImpl;
import dao.impl.KhachHangDAOImpl;
import dao.impl.HoaDonDAOImpl;
import dao.impl.TaiKhoanDAOImpl;
import dao.impl.ChiTietHoaDonDAOImpl;
import dao.impl.NhaSanXuatDAOImpl;
import dao.impl.NhanVienDAOImpl;
import dao.impl.PhieuDatThuocDAOImpl;
import dao.impl.ChiTietPhieuDatThuocDAOImpl;
import dao.impl.NhaCungCapDAOImpl;
import dao.impl.PhieuNhapThuocDAOImpl;
import dao.impl.ChiTietPhieuNhapThuocDAOImpl;
import dao.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.*;
import service.impl.*;

public class RMIServer {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            // Khởi tạo các DAO
            ThuocDAO thuocDAO = new ThuocDAOImpl();
            DanhMucDAO danhMucDAO = new DanhMucDAOImpl();
            KhachHangDAO khachHangDAO = new KhachHangDAOImpl();
            HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
            TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAOImpl();
            ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAOImpl();
            NhaSanXuatDAO nhaSanXuatDAO = new NhaSanXuatDAOImpl();
            NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();
            PhieuDatThuocDAO phieuDatThuocDAO = new PhieuDatThuocDAOImpl();
            ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO = new ChiTietPhieuDatThuocDAOImpl();
            NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl();
            PhieuNhapThuocDAO phieuNhapThuocDAO = new PhieuNhapThuocDAOImpl();
            ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO = new ChiTietPhieuNhapThuocDAOImpl();

            // Tạo các instance của remote object implementations
            ThuocService thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO);
            BanThuocService banThuocService = new BanThuocServiceImpl(thuocDAO, hoaDonDAO, khachHangDAO, chiTietHoaDonDAO, danhMucDAO);
            HoaDonService hoaDonService = new HoaDonServiceImpl(hoaDonDAO);
            KhachHangService khachHangService = new KhachHangServiceImpl(khachHangDAO);
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(taiKhoanDAO);
            PhieuDatThuocService phieuDatThuocService = new PhieuDatThuocServiceImpl(phieuDatThuocDAO, chiTietPhieuDatThuocDAO);
            
            // Khởi tạo các service mới
            NhaCungCapService nhaCungCapService = new NhaCungCapServiceImpl(nhaCungCapDAO);
            PhieuNhapThuocService phieuNhapThuocService = new PhieuNhapThuocServiceImpl(
                phieuNhapThuocDAO, nhaCungCapDAO, nhanVienDAO, chiTietPhieuNhapThuocDAO, thuocDAO);
            ChiTietPhieuNhapThuocService chiTietPhieuNhapThuocService = new ChiTietPhieuNhapThuocServiceImpl(
                chiTietPhieuNhapThuocDAO, phieuNhapThuocDAO, thuocDAO);

            // Đăng ký các dịch vụ RMI
            registry.rebind("ThuocService", thuocService);
            registry.rebind("BanThuocService", banThuocService);
            registry.rebind("HoaDonService", hoaDonService);
            registry.rebind("KhachHangService", khachHangService);
            registry.rebind("TaiKhoanService", taiKhoanService);
            registry.rebind("PhieuDatThuocService", phieuDatThuocService);
            
            // Đăng ký các dịch vụ mới
            registry.rebind("NhaCungCapService", nhaCungCapService);
            registry.rebind("PhieuNhapThuocService", phieuNhapThuocService);
            registry.rebind("ChiTietPhieuNhapThuocService", chiTietPhieuNhapThuocService);

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}