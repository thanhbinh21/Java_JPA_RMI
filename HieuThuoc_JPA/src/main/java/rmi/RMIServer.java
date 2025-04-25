package rmi;

import dao.impl.*;
import dao.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import service.*;
import service.impl.*;

public class RMIServer {

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "172.28.86.78"); // IP của máy chủ
            Registry registry = LocateRegistry.createRegistry(8989);

            //Registry registry = LocateRegistry.createRegistry(8989);
            //Hostname



            // === DAO khởi tạo dùng chung ===
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
            KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAOImpl();
            NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl();
            PhieuNhapThuocDAO phieuNhapThuocDAO = new PhieuNhapThuocDAOImpl();
            ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO = new ChiTietPhieuNhapThuocDAOImpl();
            VaiTroDAO vaiTroDAO = new VaiTroDAOImpl();

            // === Các service bind  ===
            BanThuocService banThuocService = new BanThuocServiceImpl(thuocDAO, hoaDonDAO, khachHangDAO, chiTietHoaDonDAO, danhMucDAO);
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(taiKhoanDAO);
            DatThuocSevice datThuocService = new DatThuocServiceImpl(phieuDatThuocDAO, thuocDAO, khachHangDAO, chiTietPhieuDatThuocDAO);
            PhieuDatThuocService phieuDatThuocService = new PhieuDatThuocServiceImpl(phieuDatThuocDAO, chiTietPhieuDatThuocDAO);
            NhaCungCapService nhaCungCapService = new NhaCungCapServiceImpl(nhaCungCapDAO);
            PhieuNhapThuocService phieuNhapThuocService = new PhieuNhapThuocServiceImpl(
                    phieuNhapThuocDAO, nhaCungCapDAO, nhanVienDAO, chiTietPhieuNhapThuocDAO, thuocDAO);
            ChiTietPhieuNhapThuocService chiTietPhieuNhapThuocService = new ChiTietPhieuNhapThuocServiceImpl(
                    chiTietPhieuNhapThuocDAO, phieuNhapThuocDAO, thuocDAO);
            NhaSanXuatService nhaSanXuatService = new NhaSanXuatServiceImpl(nhaSanXuatDAO);
            NhanVienService nhanVienService = new NhanVienServiceImpl(nhanVienDAO);
            VaiTroService vaiTroService = new VaiTroServiceImpl(vaiTroDAO);
            DanhMucService danhMucService = new DanhMucServiceImpl(danhMucDAO);
           // ChiTietHoaDonService chiTietHoaDonService = new ChiTietHoaDonServiceImpl(chiTietHoaDonDAO);
            KhuyenMaiService khuyenMaiService = new KhuyenMaiServiceImpl(khuyenMaiDAO);
            OneSessionService oneSessionService = new OneSessionServiceImpl();

            // === Bind các service bình thường ===
            registry.rebind("BanThuocService", banThuocService);
            registry.rebind("TaiKhoanService", taiKhoanService);
            registry.rebind("DatThuocService", datThuocService);
            registry.rebind("PhieuDatThuocService", phieuDatThuocService);
            registry.rebind("NhaCungCapService", nhaCungCapService);
            registry.rebind("PhieuNhapThuocService", phieuNhapThuocService);
            registry.rebind("ChiTietPhieuNhapThuocService", chiTietPhieuNhapThuocService);
            registry.rebind("NhaSanXuatService", nhaSanXuatService);
            registry.rebind("NhanVienService", nhanVienService);
            registry.rebind("VaiTroService", vaiTroService);
            registry.rebind("DanhMucService", danhMucService);
            //registry.rebind("ChiTietHoaDonService", chiTietHoaDonService);
            registry.rebind("KhuyenMaiService", khuyenMaiService);
            registry.rebind("OneSessionService", oneSessionService);

            // === Bind 3 service chính  ===
            ExecutorService executor = Executors.newFixedThreadPool(4);

            executor.submit(() -> {
                try {
                    ThuocService thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO, nhaSanXuatDAO, khuyenMaiDAO);
                    registry.rebind("ThuocService", thuocService);
                    System.out.println("✔ ThuocService ready");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            executor.submit(() -> {
                try {
                    KhachHangService khachHangService = new KhachHangServiceImpl(khachHangDAO);
                    registry.rebind("KhachHangService", khachHangService);
                    System.out.println("✔ KhachHangService ready");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            executor.submit(() -> {
                try {
                    HoaDonService hoaDonService = new HoaDonServiceImpl(hoaDonDAO);
                    registry.rebind("HoaDonService", hoaDonService);
                    System.out.println("✔ HoaDonService ready");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // CHitietHoaDon
            executor.submit(() -> {
                try {
                    ChiTietHoaDonService chiTietHoaDonService = new ChiTietHoaDonServiceImpl(chiTietHoaDonDAO);
                    registry.rebind("ChiTietHoaDonService", chiTietHoaDonService);
                    System.out.println("✔ ChiTietHoaDonService ready");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            executor.shutdown();

            System.out.println("🚀 RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
