package service.impl;

import dao.*;
import dao.impl.*;
import jakarta.persistence.EntityManager;
import service.*;
import until.JPAUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OneSessionServiceImpl extends UnicastRemoteObject implements OneSessionService {
    private EntityManager em;
    private ThuocDAO thuocDAO;
    private DanhMucDAO danhMucDAO;
    private KhachHangDAO khachHangDAO;
    private HoaDonDAO hoaDonDAO;
    private TaiKhoanDAO taiKhoanDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private NhaSanXuatDAO nhaSanXuatDAO;
    private NhanVienDAO nhanVienDAO;
    private PhieuDatThuocDAO phieuDatThuocDAO;
    private ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO;
    private VaiTroDAO vaiTroDAO;

    private ThuocService thuocService;
    private BanThuocService banThuocService;
    private KhachHangService khachHangService;
    private HoaDonService hoaDonService;
    private TaiKhoanService taiKhoanService;
    private NhaSanXuatServiceImpl nhaSanXuatService;
    private NhanVienServiceImpl nhanVienService;
    private PhieuDatThuocService phieuDatThuocService;
    private VaiTroService vaiTroService;
    private DanhMucService danhMucService;
    private ChiTietHoaDonService chiTietHoaDonService;

    public OneSessionServiceImpl(EntityManager em) throws RemoteException {
        this.em = em;
        createService();
    }

    public OneSessionServiceImpl() throws RemoteException {
        this.em = JPAUtil.getEntityManager();
        createService();
    }

    private void createService() throws RemoteException {
        thuocDAO = new ThuocDAOImpl(em);
        danhMucDAO = new DanhMucDAOImpl(em);
        khachHangDAO = new KhachHangDAOImpl(em);
        hoaDonDAO = new HoaDonDAOImpl(em);
        taiKhoanDAO = new TaiKhoanDAOImpl(em);
        chiTietHoaDonDAO = new ChiTietHoaDonDAOImpl(em);
        nhaSanXuatDAO = new NhaSanXuatDAOImpl(em);
        nhanVienDAO = new NhanVienDAOImpl(em);
        phieuDatThuocDAO = new PhieuDatThuocDAOImpl(em);
        chiTietPhieuDatThuocDAO = new ChiTietPhieuDatThuocDAOImpl(em);
        vaiTroDAO = new VaiTroDAOImpl(em);

        thuocService = new ThuocServiceImpl(thuocDAO, danhMucDAO);
        banThuocService = new BanThuocServiceImpl(thuocDAO, hoaDonDAO, khachHangDAO, chiTietHoaDonDAO, danhMucDAO);
        khachHangService = new KhachHangServiceImpl(khachHangDAO);
        hoaDonService = new HoaDonServiceImpl(hoaDonDAO);
        taiKhoanService = new TaiKhoanServiceImpl(taiKhoanDAO);
        nhaSanXuatService = new NhaSanXuatServiceImpl(nhaSanXuatDAO);
        nhanVienService = new NhanVienServiceImpl(nhanVienDAO);
        phieuDatThuocService = new PhieuDatThuocServiceImpl(phieuDatThuocDAO, chiTietPhieuDatThuocDAO);
        vaiTroService = new VaiTroServiceImpl(vaiTroDAO);
        danhMucService = new DanhMucServiceImpl(danhMucDAO);
        chiTietHoaDonService = new ChiTietHoaDonServiceImpl(chiTietHoaDonDAO);
    }

    @Override
    public BanThuocService getBanThuocService() throws RemoteException {
        return banThuocService;
    }

    @Override
    public ChiTietHoaDonService getChiTietHoaDonService() throws RemoteException {
        return chiTietHoaDonService;
    }

    @Override
    public DanhMucService getDanhMucService() throws RemoteException {
        return danhMucService;
    }

    @Override
    public HoaDonService getHoaDonService() throws RemoteException {
        return hoaDonService;
    }

    @Override
    public KhachHangService getKhachHangService() throws RemoteException {
        return khachHangService;
    }

    @Override
    public NhanVienService getNhanVienService() throws RemoteException {
        return nhanVienService;
    }

    @Override
    public NhaSanXuatService getNhaSanXuatService() throws RemoteException {
        return nhaSanXuatService;
    }

    @Override
    public PhieuDatThuocService getPhieuDatThuocService() throws RemoteException {
        return phieuDatThuocService;
    }

    @Override
    public TaiKhoanService getTaiKhoanService() throws RemoteException {
        return taiKhoanService;
    }

    @Override
    public ThuocService getThuocService() throws RemoteException {
        return thuocService;
    }

    @Override
    public VaiTroService getVaiTroService() throws RemoteException {
        return vaiTroService;
    }
}
