package service.impl;

import dao.DanhMucDAO;
import dao.KhuyenMaiDAO;
import dao.NhaSanXuatDAO;
import dao.ThuocDAO;
import entity.DanhMuc;
import entity.KhuyenMai;
import entity.NhaSanXuat;
import entity.Thuoc;
import java.rmi.RemoteException;
import java.util.List;
import service.ThuocService;

public class ThuocServiceImpl extends GenericServiceImpl<Thuoc, String> implements ThuocService {

    private final ThuocDAO thuocDAO;
    private final DanhMucDAO danhMucDAO;
    private final NhaSanXuatDAO nhaSanXuatDAO;
    private final KhuyenMaiDAO khuyenMaiDAO;

    public ThuocServiceImpl(ThuocDAO thuocDAO, DanhMucDAO danhMucDAO, NhaSanXuatDAO nhaSanXuatDAO, KhuyenMaiDAO khuyenMaiDAO) throws RemoteException {
        super(thuocDAO);
        this.thuocDAO = thuocDAO;
        this.danhMucDAO = danhMucDAO;
        this.nhaSanXuatDAO = nhaSanXuatDAO;
        this.khuyenMaiDAO = khuyenMaiDAO;
    }



    @Override
    public List<DanhMuc> getAllDanhMuc() throws RemoteException {
        return danhMucDAO.findAll();
    }

    @Override
    public List<Thuoc> getThuocByDanhMuc(String maDM) throws RemoteException {
        return thuocDAO.selectByDanhMuc(danhMucDAO.findById(maDM));
    }

    @Override
    public List<Thuoc> searchThuoc(String keyword) throws RemoteException {
        return List.of();
    }

    @Override
    public List<Object[]> getMaTenThuoc() throws RemoteException {
        return List.of();
    }

    public List<Thuoc> getThuocByTenDanhMuc(String tenDM) throws RemoteException {
        return thuocDAO.selectByDanhMuc(danhMucDAO.findByTen(tenDM));
    }

    @Override
    public NhaSanXuat findNhaSanXuatById(String nhaSanXuatStr) throws RemoteException {
        try {
            return nhaSanXuatDAO.findById(nhaSanXuatStr);
        } catch (Exception e) {
            throw new RemoteException("Error finding NhaSanXuat by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public DanhMuc findDanhMucById(String danhMucStr) throws RemoteException {
        try {
            // Assuming you have a DAO method to find DanhMuc by ID
            return danhMucDAO.findById(danhMucStr);
        } catch (Exception e) {
            throw new RemoteException("Error finding DanhMuc by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public KhuyenMai findKhuyenMaiById(String khuyenMaiStr) throws RemoteException {
        try {
            return khuyenMaiDAO.findById(khuyenMaiStr);
        } catch (Exception e) {
            throw new RemoteException("Error finding KhuyenMai by ID: " + e.getMessage(), e);
        }
    }

}