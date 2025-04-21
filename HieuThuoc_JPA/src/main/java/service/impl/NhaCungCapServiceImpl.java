package service.impl;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;
import service.NhaCungCapService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NhaCungCapServiceImpl extends UnicastRemoteObject implements NhaCungCapService {
    private final NhaCungCapDAO nhaCungCapDAO;

    public NhaCungCapServiceImpl(NhaCungCapDAO nhaCungCapDAO) throws RemoteException {
        super();
        this.nhaCungCapDAO = nhaCungCapDAO;
    }

    @Override
    public NhaCungCap findById(String id) throws RemoteException {
        return nhaCungCapDAO.findById(id);
    }

    @Override
    public List<NhaCungCap> findAll() throws RemoteException {
        return nhaCungCapDAO.findAll();
    }

    @Override
    public boolean save(NhaCungCap entity) throws RemoteException {
        return nhaCungCapDAO.save(entity);
    }

    @Override
    public boolean update(NhaCungCap entity) throws RemoteException {
        return nhaCungCapDAO.update(entity);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        return nhaCungCapDAO.delete(id);
    }

    @Override
    public NhaCungCap findByTen(String ten) throws RemoteException {
        Optional<NhaCungCap> nhaCungCap = nhaCungCapDAO.findByTen(ten);
        return nhaCungCap.orElse(null);
    }

    @Override
    public List<NhaCungCap> searchSuppliers(String keyword) throws RemoteException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        
        String searchTerm = keyword.toLowerCase();
        return nhaCungCapDAO.findAll().stream()
                .filter(ncc -> 
                    ncc.getTen().toLowerCase().contains(searchTerm) || 
                    (ncc.getDiaChi() != null && ncc.getDiaChi().toLowerCase().contains(searchTerm)) || 
                    (ncc.getSdt() != null && ncc.getSdt().contains(searchTerm))
                )
                .collect(Collectors.toList());
    }
} 