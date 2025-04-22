package service.impl;

import dao.ChiTietPhieuNhapThuocDAO;
import dao.PhieuNhapThuocDAO;
import dao.ThuocDAO;
import dao.impl.ChiTietPhieuNhapThuocDAOImpl;
import entity.ChiTietPhieuNhapThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import service.ChiTietPhieuNhapThuocService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;


public class ChiTietPhieuNhapThuocServiceImpl extends UnicastRemoteObject implements ChiTietPhieuNhapThuocService {
    private final ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO;
    private final PhieuNhapThuocDAO phieuNhapThuocDAO;
    private final ThuocDAO thuocDAO;

    public ChiTietPhieuNhapThuocServiceImpl(ChiTietPhieuNhapThuocDAO chiTietPhieuNhapThuocDAO, 
                                           PhieuNhapThuocDAO phieuNhapThuocDAO,
                                           ThuocDAO thuocDAO) throws RemoteException {
        super();
        this.chiTietPhieuNhapThuocDAO = chiTietPhieuNhapThuocDAO;
        this.phieuNhapThuocDAO = phieuNhapThuocDAO;
        this.thuocDAO = thuocDAO;
    }

    @Override
    public ChiTietPhieuNhapThuoc findById(Long id) throws RemoteException {
        return null;

    }

    @Override
    public List<ChiTietPhieuNhapThuoc> findAll() throws RemoteException {
        return chiTietPhieuNhapThuocDAO.findAll();
    }

    @Override
    public boolean save(ChiTietPhieuNhapThuoc entity) throws RemoteException {
        return chiTietPhieuNhapThuocDAO.save(entity);
    }

    @Override
    public boolean update(ChiTietPhieuNhapThuoc entity) throws RemoteException {
        return chiTietPhieuNhapThuocDAO.update(entity);
    }

    @Override
    public boolean delete(Long id) throws RemoteException {
        return false;
    }

    @Override
    public List<ChiTietPhieuNhapThuoc> findByPhieuNhapThuoc(String phieuNhapThuocId) throws RemoteException {
        PhieuNhapThuoc phieuNhapThuoc = phieuNhapThuocDAO.findById(phieuNhapThuocId);
        if (phieuNhapThuoc == null) {
            return List.of();
        }
        return chiTietPhieuNhapThuocDAO.findByPhieuNhapThuoc(phieuNhapThuoc);
    }

    @Override
    public ChiTietPhieuNhapThuoc findByThuocAndPhieuNhapThuoc(String thuocId, String phieuNhapThuocId) throws RemoteException {
        Thuoc thuoc = thuocDAO.findById(thuocId);
        PhieuNhapThuoc phieuNhapThuoc = phieuNhapThuocDAO.findById(phieuNhapThuocId);
        
        if (thuoc == null || phieuNhapThuoc == null) {
            return null;
        }
        
        Optional<ChiTietPhieuNhapThuoc> chiTietOpt = chiTietPhieuNhapThuocDAO.findByThuocAndPhieuNhapThuoc(thuoc, phieuNhapThuoc);
        return chiTietOpt.orElse(null);
    }

    @Override
    public boolean saveAll(List<ChiTietPhieuNhapThuoc> chiTietPhieuNhapThuocs) throws RemoteException {
        boolean allSuccess = true;
        for (ChiTietPhieuNhapThuoc chiTiet : chiTietPhieuNhapThuocs) {
            boolean success = chiTietPhieuNhapThuocDAO.save(chiTiet);
            if (!success) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    public boolean createAndSaveChiTiet(String phieuNhapId, String thuocId, int soLuong, double donGia) throws RemoteException {
        // Check if the entities exist
        PhieuNhapThuoc phieuNhap = phieuNhapThuocDAO.findById(phieuNhapId);
        Thuoc thuoc = thuocDAO.findById(thuocId);
        
        if (phieuNhap == null || thuoc == null) {
            return false;
        }
        
        // Use the DAO's direct SQL method to avoid Hibernate session issues
        return ((ChiTietPhieuNhapThuocDAOImpl) chiTietPhieuNhapThuocDAO).insertChiTietPhieuNhap(
                phieuNhapId, thuocId, soLuong, donGia);
    }

    @Override
    public double calculateTotalAmount(String phieuNhapThuocId) throws RemoteException {
        List<ChiTietPhieuNhapThuoc> chiTietList = findByPhieuNhapThuoc(phieuNhapThuocId);
        return chiTietList.stream()
                .mapToDouble(ct -> ct.getSoLuong() * ct.getDonGia())
                .sum();
    }
} 