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
import service.BanThuocService;

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
    public boolean createHoaDon(HoaDon hoaDon, List<ChiTietHoaDon> cart) throws RemoteException {
        try {
            KhachHang khachHang = hoaDon.getKhachHang();
            if (khachHang != null) {
                KhachHang existingKH = khachHangDAO.selectBySdt(khachHang.getSoDienThoai());
                if (existingKH == null) {
                    boolean saveKhachHangResult = khachHangDAO.save(khachHang);
                    if (!saveKhachHangResult) {
                        return false;
                    }
                } else {
                    hoaDon.setKhachHang(existingKH);
                }
            }
            boolean saveHoaDonResult = hoaDonDAO.save(hoaDon);
            if (!saveHoaDonResult) {
                return false;
            }
            
            jakarta.persistence.EntityManager em = until.JPAUtil.getEntityManager();
            jakarta.persistence.EntityTransaction tx = null;
            
            try {
                for (ChiTietHoaDon cartItem : cart) {
                    tx = em.getTransaction();
                    tx.begin();
                    
                    HoaDon freshHoaDon = em.find(HoaDon.class, hoaDon.getId());
                    
                    String thuocId = cartItem.getThuoc().getId();
                    Thuoc freshThuoc = em.find(Thuoc.class, thuocId);
                    
                    if (freshThuoc == null) {
                        tx.rollback();
                        System.err.println("Could not find Thuoc with ID: " + thuocId);
                        continue;
                    }
                    
                    ChiTietHoaDon newCTHD = new ChiTietHoaDon();
                    newCTHD.setHoaDon(freshHoaDon);
                    newCTHD.setThuoc(freshThuoc);
                    newCTHD.setSoLuong(cartItem.getSoLuong());
                    newCTHD.setDonGia(cartItem.getDonGia());
                    
                    em.persist(newCTHD);
                    
                    int newSoLuongTon = freshThuoc.getSoLuongTon() - cartItem.getSoLuong();
                    freshThuoc.setSoLuongTon(newSoLuongTon);
                    tx.commit();
                }
                
                return true;
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                return false;
            } finally {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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