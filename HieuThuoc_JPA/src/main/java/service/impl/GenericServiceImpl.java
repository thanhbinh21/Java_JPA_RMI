package service.impl;

import dao.GenericDAO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import service.GenericService;

public abstract class GenericServiceImpl<T, ID> extends UnicastRemoteObject implements GenericService<T, ID> {

    protected GenericDAO<T, ID> genericDAO;

    public GenericServiceImpl(GenericDAO<T, ID> genericDAO) throws RemoteException {
        super();
        this.genericDAO = genericDAO;
    }

    @Override
    public T findById(ID id) throws RemoteException {
        return genericDAO.findById(id);
    }

    @Override
    public List<T> findAll() throws RemoteException {
        return genericDAO.findAll();
    }

    @Override
    public boolean save(T entity) throws RemoteException {
        return genericDAO.save(entity);
    }

    @Override
    public boolean update(T entity) throws RemoteException {
        return genericDAO.update(entity);
    }

    @Override
    public boolean delete(ID id) throws RemoteException {
        return genericDAO.delete(id);
    }
}