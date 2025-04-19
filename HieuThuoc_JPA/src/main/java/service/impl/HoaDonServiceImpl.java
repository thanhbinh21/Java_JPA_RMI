package service.impl;

import dao.HoaDonDAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import java.rmi.RemoteException;
import java.util.List;
import service.HoaDonService;

public class HoaDonServiceImpl extends GenericServiceImpl<HoaDon, String> implements HoaDonService {

    private final HoaDonDAO hoaDonDAO;

    public HoaDonServiceImpl(HoaDonDAO hoaDonDAO) throws RemoteException {
        super(hoaDonDAO);
        this.hoaDonDAO = hoaDonDAO;
    }

    @Override
    public List<HoaDon> findByKhachHang(KhachHang khachHang) throws RemoteException {
        return hoaDonDAO.findByKhachHang(khachHang);
    }

    @Override
    public List<HoaDon> findByNhanVien(NhanVien nhanVien) throws RemoteException {
        return hoaDonDAO.findByNhanVien(nhanVien);
    }

    @Override
    public List<Object[]> getSoLuongHoaDonTheoKhachHang() throws RemoteException {
        return hoaDonDAO.getSoLuongHoaDonTheoKhachHang();
    }
}