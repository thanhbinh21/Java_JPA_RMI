package service;

import entity.NhaCungCap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NhaCungCapService extends GenericService<NhaCungCap, String> {
    /**
     * Find a supplier by name
     * @param ten supplier name
     * @return the supplier if found, otherwise empty
     * @throws RemoteException if RMI communication fails
     */
    NhaCungCap findByTen(String ten) throws RemoteException;
    
    /**
     * Search suppliers with a keyword in their name, address, or phone number
     * @param keyword search keyword
     * @return list of matching suppliers
     * @throws RemoteException if RMI communication fails
     */
    List<NhaCungCap> searchSuppliers(String keyword) throws RemoteException;
} 