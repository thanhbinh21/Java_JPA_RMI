package rmi;

<<<<<<< HEAD
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
=======
import dao.impl.*;

>>>>>>> ccf501d8b7640859e186f0c6f8add2dcb25edccf
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
<<<<<<< HEAD
            NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl();
            PhieuNhapThuocDAO phieuNhapThuocDAO = new PhieuNhapThuocDAOImpl();
            ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO = new ChiTietPhieuNhapThuocDAOImpl();
=======
            VaiTroDAO vaiTroDAO = new VaiTroDAOImpl();
>>>>>>> ccf501d8b7640859e186f0c6f8add2dcb25edccf

            // Tạo các instance của remote object implementations
            ThuocService thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO);
            BanThuocService banThuocService = new BanThuocServiceImpl(thuocDAO, hoaDonDAO, khachHangDAO, chiTietHoaDonDAO, danhMucDAO);
            HoaDonService hoaDonService = new HoaDonServiceImpl(hoaDonDAO);
            KhachHangService khachHangService = new KhachHangServiceImpl(khachHangDAO);
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(taiKhoanDAO);
<<<<<<< HEAD
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
=======
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
>>>>>>> ccf501d8b7640859e186f0c6f8add2dcb25edccf

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}