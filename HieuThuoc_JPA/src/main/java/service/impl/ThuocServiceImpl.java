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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return findAll();
            }
            
            String searchTerm = keyword.toLowerCase().trim();
            
            // Search across multiple fields
            return findAll().stream()
                .filter(thuoc -> 
                    (thuoc.getId() != null && thuoc.getId().toLowerCase().contains(searchTerm)) ||
                    (thuoc.getTen() != null && thuoc.getTen().toLowerCase().contains(searchTerm)) ||
                    (thuoc.getThanhPhan() != null && thuoc.getThanhPhan().toLowerCase().contains(searchTerm))
                )
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteException("Error searching for medicines: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> getMaTenThuoc() throws RemoteException {
        try {
            List<Thuoc> thuocList = findAll();
            List<Object[]> result = new ArrayList<>();
            
            for (Thuoc thuoc : thuocList) {
                result.add(new Object[]{thuoc.getId(), thuoc.getTen()});
            }
            
            return result;
        } catch (Exception e) {
            throw new RemoteException("Error retrieving medicine IDs and names: " + e.getMessage(), e);
        }
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

    @Override
    public List<Thuoc> findByCategory(String categoryId) throws RemoteException {
        try {
            DanhMuc danhMuc = danhMucDAO.findById(categoryId);
            if (danhMuc == null) {
                return List.of();
            }
            return thuocDAO.selectByDanhMuc(danhMuc);
        } catch (Exception e) {
            throw new RemoteException("Error finding medicines by category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Thuoc> findByManufacturer(String manufacturerId) throws RemoteException {
        try {
            NhaSanXuat nhaSanXuat = nhaSanXuatDAO.findById(manufacturerId);
            if (nhaSanXuat == null) {
                return List.of();
            }
            
            // Filter medicines by manufacturer
            return findAll().stream()
                .filter(thuoc -> 
                    thuoc.getNhaSanXuat() != null && 
                    thuoc.getNhaSanXuat().getId().equals(manufacturerId)
                )
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteException("Error finding medicines by manufacturer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Thuoc> findByCategoryAndManufacturer(String categoryId, String manufacturerId) throws RemoteException {
        try {
            DanhMuc danhMuc = danhMucDAO.findById(categoryId);
            NhaSanXuat nhaSanXuat = nhaSanXuatDAO.findById(manufacturerId);
            
            if (danhMuc == null || nhaSanXuat == null) {
                return List.of();
            }
            
            // Filter by both category and manufacturer
            return findAll().stream()
                .filter(thuoc -> 
                    thuoc.getDanhMuc() != null && 
                    thuoc.getDanhMuc().getId().equals(categoryId) &&
                    thuoc.getNhaSanXuat() != null && 
                    thuoc.getNhaSanXuat().getId().equals(manufacturerId)
                )
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteException("Error finding medicines by category and manufacturer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Thuoc> findByName(String searchValue) throws RemoteException {
        try {
            if (searchValue == null || searchValue.trim().isEmpty()) {
                return findAll();
            }
            
            String searchTerm = searchValue.toLowerCase().trim();
            
            // Search by name
            return findAll().stream()
                .filter(thuoc -> 
                    thuoc.getTen() != null && 
                    thuoc.getTen().toLowerCase().contains(searchTerm)
                )
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteException("Error finding medicines by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Thuoc> findByIngredient(String searchValue) throws RemoteException {
        try {
            if (searchValue == null || searchValue.trim().isEmpty()) {
                return findAll();
            }
            
            String searchTerm = searchValue.toLowerCase().trim();
            
            // Search by ingredient
            return findAll().stream()
                .filter(thuoc -> 
                    thuoc.getThanhPhan() != null && 
                    thuoc.getThanhPhan().toLowerCase().contains(searchTerm)
                )
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteException("Error finding medicines by ingredient: " + e.getMessage(), e);
        }
    }
}