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
//            NhaSanXuatDAO nhaSanXuatDAO = new NhaSanXuatDAOImpl();
//            NhanVienDAO nhanVienDAO = new NhanVienDAOImpl();
            PhieuDatThuocDAO phieuDatThuocDAO = new PhieuDatThuocDAOImpl();
            ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO = new ChiTietPhieuDatThuocDAOImpl();

            // Tạo các instance của remote object implementations trực tiếp
            ThuocService thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO);
            BanThuocService banThuocService = new BanThuocServiceImpl(thuocDAO, hoaDonDAO, khachHangDAO, chiTietHoaDonDAO, danhMucDAO);
            KhachHangService khachHangService = new KhachHangServiceImpl(khachHangDAO);
            HoaDonService hoaDonService = new HoaDonServiceImpl(hoaDonDAO);
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(taiKhoanDAO);
//            NhaSanXuatServiceImpl nhaSanXuatService = new NhaSanXuatServiceImpl(nhaSanXuatDAO);
//            NhanVienServiceImpl nhanVienService = new NhanVienServiceImpl(nhanVienDAO);
            PhieuDatThuocService phieuDatThuocService = new PhieuDatThuocServiceImpl(phieuDatThuocDAO, chiTietPhieuDatThuocDAO);

            // Đăng ký các remote object vào Registry với một tên
            registry.bind("ThuocService", thuocService);
            registry.bind("BanThuocService", banThuocService);
            registry.bind("KhachHangService", khachHangService);
            registry.bind("HoaDonService", hoaDonService);
            registry.bind("TaiKhoanService", taiKhoanService);
//            registry.bind("NhaSanXuatService", nhaSanXuatService);
//            registry.bind("NhanVienService", nhanVienService);
            registry.bind("PhieuDatThuocService", phieuDatThuocService);

            System.out.println("RMI Server is running...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}