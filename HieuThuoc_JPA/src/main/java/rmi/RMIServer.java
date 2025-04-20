package rmi;

import dao.impl.*;

import dao.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import service.*;
import service.impl.*;
import until.JPAUtil;

public class RMIServer {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(8989);

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
            VaiTroDAO vaiTroDAO = new VaiTroDAOImpl();

            // Tạo các instance của remote object implementations trực tiếp
            ThuocService thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO);
            BanThuocService banThuocService = new BanThuocServiceImpl(thuocDAO, hoaDonDAO, khachHangDAO, chiTietHoaDonDAO, danhMucDAO);
            KhachHangService khachHangService = new KhachHangServiceImpl(khachHangDAO);
            HoaDonService hoaDonService = new HoaDonServiceImpl(hoaDonDAO);
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(taiKhoanDAO);
            NhaSanXuatServiceImpl nhaSanXuatService = new NhaSanXuatServiceImpl(nhaSanXuatDAO);
            NhanVienServiceImpl nhanVienService = new NhanVienServiceImpl(nhanVienDAO);
            PhieuDatThuocService phieuDatThuocService = new PhieuDatThuocServiceImpl(phieuDatThuocDAO, chiTietPhieuDatThuocDAO);
            VaiTroService vaiTroService = new VaiTroServiceImpl(vaiTroDAO);
            DanhMucService danhMucService = new DanhMucServiceImpl(danhMucDAO);
            ChiTietHoaDonService chiTietHoaDonService = new ChiTietHoaDonServiceImpl(chiTietHoaDonDAO);

            // Đăng ký các remote object vào Registry với một tên
            registry.bind("ThuocService", thuocService);
            registry.bind("BanThuocService", banThuocService);
            registry.bind("KhachHangService", khachHangService);
            registry.bind("HoaDonService", hoaDonService);
            registry.bind("TaiKhoanService", taiKhoanService);
            registry.bind("NhaSanXuatService", nhaSanXuatService);
            registry.bind("NhanVienService", nhanVienService);
            registry.bind("PhieuDatThuocService", phieuDatThuocService);
            registry.bind("VaiTroService", vaiTroService);
            registry.bind("DanhMucService", danhMucService);
            registry.bind("ChiTietHoaDonService", chiTietHoaDonService);

            OneSessionService oneSessionServices = new OneSessionServiceImpl();
            registry.bind("OneSessionService", oneSessionServices);

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}