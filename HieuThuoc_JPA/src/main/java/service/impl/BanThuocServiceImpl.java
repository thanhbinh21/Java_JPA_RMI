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
            boolean saveHoaDonResult = hoaDonDAO.save(hoaDon);
            if (!saveHoaDonResult) {
                return false;
            }

            for (ChiTietHoaDon cartItem : cart) {
                ChiTietHoaDon newCTHD = new ChiTietHoaDon();
                newCTHD.setHoaDon(hoaDon);
                
                // Get a fresh Thuoc from the database with a clean session
                String thuocId = cartItem.getThuoc().getId();
                Thuoc thuoc = thuocDAO.findById(thuocId);
                if (thuoc == null) {
                    return false;
                }
                
                newCTHD.setThuoc(thuoc);
                newCTHD.setSoLuong(cartItem.getSoLuong());
                newCTHD.setDonGia(cartItem.getDonGia());
                boolean saveChiTietResult = chiTietHoaDonDAO.save(newCTHD);
                if (!saveChiTietResult) {
                    return false;
                }
                
                int newSoLuongTon = thuoc.getSoLuongTon() - cartItem.getSoLuong();
                thuoc.setSoLuongTon(newSoLuongTon);
                thuocDAO.update(thuoc);
            }
            
            return true;
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