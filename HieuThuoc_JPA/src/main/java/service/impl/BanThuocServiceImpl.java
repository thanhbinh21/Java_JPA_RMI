package service.impl;

import dao.ChiTietHoaDonDAO;
import dao.DanhMucDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.ThuocDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.Thuoc;
import java.rmi.RemoteException;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.BanThuocService;
import until.JPAUtil;

public class BanThuocServiceImpl extends GenericServiceImpl<Thuoc, String> implements BanThuocService {

    private final ThuocDAO thuocDAO;
    private final HoaDonDAO hoaDonDAO;
    private final KhachHangDAO khachHangDAO;
    private final ChiTietHoaDonDAO chiTietHoaDonDAO;
    private final DanhMucDAO danhMucDAO;

    public BanThuocServiceImpl(ThuocDAO thuocDAO, HoaDonDAO hoaDonDAO, KhachHangDAO khachHangDAO,
                               ChiTietHoaDonDAO chiTietHoaDonDAO, DanhMucDAO danhMucDAO) throws RemoteException {
        super(thuocDAO); // Coi BanThuocService quản lý Thuoc cho các CRUD chung
        this.thuocDAO = thuocDAO;
        this.hoaDonDAO = hoaDonDAO;
        this.khachHangDAO = khachHangDAO;
        this.chiTietHoaDonDAO = chiTietHoaDonDAO;
        this.danhMucDAO = danhMucDAO;
    }

    @Override
    public List<Thuoc> getAllThuoc() throws RemoteException {
        return thuocDAO.findAll();
    }

    @Override
    public Thuoc getThuocById(String maThuoc) throws RemoteException {
        return thuocDAO.findById(maThuoc);
    }

    @Override
    public List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException {
        return thuocDAO.selectByDanhMuc(danhMucDAO.findById(maDM));
    }

    @Override
    public List<Thuoc> searchThuoc(String keyword) throws RemoteException {
        return thuocDAO.searchByKeyword(keyword);
    }

    @Override
    public boolean addThuocToCart(List<ChiTietHoaDon> cart, Thuoc thuoc, int soLuong) throws RemoteException {
        // Triển khai logic
        return false;
    }

    @Override
    public boolean updateCartItemQuantity(List<ChiTietHoaDon> cart, String maThuoc, int newQuantity) throws RemoteException {
        // Triển khai logic
        return false;
    }

    @Override
    public boolean removeThuocFromCart(List<ChiTietHoaDon> cart, String maThuoc) throws RemoteException {
        // Triển khai logic
        return false;
    }

    @Override
    public synchronized boolean createHoaDon(HoaDon hoaDon, List<ChiTietHoaDon> cart) throws RemoteException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1. Xử lý khách hàng
            KhachHang khachHang = hoaDon.getKhachHang();
            if (khachHang != null) {
                KhachHang existingKH = khachHangDAO.selectBySdt(khachHang.getSoDienThoai());
                if (existingKH == null) {
                    em.persist(khachHang); // save khách hàng
                } else {
                    hoaDon.setKhachHang(existingKH);
                }
            }

            // 2. Lưu hóa đơn
            em.persist(hoaDon);
            em.flush(); // Đảm bảo ID được sinh

            // 3. Lưu chi tiết hóa đơn
            for (ChiTietHoaDon item : cart) {
                Thuoc thuoc = em.find(Thuoc.class, item.getThuoc().getId());
                if (thuoc == null) {
                    System.err.println("Không tìm thấy thuốc: " + item.getThuoc().getId());
                    tx.rollback();
                    return false;
                }

                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setHoaDon(hoaDon);
                cthd.setThuoc(thuoc);
                cthd.setSoLuong(item.getSoLuong());
                cthd.setDonGia(item.getDonGia());

                em.persist(cthd);

                // Cập nhật số lượng tồn
                thuoc.setSoLuongTon(thuoc.getSoLuongTon() - item.getSoLuong());
                em.merge(thuoc);
            }

            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
            return false;
        } finally {
            em.close();
        }
    }


    @Override
    public KhachHang getKhachHangBySdt(String sdt) throws RemoteException {
        return khachHangDAO.selectBySdt(sdt);
    }

    @Override
    public boolean createKhachHang(KhachHang khachHang) throws RemoteException {
        return khachHangDAO.save(khachHang);
    }
}